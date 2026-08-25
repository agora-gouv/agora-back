#!/usr/bin/env bash
# =============================================================================
# test-acme-resume-order.sh — Test des reprises sur incident (Phase 1)
# =============================================================================
#
# Teste deux cas de reprise où un order est déjà en base :
#
#   CAS A — Reprise depuis CHALLENGE_PENDING (crash avant finalisation)
#           Setup  : créer un order stub via l'API, insérer acme_order en base
#                    avec status=CHALLENGE_PENDING et l'order_url du stub
#           Attendu: le UseCase reprend depuis le challenge, finalise et déploie
#                    → aucun appel new-order, mais authz + challenge + finalize + cert + cloudflare
#
#   CAS B — Reprise depuis ORDER_FINALIZING (crash après challenge VALID)
#           Setup  : idem mais status=ORDER_FINALIZING (challenge déjà validé dans le stub)
#           Attendu: le UseCase poll directement l'order, télécharge le cert et déploie
#                    → aucun appel new-order ni challenge, mais finalize + cert + cloudflare
#
# Prérequis :
#   - L'application doit être démarrée avec .env.acme_test comme source d'env
#   - node, jq, curl, psql disponibles dans le PATH
#
# Usage :
#   ./scripts/test-acme-resume-order.sh
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
echo "║   TEST ACME — REPRISES SUR INCIDENT (CHALLENGE / FINALIZE)  ║"
echo "╚══════════════════════════════════════════════════════════════╝"
echo -e "${RESET}"

# =============================================================================
# ÉTAPE 0 — Prérequis et configuration
# =============================================================================
step "0 — Vérification des prérequis et chargement de la configuration"

for cmd in node jq curl openssl; do
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
# Chiffre une chaîne et retourne base64([IV 12 bytes][ciphertext+tag])
# Usage : encrypt_aes_file <fichier_contenant_le_texte_clair>
# (on passe par un fichier pour éviter les problèmes d'échappement des sauts de ligne)
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

# ─── Helper : générer une keypair EC P-256 acme4j-compatible via le stub ─────
# Le stub génère la keypair avec KeyPairUtils.createECKeyPair("P-256") et la
# chiffre directement avec AcmeCryptoHelper — garantissant la compatibilité acme4j.
# Retourne la keypair déjà chiffrée (prête pour INSERT en base).
generate_encrypted_keypair() {
  curl -s -X POST "$BASE_URL/stub/acme/generate-keypair" | jq -r '.encryptedKeyPair'
}

# ─── Helper : créer un order dans le stub et retourner l'orderId ─────────────
# Appelle POST /stub/acme/new-order avec un corps JWS minimal (le stub l'ignore)
# Retourne l'orderId extrait du header Location
create_stub_order() {
  local response
  response=$(curl -s -D - -X POST \
    -H "Content-Type: application/jose+json" \
    -d '{"payload":"","protected":"","signature":""}' \
    "$BASE_URL/stub/acme/new-order")

  # Extraire le Location header : Location: http://localhost:8080/stub/acme/order/{orderId}
  local location
  location=$(echo "$response" | grep -i "^Location:" | tr -d '\r' | awk '{print $2}')
  echo "$location"
}

# =============================================================================
# SETUP COMMUN : générer les keypairs et préparer le stub
# =============================================================================
step "3 — Génération des keypairs de test (account + domain)"

# Keypair account (EC P-256, générée + chiffrée par le stub via KeyPairUtils acme4j)
ENCRYPTED_ACCOUNT_KEY=$(generate_encrypted_keypair)
ok "Keypair account générée et chiffrée"

# Keypair domaine (idem)
ENCRYPTED_DOMAIN_KEY=$(generate_encrypted_keypair)
ok "Keypair domaine générée et chiffrée"

# =============================================================================
# CAS A — Reprise depuis CHALLENGE_PENDING
# =============================================================================
scenario "A — Reprise depuis CHALLENGE_PENDING (crash avant finalisation)"
info "  Le UseCase doit : re-trigger challenge → finaliser → télécharger cert → déployer Cloudflare"
info "  Il NE doit PAS appeler : new-order, new-account"

step "A.1 — Purge des données ACME"
run_sql "DELETE FROM acme_order       WHERE domain = '${ACME_DOMAIN}';" > /dev/null
run_sql "DELETE FROM acme_account     WHERE server_url LIKE '%stub%';"  > /dev/null
run_sql "DELETE FROM acme_certificate WHERE domain = '${ACME_DOMAIN}';" > /dev/null
ok "Base purgée"

step "A.2 — Pré-création d'un order dans le stub ACME"
# Réinitialiser le stub store pour un départ propre
curl -s -o /dev/null -X DELETE -H "Authorization: Bearer $JWT" "$BASE_URL/admin/stub/acme/calls/reset"

ORDER_LOCATION=$(create_stub_order)
if [ -z "$ORDER_LOCATION" ]; then
  fail "Impossible de créer un order dans le stub (aucun Location header reçu)"
  # Ne pas exit — continuer pour afficher le bilan complet
  ORDER_LOCATION="$BASE_URL/stub/acme/order/fallback-id"
else
  ok "Order créé dans le stub : $ORDER_LOCATION"
fi

# Extraire l'orderId depuis l'URL
ORDER_ID=$(basename "$ORDER_LOCATION")
info "  orderId stub : $ORDER_ID"

step "A.3 — Insertion de l'account et de l'order en base (status=CHALLENGE_PENDING)"
# Insérer le compte ACME (nécessaire pour resumeOrder qui charge le compte)
run_sql "
INSERT INTO acme_account (server_url, account_url, key_pem, created_at)
VALUES (
  '${ACME_SERVER_URL}',
  '${BASE_URL}/stub/acme/account/stub-account-001',
  '${ENCRYPTED_ACCOUNT_KEY}',
  NOW()
);
" > /dev/null
ok "acme_account inséré"

# Insérer l'order en base avec status=CHALLENGE_PENDING
run_sql "
INSERT INTO acme_order (domain, order_url, domain_key_pem, status, created_at)
VALUES (
  '${ACME_DOMAIN}',
  '${ORDER_LOCATION}',
  '${ENCRYPTED_DOMAIN_KEY}',
  'CHALLENGE_PENDING',
  NOW()
);
" > /dev/null
ok "acme_order inséré (status=CHALLENGE_PENDING, order_url=$ORDER_LOCATION)"

step "A.4 — Reset du stub store (on ne veut pas compter les appels du setup)"
curl -s -o /dev/null -X DELETE -H "Authorization: Bearer $JWT" "$BASE_URL/admin/stub/acme/calls/reset"
ok "Stub store réinitialisé"

step "A.5 — Appel POST /admin/acme/renew-certificate (reprise depuis CHALLENGE_PENDING)"
info "  Appel en cours (peut prendre ~10s selon le polling stub)..."
RENEW_RESPONSE=$(curl -s -w "\n__HTTP_STATUS__:%{http_code}" \
  -X POST \
  -H "Authorization: Bearer $JWT" \
  "$BASE_URL/admin/acme/renew-certificate")
RENEW_BODY=$(echo "$RENEW_RESPONSE" | grep -v "__HTTP_STATUS__")
RENEW_STATUS=$(echo "$RENEW_RESPONSE" | grep "__HTTP_STATUS__" | cut -d: -f2)
info "  HTTP Status : $RENEW_STATUS"
info "  Body        : $RENEW_BODY"
if [ "$RENEW_STATUS" = "200" ]; then
  ok "Reprise exécutée (HTTP 200)"
else
  fail "Reprise échouée (HTTP $RENEW_STATUS)"
fi

step "A.6 — Vérification des appels stub enregistrés"
CALLS_JSON=$(curl -s -H "Authorization: Bearer $JWT" "$BASE_URL/admin/stub/acme/calls")
info "  Appels enregistrés :"
echo "$CALLS_JSON" | jq -r '.[] | "    \(.method) \(.endpoint)"' 2>/dev/null || echo "    (impossible de parser)"

check_call_present() {
  local label="$1" pattern="$2"
  if echo "$CALLS_JSON" | jq -r '.[].endpoint' 2>/dev/null | grep -q "$pattern"; then
    ok "  Appel présent    : $label ($pattern)"
  else
    fail "  Appel manquant   : $label ($pattern)"
  fi
}
check_call_absent() {
  local label="$1" pattern="$2"
  if echo "$CALLS_JSON" | jq -r '.[].endpoint' 2>/dev/null | grep -q "$pattern"; then
    fail "  Appel inattendu  : $label ($pattern) — ne devrait pas être appelé en reprise"
  else
    ok "  Appel absent (normal) : $label ($pattern)"
  fi
}

# En reprise CHALLENGE_PENDING : pas de new-order ni new-account
check_call_absent "new-order"           "/stub/acme/new-order"
check_call_absent "new-account"         "/stub/acme/new-account"
# Mais le reste du flux doit avoir eu lieu
check_call_present "authorization ACME" "/stub/acme/authz/"
check_call_present "challenge HTTP-01"  "/stub/acme/challenge/"
check_call_present "finalize order"     "/stub/acme/order/"
check_call_present "download cert"      "/stub/acme/certificate/"
check_call_present "deploy Cloudflare"  "/stub/cloudflare/zones/"

step "A.7 — Vérification en base : certificat DEPLOYED + acme_order nettoyé"
DB_CERT_STATUS=$(run_sql "SELECT status FROM acme_certificate WHERE domain = '${ACME_DOMAIN}' ORDER BY created_at DESC LIMIT 1;" | tr -d ' \n\t')
info "  Statut acme_certificate : $DB_CERT_STATUS"
if echo "$DB_CERT_STATUS" | grep -qi "DEPLOYED"; then
  ok "Certificat DEPLOYED en base ✓"
else
  fail "Statut inattendu : $DB_CERT_STATUS"
fi

DB_ORDER_COUNT=$(run_sql "SELECT count(*) FROM acme_order WHERE domain = '${ACME_DOMAIN}';" | tr -d ' \n\t')
info "  Lignes acme_order restantes : $DB_ORDER_COUNT"
if [ "$DB_ORDER_COUNT" = "0" ]; then
  ok "acme_order nettoyé après déploiement ✓"
else
  fail "acme_order non supprimé (count=$DB_ORDER_COUNT)"
fi

# =============================================================================
# CAS B — Reprise depuis ORDER_FINALIZING
# =============================================================================
scenario "B — Reprise depuis ORDER_FINALIZING (challenge déjà validé, crash avant téléchargement)"
info "  Le UseCase doit : poll order → télécharger cert → déployer Cloudflare"
info "  Il NE doit PAS appeler : new-order, new-account, authz, challenge"

step "B.1 — Purge des données ACME"
run_sql "DELETE FROM acme_order       WHERE domain = '${ACME_DOMAIN}';" > /dev/null
run_sql "DELETE FROM acme_account     WHERE server_url LIKE '%stub%';"  > /dev/null
run_sql "DELETE FROM acme_certificate WHERE domain = '${ACME_DOMAIN}';" > /dev/null
ok "Base purgée"

step "B.2 — Pré-création d'un order dans le stub ACME et validation immédiate du challenge"
# Créer l'order dans le stub
curl -s -o /dev/null -X DELETE -H "Authorization: Bearer $JWT" "$BASE_URL/admin/stub/acme/calls/reset"

ORDER_LOCATION_B=$(create_stub_order)
if [ -z "$ORDER_LOCATION_B" ]; then
  fail "Impossible de créer un order dans le stub"
  ORDER_LOCATION_B="$BASE_URL/stub/acme/order/fallback-id-b"
else
  ok "Order créé dans le stub : $ORDER_LOCATION_B"
fi
ORDER_ID_B=$(basename "$ORDER_LOCATION_B")

# Récupérer le token du challenge en interrogeant l'authz du stub
# On récupère le body de l'order pour trouver l'authzId
ORDER_BODY=$(curl -s -X POST \
  -H "Content-Type: application/jose+json" \
  -d '{"payload":"","protected":"","signature":""}' \
  "$ORDER_LOCATION_B")
AUTHZ_URL=$(echo "$ORDER_BODY" | jq -r '.authorizations[0]' 2>/dev/null || echo "")
info "  Authorization URL : $AUTHZ_URL"

if [ -n "$AUTHZ_URL" ] && [ "$AUTHZ_URL" != "null" ]; then
  # Récupérer le token depuis l'authz
  AUTHZ_BODY=$(curl -s -X POST \
    -H "Content-Type: application/jose+json" \
    -d '{"payload":"","protected":"","signature":""}' \
    "$AUTHZ_URL")
  CHALLENGE_TOKEN=$(echo "$AUTHZ_BODY" | jq -r '.challenges[0].token' 2>/dev/null || echo "")
  CHALLENGE_URL=$(echo "$AUTHZ_BODY"  | jq -r '.challenges[0].url'   2>/dev/null || echo "")
  info "  Challenge token : $CHALLENGE_TOKEN"
  info "  Challenge URL   : $CHALLENGE_URL"

  if [ -n "$CHALLENGE_TOKEN" ] && [ "$CHALLENGE_TOKEN" != "null" ]; then
    # Déclencher le challenge pour que le stub le marque VALID
    curl -s -o /dev/null -X POST \
      -H "Content-Type: application/jose+json" \
      -d '{"payload":"","protected":"","signature":""}' \
      "$CHALLENGE_URL"
    ok "Challenge déclenché → authz marquée VALID dans le stub"
  else
    fail "Impossible de récupérer le token du challenge depuis l'authz"
  fi
else
  fail "Impossible de récupérer l'URL de l'authorization depuis l'order"
fi

# Finaliser l'order dans le stub pour qu'il soit en status=valid avec un certId
FINALIZE_URL="${BASE_URL}/stub/acme/order/${ORDER_ID_B}/finalize"
curl -s -o /dev/null -X POST \
  -H "Content-Type: application/jose+json" \
  -d '{"payload":"","protected":"","signature":""}' \
  "$FINALIZE_URL"
ok "Order finalisé dans le stub (status=valid, certificat pré-généré)"

step "B.3 — Insertion de l'account et de l'order en base (status=ORDER_FINALIZING)"
# Générer de nouvelles keypairs EC P-256 acme4j-compatibles pour le cas B
ENCRYPTED_DOMAIN_KEY_B=$(generate_encrypted_keypair)
ENCRYPTED_ACCOUNT_KEY_B=$(generate_encrypted_keypair)

run_sql "
INSERT INTO acme_account (server_url, account_url, key_pem, created_at)
VALUES (
  '${ACME_SERVER_URL}',
  '${BASE_URL}/stub/acme/account/stub-account-001',
  '${ENCRYPTED_ACCOUNT_KEY_B}',
  NOW()
);
" > /dev/null
ok "acme_account inséré"

run_sql "
INSERT INTO acme_order (domain, order_url, domain_key_pem, status, created_at)
VALUES (
  '${ACME_DOMAIN}',
  '${ORDER_LOCATION_B}',
  '${ENCRYPTED_DOMAIN_KEY_B}',
  'ORDER_FINALIZING',
  NOW()
);
" > /dev/null
ok "acme_order inséré (status=ORDER_FINALIZING, order_url=$ORDER_LOCATION_B)"

step "B.4 — Reset du stub store"
curl -s -o /dev/null -X DELETE -H "Authorization: Bearer $JWT" "$BASE_URL/admin/stub/acme/calls/reset"
ok "Stub store réinitialisé"

step "B.5 — Appel POST /admin/acme/renew-certificate (reprise depuis ORDER_FINALIZING)"
info "  Appel en cours (peut prendre ~10s selon le polling stub)..."
RENEW_RESPONSE_B=$(curl -s -w "\n__HTTP_STATUS__:%{http_code}" \
  -X POST \
  -H "Authorization: Bearer $JWT" \
  "$BASE_URL/admin/acme/renew-certificate")
RENEW_BODY_B=$(echo "$RENEW_RESPONSE_B" | grep -v "__HTTP_STATUS__")
RENEW_STATUS_B=$(echo "$RENEW_RESPONSE_B" | grep "__HTTP_STATUS__" | cut -d: -f2)
info "  HTTP Status : $RENEW_STATUS_B"
info "  Body        : $RENEW_BODY_B"
if [ "$RENEW_STATUS_B" = "200" ]; then
  ok "Reprise exécutée (HTTP 200)"
else
  fail "Reprise échouée (HTTP $RENEW_STATUS_B)"
fi

step "B.6 — Vérification des appels stub enregistrés"
CALLS_JSON_B=$(curl -s -H "Authorization: Bearer $JWT" "$BASE_URL/admin/stub/acme/calls")
info "  Appels enregistrés :"
echo "$CALLS_JSON_B" | jq -r '.[] | "    \(.method) \(.endpoint)"' 2>/dev/null || echo "    (impossible de parser)"

check_call_b_present() {
  local label="$1" pattern="$2"
  if echo "$CALLS_JSON_B" | jq -r '.[].endpoint' 2>/dev/null | grep -q "$pattern"; then
    ok "  Appel présent    : $label ($pattern)"
  else
    fail "  Appel manquant   : $label ($pattern)"
  fi
}
check_call_b_absent() {
  local label="$1" pattern="$2"
  if echo "$CALLS_JSON_B" | jq -r '.[].endpoint' 2>/dev/null | grep -q "$pattern"; then
    fail "  Appel inattendu  : $label ($pattern) — ne devrait pas être appelé en reprise ORDER_FINALIZING"
  else
    ok "  Appel absent (normal) : $label ($pattern)"
  fi
}

# En reprise ORDER_FINALIZING : pas de new-order, new-account, authz, challenge
check_call_b_absent "new-order"           "/stub/acme/new-order"
check_call_b_absent "new-account"         "/stub/acme/new-account"
check_call_b_absent "challenge HTTP-01"   "/stub/acme/challenge/"
# Le poll de l'order (POST /stub/acme/order/{id}) est attendu
check_call_b_present "poll order"         "/stub/acme/order/"
# Téléchargement cert et déploiement Cloudflare attendus
check_call_b_present "download cert"      "/stub/acme/certificate/"
check_call_b_present "deploy Cloudflare"  "/stub/cloudflare/zones/"

step "B.7 — Vérification en base : certificat DEPLOYED + acme_order nettoyé"
DB_CERT_STATUS_B=$(run_sql "SELECT status FROM acme_certificate WHERE domain = '${ACME_DOMAIN}' ORDER BY created_at DESC LIMIT 1;" | tr -d ' \n\t')
info "  Statut acme_certificate : $DB_CERT_STATUS_B"
if echo "$DB_CERT_STATUS_B" | grep -qi "DEPLOYED"; then
  ok "Certificat DEPLOYED en base ✓"
else
  fail "Statut inattendu : $DB_CERT_STATUS_B"
fi

DB_ORDER_COUNT_B=$(run_sql "SELECT count(*) FROM acme_order WHERE domain = '${ACME_DOMAIN}';" | tr -d ' \n\t')
info "  Lignes acme_order restantes : $DB_ORDER_COUNT_B"
if [ "$DB_ORDER_COUNT_B" = "0" ]; then
  ok "acme_order nettoyé après déploiement ✓"
else
  fail "acme_order non supprimé (count=$DB_ORDER_COUNT_B)"
fi

# =============================================================================
# BILAN FINAL
# =============================================================================
echo ""
echo -e "${BOLD}═══════════════════════════════════════════════════════════${RESET}"
echo -e "${BOLD}BILAN — REPRISES SUR INCIDENT (CHALLENGE_PENDING / ORDER_FINALIZING)${RESET}"
echo -e "═══════════════════════════════════════════════════════════"
echo -e "  ${GREEN}✓ Succès  : $PASS${RESET}"
echo -e "  ${RED}✗ Échecs  : $FAIL${RESET}"
echo -e "═══════════════════════════════════════════════════════════"

if [ "$FAIL" -eq 0 ]; then
  echo -e "\n${GREEN}${BOLD}✅ TOUS LES CHECKS PASSENT — Reprises sur incident validées${RESET}\n"
  exit 0
else
  echo -e "\n${RED}${BOLD}❌ $FAIL CHECK(S) ÉCHOUÉ(S) — Voir les détails ci-dessus${RESET}\n"
  exit 1
fi
