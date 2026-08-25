#!/usr/bin/env bash
# =============================================================================
# test-acme-certificate-states.sh — Test des cas Phase 0 (guards)
# =============================================================================
#
# Teste deux cas où un certificat est déjà en base :
#   CAS A — Certificat DEPLOYED valide (expiresAt > now+30j)
#           → Le UseCase doit s'arrêter immédiatement, sans contacter l'ACME ni Cloudflare
#   CAS B — Certificat TO_DEPLOY valide (expiresAt > now+30j, déploiement manqué)
#           → Le UseCase doit rejouer uniquement le déploiement Cloudflare, sans contacter l'ACME
#
# Prérequis :
#   - L'application doit être démarrée avec .env.acme_test comme source d'env
#   - node, jq, curl, psql disponibles dans le PATH
#
# Usage :
#   ./scripts/test-acme-certificate-states.sh
# =============================================================================

set -euo pipefail

# ─── Couleurs ─────────────────────────────────────────────────────────────────
CYAN='\033[0;36m'
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
WHITE='\033[0;37m'
BOLD='\033[1m'
RESET='\033[0m'

# ─── Compteurs ────────────────────────────────────────────────────────────────
PASS=0
FAIL=0

# ─── Helpers de log ───────────────────────────────────────────────────────────
step()     { echo -e "\n${CYAN}${BOLD}[STEP]${RESET} $*"; }
scenario() { echo -e "\n${YELLOW}${BOLD}[CAS]${RESET}  $*"; }
info()     { echo -e "${WHITE}[INFO]${RESET} $*"; }
ok()       { echo -e "${GREEN}[OK]${RESET}   $*"; PASS=$((PASS + 1)); }
fail()     { echo -e "${RED}[FAIL]${RESET} $*"; FAIL=$((FAIL + 1)); }

# ─── Localisation du projet ───────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
ENV_FILE="$PROJECT_ROOT/.env.acme_test"

echo -e "${BOLD}"
echo "╔══════════════════════════════════════════════════════════════╗"
echo "║   TEST ACME — ÉTATS DU CERTIFICAT (DEPLOYED / TO_DEPLOY)    ║"
echo "╚══════════════════════════════════════════════════════════════╝"
echo -e "${RESET}"

# =============================================================================
# ÉTAPE 0 — Prérequis et configuration
# =============================================================================
step "0 — Vérification des prérequis et chargement de la configuration"

for cmd in node jq curl; do
  if command -v "$cmd" &>/dev/null; then
    ok "  '$cmd' trouvé"
  else
    fail "  '$cmd' introuvable — installer avant de relancer"
  fi
done

PSQL_BIN=""
for candidate in psql \
    /Applications/Postgres.app/Contents/Versions/14/bin/psql \
    /Applications/Postgres.app/Contents/Versions/15/bin/psql \
    /opt/homebrew/bin/psql \
    /usr/local/bin/psql; do
  if command -v "$candidate" &>/dev/null 2>&1; then
    PSQL_BIN="$candidate"
    break
  fi
done
if [ -n "$PSQL_BIN" ]; then
  ok "  'psql' trouvé : $PSQL_BIN"
else
  fail "  'psql' introuvable"
fi

if [ "$FAIL" -gt 0 ]; then
  echo -e "\n${RED}${BOLD}Prérequis manquants — arrêt.${RESET}"
  exit 1
fi

if [ ! -f "$ENV_FILE" ]; then
  fail "Fichier '$ENV_FILE' introuvable."
  exit 1
fi
ok "Fichier .env.acme_test trouvé"

while IFS= read -r line || [ -n "$line" ]; do
  [[ "$line" =~ ^[[:space:]]*# ]] && continue
  [[ -z "${line//[[:space:]]/}" ]] && continue
  clean="${line%%  #*}"
  clean="${clean%% #*}"
  clean="${clean%%	#*}"
  [[ "$clean" =~ ^[A-Z_]+= ]] || continue
  export "$clean" 2>/dev/null || true
done < "$ENV_FILE"

for var in DATABASE_URL JWT_SECRET ACME_SERVER_URL CLOUDFLARE_BASE_URL ACME_DOMAIN ACME_ENCRYPTION_KEY; do
  val=$(eval "echo \"\${${var}:-}\"")
  if [ -z "$val" ]; then
    fail "Variable $var non définie dans .env.acme_test"
  fi
done

if [ "$FAIL" -gt 0 ]; then
  echo -e "\n${RED}${BOLD}Configuration incomplète — arrêt.${RESET}"
  exit 1
fi

BASE_URL="http://localhost:${PORT:-8080}"

# ─── Helpers DB ───────────────────────────────────────────────────────────────
DB_URL_CLEAN="${DATABASE_URL#postgresql://}"
DB_USER=$(echo "$DB_URL_CLEAN" | cut -d: -f1)
DB_REST="${DB_URL_CLEAN#*:}"
DB_PASS=$(echo "$DB_REST" | cut -d@ -f1)
DB_HOST_PORT=$(echo "$DB_REST" | cut -d@ -f2 | cut -d/ -f1)
DB_HOST=$(echo "$DB_HOST_PORT" | cut -d: -f1)
DB_PORT=$(echo "$DB_HOST_PORT" | cut -d: -f2)
DB_NAME=$(echo "$DB_REST" | cut -d/ -f2)
export PGPASSWORD="$DB_PASS"

run_sql() {
  "$PSQL_BIN" -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "$1" 2>&1
}

# ─── Helper chiffrement AES-256-GCM (identique à AcmeCryptoHelper.kt) ────────
# Chiffre le contenu d'un fichier temporaire et retourne base64([IV 12 bytes][ciphertext+tag])
# Usage : encrypt_aes_file <chemin_fichier>
# (on passe par un fichier pour éviter les problèmes d'échappement des sauts de ligne PEM)
encrypt_aes_file() {
  local tmpfile="$1"
  node -e "
const crypto = require('crypto');
const fs = require('fs');
const key = Buffer.from('${ACME_ENCRYPTION_KEY}', 'base64');
const iv = crypto.randomBytes(12);
const plaintext = fs.readFileSync('${tmpfile}', 'utf8');
const cipher = crypto.createCipheriv('aes-256-gcm', key, iv);
const enc = Buffer.concat([cipher.update(plaintext, 'utf8'), cipher.final()]);
const tag = cipher.getAuthTag();
const combined = Buffer.concat([iv, enc, tag]);
console.log(combined.toString('base64'));
"
}

# ─── Vérification serveur UP ─────────────────────────────────────────────────
step "1 — Vérification que le serveur est démarré"
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/stub/acme/directory" 2>/dev/null || echo "000")
if [ "$HTTP_STATUS" = "200" ]; then
  ok "Serveur disponible (HTTP $HTTP_STATUS)"
else
  fail "Serveur indisponible (HTTP $HTTP_STATUS) — lancer l'application avec .env.acme_test"
  exit 1
fi

# ─── JWT admin ───────────────────────────────────────────────────────────────
step "2 — Génération du JWT admin"
ADMIN_ID=$(run_sql "SELECT id FROM agora_users WHERE authorization_level = 1337 AND is_banned = 0 LIMIT 1;" | tr -d ' \n\t')
if [ -z "$ADMIN_ID" ]; then
  fail "Aucun utilisateur admin trouvé en base"
  exit 1
fi
ok "Admin trouvé : $ADMIN_ID"

JWT=$(node -e "
const crypto = require('crypto');
const key = Buffer.from('${JWT_SECRET}', 'base64');
function b64u(buf) { return buf.toString('base64').replace(/\+/g,'-').replace(/\//g,'_').replace(/=/g,''); }
const h = b64u(Buffer.from(JSON.stringify({alg:'HS512',typ:'JWT'})));
const now = Math.floor(Date.now()/1000);
const p = b64u(Buffer.from(JSON.stringify({sub:'${ADMIN_ID}',iat:now,exp:now+3600})));
const s = b64u(crypto.createHmac('sha512',key).update(h+'.'+p).digest());
console.log(h+'.'+p+'.'+s);
")
ok "JWT généré : ${JWT:0:40}..."

# ─── Certificat PEM de test (auto-signé minimaliste en base64) ────────────────
# Certificat auto-signé généré pour les tests, valide 90 jours
DUMMY_CERT="-----BEGIN CERTIFICATE-----
MIICpDCCAYwCCQDU6pQ4pHnCrTANBgkqhkiG9w0BAQsFADAUMRIwEAYDVQQDDAls
b2NhbC50ZXN0MB4XDTIzMDEwMTAwMDAwMFoXDTI2MDEwMTAwMDAwMFowFDESMBAG
A1UEAwwJbG9jYWwudGVzdDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB
AKvxMFXMIhAgeFSBjfJh2UaCHONzG4bEoMm/L1GbITRdAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADQIDAQABo1MwUTAdBg
NVHQ4EFgQU+fakeCXJNZrCi1jXAAAAAAAAAAAwHwYDVR0jBBgwFoAU+fakeCXJNZ
rCi1jXAAAAAAAAAAAwDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQ
EAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAo=
-----END CERTIFICATE-----
"
DUMMY_PRIVKEY="-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCr8TBVzCIQIHhU
gY3yYdlGghzjcxuGxKDJvy9RmyE0XQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgMBAAECggEAAAAAAAAAAA==
-----END PRIVATE KEY-----
"

# =============================================================================
# CAS A — Certificat DEPLOYED valide → aucune action attendue
# =============================================================================
scenario "A — Certificat DEPLOYED (expiresAt = now+90j) → UseCase doit s'arrêter sans rien faire"

step "A.1 — Purge + insertion du certificat DEPLOYED en base"
run_sql "DELETE FROM acme_order       WHERE domain = '${ACME_DOMAIN}';" > /dev/null
run_sql "DELETE FROM acme_account     WHERE server_url LIKE '%stub%';"  > /dev/null
run_sql "DELETE FROM acme_certificate WHERE domain = '${ACME_DOMAIN}';" > /dev/null

# Chiffrer la clé privée de test via fichier temporaire (évite les pb de sauts de ligne PEM)
PRIVKEY_TMP=$(mktemp)
printf '%s' "$DUMMY_PRIVKEY" > "$PRIVKEY_TMP"
ENCRYPTED_PRIVKEY=$(encrypt_aes_file "$PRIVKEY_TMP")
rm -f "$PRIVKEY_TMP"

run_sql "
INSERT INTO acme_certificate (domain, certificate, private_key, expires_at, created_at, status)
VALUES (
  '${ACME_DOMAIN}',
  \$\$${DUMMY_CERT}\$\$,
  '${ENCRYPTED_PRIVKEY}',
  NOW() + INTERVAL '90 days',
  NOW(),
  'DEPLOYED'
);
" > /dev/null
ok "Certificat DEPLOYED inséré en base (expires_at = now+90j)"

step "A.2 — Reset du stub store"
RESET_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -X DELETE \
  -H "Authorization: Bearer $JWT" \
  "$BASE_URL/admin/stub/acme/calls/reset")
if [ "$RESET_STATUS" = "200" ]; then
  ok "Stub store réinitialisé"
else
  fail "Reset du stub store échoué (HTTP $RESET_STATUS)"
fi

step "A.3 — Appel POST /admin/acme/renew-certificate"
RENEW_RESPONSE=$(curl -s -w "\n__HTTP_STATUS__:%{http_code}" \
  -X POST \
  -H "Authorization: Bearer $JWT" \
  "$BASE_URL/admin/acme/renew-certificate")
RENEW_STATUS=$(echo "$RENEW_RESPONSE" | grep "__HTTP_STATUS__" | cut -d: -f2)
info "  HTTP Status : $RENEW_STATUS"
if [ "$RENEW_STATUS" = "200" ]; then
  ok "Requête exécutée (HTTP 200)"
else
  fail "Requête échouée (HTTP $RENEW_STATUS)"
fi

step "A.4 — Vérification : aucun appel ACME ni Cloudflare enregistré"
CALLS_JSON=$(curl -s -H "Authorization: Bearer $JWT" "$BASE_URL/admin/stub/acme/calls")
CALLS_COUNT=$(echo "$CALLS_JSON" | jq 'length' 2>/dev/null || echo "0")
info "  Nombre d'appels enregistrés : $CALLS_COUNT"
if [ "$CALLS_COUNT" = "0" ]; then
  ok "Aucun appel stub enregistré — UseCase s'est arrêté en Phase 0 ✓"
else
  fail "Des appels ont été enregistrés alors que le certificat est valide et déployé : $CALLS_JSON"
fi

step "A.5 — Vérification : certificat toujours DEPLOYED en base"
DB_STATUS=$(run_sql "SELECT status FROM acme_certificate WHERE domain = '${ACME_DOMAIN}' ORDER BY created_at DESC LIMIT 1;" | tr -d ' \n\t')
info "  Statut en base : $DB_STATUS"
if echo "$DB_STATUS" | grep -qi "DEPLOYED"; then
  ok "Certificat toujours DEPLOYED en base — aucune modification ✓"
else
  fail "Statut inattendu en base : $DB_STATUS"
fi

# =============================================================================
# CAS B — Certificat TO_DEPLOY valide → retry Cloudflare seulement
# =============================================================================
scenario "B — Certificat TO_DEPLOY (expiresAt = now+90j) → UseCase doit rejouer uniquement Cloudflare"

step "B.1 — Mise à jour du statut en base → TO_DEPLOY"
run_sql "UPDATE acme_certificate SET status = 'TO_DEPLOY', deployed_at = NULL WHERE domain = '${ACME_DOMAIN}';" > /dev/null
run_sql "DELETE FROM acme_order WHERE domain = '${ACME_DOMAIN}';" > /dev/null
ok "Certificat mis à jour en statut TO_DEPLOY"

step "B.2 — Reset du stub store"
RESET_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -X DELETE \
  -H "Authorization: Bearer $JWT" \
  "$BASE_URL/admin/stub/acme/calls/reset")
if [ "$RESET_STATUS" = "200" ]; then
  ok "Stub store réinitialisé"
else
  fail "Reset du stub store échoué (HTTP $RESET_STATUS)"
fi

step "B.3 — Appel POST /admin/acme/renew-certificate"
RENEW_RESPONSE=$(curl -s -w "\n__HTTP_STATUS__:%{http_code}" \
  -X POST \
  -H "Authorization: Bearer $JWT" \
  "$BASE_URL/admin/acme/renew-certificate")
RENEW_STATUS=$(echo "$RENEW_RESPONSE" | grep "__HTTP_STATUS__" | cut -d: -f2)
info "  HTTP Status : $RENEW_STATUS"
if [ "$RENEW_STATUS" = "200" ]; then
  ok "Requête exécutée (HTTP 200)"
else
  fail "Requête échouée (HTTP $RENEW_STATUS)"
fi

step "B.4 — Vérification : aucun appel ACME, mais un appel Cloudflare"
CALLS_JSON=$(curl -s -H "Authorization: Bearer $JWT" "$BASE_URL/admin/stub/acme/calls")
info "  Appels enregistrés :"
echo "$CALLS_JSON" | jq -r '.[] | "    \(.method) \(.endpoint)"' 2>/dev/null || echo "    (impossible de parser)"

# Vérifier absence d'appels ACME
HAS_NEW_ORDER=$(echo "$CALLS_JSON" | jq -r '.[].endpoint' 2>/dev/null | grep -c "new-order" || true)
HAS_NEW_ACCOUNT=$(echo "$CALLS_JSON" | jq -r '.[].endpoint' 2>/dev/null | grep -c "new-account" || true)
if [ "$HAS_NEW_ORDER" = "0" ] && [ "$HAS_NEW_ACCOUNT" = "0" ]; then
  ok "Aucun appel ACME (new-order, new-account) — provisioning sauté ✓"
else
  fail "Des appels ACME ont été effectués alors que needsProvisioning=false (new-order=$HAS_NEW_ORDER, new-account=$HAS_NEW_ACCOUNT)"
fi

# Vérifier présence appel Cloudflare
HAS_CLOUDFLARE=$(echo "$CALLS_JSON" | jq -r '.[].endpoint' 2>/dev/null | grep -c "stub/cloudflare/zones/" || true)
if [ "$HAS_CLOUDFLARE" -gt "0" ]; then
  ok "Appel Cloudflare présent — déploiement rejoué ✓"
else
  fail "Aucun appel Cloudflare enregistré — le déploiement aurait dû être rejoué"
fi

step "B.5 — Vérification : certificat passe à DEPLOYED en base"
DB_STATUS=$(run_sql "SELECT status FROM acme_certificate WHERE domain = '${ACME_DOMAIN}' ORDER BY created_at DESC LIMIT 1;" | tr -d ' \n\t')
info "  Statut en base : $DB_STATUS"
if echo "$DB_STATUS" | grep -qi "DEPLOYED"; then
  ok "Certificat mis à jour en statut DEPLOYED ✓"
else
  fail "Statut inattendu en base : $DB_STATUS"
fi

step "B.6 — Vérification : acme_order absent en base"
DB_ORDER_COUNT=$(run_sql "SELECT count(*) FROM acme_order WHERE domain = '${ACME_DOMAIN}';" | tr -d ' \n\t')
info "  Lignes acme_order restantes : $DB_ORDER_COUNT"
if [ "$DB_ORDER_COUNT" = "0" ]; then
  ok "acme_order vide après déploiement ✓"
else
  fail "acme_order non nettoyé (count=$DB_ORDER_COUNT)"
fi

# =============================================================================
# BILAN FINAL
# =============================================================================
echo ""
echo -e "${BOLD}═══════════════════════════════════════════════════════════${RESET}"
echo -e "${BOLD}BILAN — ÉTATS CERTIFICAT (DEPLOYED / TO_DEPLOY)${RESET}"
echo -e "═══════════════════════════════════════════════════════════"
echo -e "  ${GREEN}✓ Succès  : $PASS${RESET}"
echo -e "  ${RED}✗ Échecs  : $FAIL${RESET}"
echo -e "═══════════════════════════════════════════════════════════"

if [ "$FAIL" -eq 0 ]; then
  echo -e "\n${GREEN}${BOLD}✅ TOUS LES CHECKS PASSENT — Guards Phase 0 validés${RESET}\n"
  exit 0
else
  echo -e "\n${RED}${BOLD}❌ $FAIL CHECK(S) ÉCHOUÉ(S) — Voir les détails ci-dessus${RESET}\n"
  exit 1
fi
