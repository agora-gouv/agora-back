#!/usr/bin/env bash
# =============================================================================
# test-acme-stub.sh — Test E2E du flux ACME en mode bouchon local
# =============================================================================
#
# Prérequis :
#   - L'application doit être démarrée avec .env.acme_test comme source d'env
#   - node, jq, curl, psql disponibles dans le PATH
#
# Usage :
#   ./scripts/test-acme-stub.sh
#
# Résultat :
#   - Affiche le détail de chaque étape avec logs colorés
#   - Exit 0 si tous les checks passent, exit 1 sinon
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
step()  { echo -e "\n${CYAN}${BOLD}[STEP]${RESET} $*"; }
info()  { echo -e "${WHITE}[INFO]${RESET} $*"; }
ok()    { echo -e "${GREEN}[OK]${RESET}   $*"; PASS=$((PASS + 1)); }
fail()  { echo -e "${RED}[FAIL]${RESET} $*"; FAIL=$((FAIL + 1)); }
warn()  { echo -e "${YELLOW}[WARN]${RESET} $*"; }

# ─── Localisation du projet ───────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
ENV_FILE="$PROJECT_ROOT/.env.acme_test"

echo -e "${BOLD}"
echo "╔══════════════════════════════════════════════════════════╗"
echo "║        TEST E2E — ACME STUB MODE (bouchon local)         ║"
echo "╚══════════════════════════════════════════════════════════╝"
echo -e "${RESET}"
info "Répertoire projet : $PROJECT_ROOT"
info "Fichier de config : $ENV_FILE"

# =============================================================================
# ÉTAPE 0 — Vérification des prérequis
# =============================================================================
step "0/9 — Vérification des prérequis (node, jq, curl, psql)"

check_cmd() {
  if command -v "$1" &>/dev/null; then
    ok "  '$1' trouvé : $(command -v "$1")"
  else
    fail "  '$1' introuvable — installer avant de relancer"
  fi
}

check_cmd node
check_cmd jq
check_cmd curl

# psql : cherche aussi dans les emplacements Homebrew / Postgres.app
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
  fail "  'psql' introuvable — vérifier l'installation de PostgreSQL"
fi

if [ "$FAIL" -gt 0 ]; then
  echo -e "\n${RED}${BOLD}Prérequis manquants — arrêt.${RESET}"
  exit 1
fi

# =============================================================================
# ÉTAPE 1 — Chargement de la configuration .env.acme_test
# =============================================================================
step "1/9 — Chargement de la configuration depuis .env.acme_test"

if [ ! -f "$ENV_FILE" ]; then
  fail "Fichier '$ENV_FILE' introuvable."
  echo -e "${RED}  → Créer le fichier .env.acme_test à la racine du projet.${RESET}"
  exit 1
fi
ok "Fichier .env.acme_test trouvé"

# Charger les variables ligne par ligne — compatible bash 3.2 (macOS), sans fichier temp
# ni process substitution.
while IFS= read -r line || [ -n "$line" ]; do
  # Ignorer commentaires et lignes vides
  [[ "$line" =~ ^[[:space:]]*# ]] && continue
  [[ -z "${line//[[:space:]]/}" ]] && continue
  # Supprimer le commentaire inline éventuel (après un espace + #)
  clean="${line%%  #*}"
  clean="${clean%% #*}"
  clean="${clean%%	#*}"   # tab + #
  # Ne garder que les lignes de la forme NOM=valeur (noms en majuscules + tirets bas)
  [[ "$clean" =~ ^[A-Z_]+= ]] || continue
  # Exporter la variable (export supporte NOM= pour valeur vide)
  export "$clean" 2>/dev/null || true
done < "$ENV_FILE"

info "  DATABASE_URL        = ${DATABASE_URL:-<non défini>}"
info "  ACME_ENABLED        = ${ACME_ENABLED:-<non défini>}"
info "  ACME_STUB_MODE      = ${ACME_STUB_MODE:-<non défini>}"
info "  ACME_SERVER_URL     = ${ACME_SERVER_URL:-<non défini>}"
info "  CLOUDFLARE_BASE_URL = ${CLOUDFLARE_BASE_URL:-<non défini>}"
info "  ACME_DOMAIN         = ${ACME_DOMAIN:-<non défini>}"

# Validation des variables critiques (eval pour compatibilité bash 3.2 / zsh)
for var in DATABASE_URL JWT_SECRET ACME_SERVER_URL CLOUDFLARE_BASE_URL ACME_DOMAIN; do
  val=$(eval "echo \"\${${var}:-}\"")
  if [ -z "$val" ]; then
    fail "Variable $var non définie dans .env.acme_test"
  fi
done

# =============================================================================
# ÉTAPE 2 — Vérification que le serveur est up
# =============================================================================
step "2/9 — Vérification que le serveur est démarré sur localhost:8080"

BASE_URL="http://localhost:${PORT:-8080}"
STUB_DIRECTORY_URL="$BASE_URL/stub/acme/directory"

info "  Test de connectivité : GET $STUB_DIRECTORY_URL"
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$STUB_DIRECTORY_URL" 2>/dev/null || echo "000")

if [ "$HTTP_STATUS" = "200" ]; then
  ok "Serveur disponible (HTTP $HTTP_STATUS)"
else
  fail "Serveur indisponible (HTTP $HTTP_STATUS)"
  echo -e "${RED}  → Lancer l'application avec .env.acme_test avant de relancer ce script.${RESET}"
  echo -e "${WHITE}  Exemple de démarrage :${RESET}"
  echo -e "    ${YELLOW}source .env.acme_test && ./gradlew bootRun${RESET}"
  exit 1
fi

# Vérifier aussi que le stub mode est bien actif (le directory doit contenir newNonce stub)
DIRECTORY_BODY=$(curl -s "$STUB_DIRECTORY_URL")
if echo "$DIRECTORY_BODY" | jq -e '.newNonce' &>/dev/null; then
  NONCE_URL=$(echo "$DIRECTORY_BODY" | jq -r '.newNonce')
  info "  Stub directory OK — newNonce : $NONCE_URL"
  if echo "$NONCE_URL" | grep -q "localhost"; then
    ok "Stub mode confirmé (newNonce pointe vers localhost)"
  else
    warn "newNonce ne pointe pas vers localhost ($NONCE_URL) — le serveur est-il lancé avec ACME_STUB_MODE=true ?"
  fi
else
  fail "Réponse du directory inattendue : $DIRECTORY_BODY"
fi

# =============================================================================
# ÉTAPE 3 — Purge des données ACME de test en base
# =============================================================================
step "3/9 — Purge des données ACME de test en base (domain='${ACME_DOMAIN}')"

info "  Connexion : $DATABASE_URL"

# Extraire les paramètres psql depuis DATABASE_URL (format postgresql://user:pass@host:port/db)
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

info "  DELETE acme_order   WHERE domain = '${ACME_DOMAIN}'"
DEL_ORDER=$(run_sql "DELETE FROM acme_order WHERE domain = '${ACME_DOMAIN}';")
info "  → $DEL_ORDER"

info "  DELETE acme_account WHERE server_url LIKE '%stub%'"
DEL_ACCOUNT=$(run_sql "DELETE FROM acme_account WHERE server_url LIKE '%stub%';")
info "  → $DEL_ACCOUNT"

info "  DELETE acme_certificate WHERE domain = '${ACME_DOMAIN}'"
DEL_CERT=$(run_sql "DELETE FROM acme_certificate WHERE domain = '${ACME_DOMAIN}';")
info "  → $DEL_CERT"

ok "Purge DB terminée"

# =============================================================================
# ÉTAPE 4 — Récupération du userId admin en base
# =============================================================================
step "4/9 — Récupération d'un userId admin (authorization_level=1337)"

# authorization_level=1337 = ADMIN_AUTHORIZATION_LEVEL (cf. AuthorizationLevel.kt)
ADMIN_ID=$(run_sql "SELECT id FROM agora_users WHERE authorization_level = 1337 AND is_banned = 0 LIMIT 1;" | tr -d ' \n\t')

if [ -z "$ADMIN_ID" ]; then
  fail "Aucun utilisateur admin (authorization_level=1337) trouvé en base"
  exit 1
fi

ok "Admin trouvé : $ADMIN_ID"

# =============================================================================
# ÉTAPE 5 — Génération du JWT admin (HS512)
# =============================================================================
step "5/9 — Génération du JWT admin (HS512)"

JWT=$(node -e "
const crypto = require('crypto');
const secret = '${JWT_SECRET}';
const key = Buffer.from(secret, 'base64');
function b64u(buf) {
  return buf.toString('base64').replace(/\+/g,'-').replace(/\//g,'_').replace(/=/g,'');
}
const h = b64u(Buffer.from(JSON.stringify({alg:'HS512',typ:'JWT'})));
const now = Math.floor(Date.now()/1000);
const p = b64u(Buffer.from(JSON.stringify({sub:'${ADMIN_ID}',iat:now,exp:now+3600})));
const s = b64u(crypto.createHmac('sha512',key).update(h+'.'+p).digest());
console.log(h+'.'+p+'.'+s);
")

if [ -z "$JWT" ]; then
  fail "Impossible de générer le JWT"
  exit 1
fi

JWT_PREVIEW="${JWT:0:40}..."
ok "JWT généré : $JWT_PREVIEW"

# =============================================================================
# ÉTAPE 6 — Déclenchement du renouvellement ACME
# =============================================================================
step "6/9 — Déclenchement du renouvellement : POST /admin/acme/renew-certificate"

info "  Appel en cours (peut prendre ~10s selon le polling stub)..."

RENEW_RESPONSE=$(curl -s -w "\n__HTTP_STATUS__:%{http_code}" \
  -X POST \
  -H "Authorization: Bearer $JWT" \
  "$BASE_URL/admin/acme/renew-certificate" 2>&1)

RENEW_BODY=$(echo "$RENEW_RESPONSE" | grep -v "__HTTP_STATUS__")
RENEW_STATUS=$(echo "$RENEW_RESPONSE" | grep "__HTTP_STATUS__" | cut -d: -f2)

info "  HTTP Status : $RENEW_STATUS"
info "  Body        : $RENEW_BODY"

if [ "$RENEW_STATUS" = "200" ]; then
  ok "Renouvellement exécuté avec succès (HTTP 200)"
else
  fail "Renouvellement échoué (HTTP $RENEW_STATUS) — body: $RENEW_BODY"
fi

# =============================================================================
# ÉTAPE 7 — Vérification des appels stub ACME
# =============================================================================
step "7/9 — Vérification des appels enregistrés : GET /admin/stub/acme/calls"

CALLS_JSON=$(curl -s \
  -H "Authorization: Bearer $JWT" \
  "$BASE_URL/admin/stub/acme/calls")

info "  Appels enregistrés :"
echo "$CALLS_JSON" | jq -r '.[] | "    \(.method) \(.endpoint)"' 2>/dev/null || echo "    (impossible de parser la réponse)"

# Vérifier la présence des endpoints clés du flux ACME
check_call() {
  local label="$1"
  local pattern="$2"
  if echo "$CALLS_JSON" | jq -r '.[].endpoint' 2>/dev/null | grep -q "$pattern"; then
    ok "  Appel présent : $label ($pattern)"
  else
    fail "  Appel manquant : $label ($pattern)"
  fi
}

check_call "directory"              "/stub/acme/directory"
check_call "new-account"            "/stub/acme/new-account"
check_call "new-order"              "/stub/acme/new-order"
check_call "authorization ACME"     "/stub/acme/authz/"
check_call "challenge HTTP-01"      "/stub/acme/challenge/"
check_call "finalize order"         "/stub/acme/order/.*/finalize"
check_call "download certificate"   "/stub/acme/certificate/"
check_call "deploy Cloudflare"      "/stub/cloudflare/zones/"

# =============================================================================
# ÉTAPE 8 — Vérification du dernier déploiement Cloudflare stub
# =============================================================================
step "8/9 — Vérification du dernier déploiement : GET /admin/stub/acme/last-deploy"

DEPLOY_JSON=$(curl -s \
  -H "Authorization: Bearer $JWT" \
  "$BASE_URL/admin/stub/acme/last-deploy")

info "  Réponse last-deploy :"
echo "$DEPLOY_JSON" | jq . 2>/dev/null || echo "  $DEPLOY_JSON"

CERT_PREVIEW=$(echo "$DEPLOY_JSON" | jq -r '.lastDeployedCertPreview // empty' 2>/dev/null)
DEPLOYED_AT=$(echo "$DEPLOY_JSON"  | jq -r '.lastDeployedAt // empty'          2>/dev/null)

if [ -n "$CERT_PREVIEW" ] && [ "$CERT_PREVIEW" != "null" ]; then
  ok "  Certificat déployé (preview: ${CERT_PREVIEW:0:30}...)"
else
  fail "  lastDeployedCertPreview est null — le déploiement Cloudflare stub n'a pas eu lieu"
fi

if [ -n "$DEPLOYED_AT" ] && [ "$DEPLOYED_AT" != "null" ]; then
  ok "  Date de déploiement présente : $DEPLOYED_AT"
else
  fail "  lastDeployedAt est null"
fi

# =============================================================================
# ÉTAPE 9 — Vérification en base : certificat persisté avec statut DEPLOYED
# =============================================================================
step "9/9 — Vérification en base : certificat persisté pour '${ACME_DOMAIN}'"

DB_CERT=$(run_sql "SELECT status, expires_at FROM acme_certificate WHERE domain = '${ACME_DOMAIN}' ORDER BY created_at DESC LIMIT 1;")

info "  Ligne acme_certificate : $DB_CERT"

if echo "$DB_CERT" | grep -qi "DEPLOYED"; then
  ok "  Certificat en base avec statut DEPLOYED"
else
  fail "  Certificat introuvable ou statut != DEPLOYED (trouvé : $DB_CERT)"
fi

DB_ORDER_COUNT=$(run_sql "SELECT count(*) FROM acme_order WHERE domain = '${ACME_DOMAIN}';" | tr -d ' \n\t')
info "  Lignes acme_order restantes : $DB_ORDER_COUNT"
if [ "$DB_ORDER_COUNT" = "0" ]; then
  ok "  acme_order nettoyé en base après déploiement (count=0)"
else
  fail "  acme_order non supprimé après déploiement (count=$DB_ORDER_COUNT)"
fi

# =============================================================================
# BILAN FINAL
# =============================================================================
echo ""
echo -e "${BOLD}═══════════════════════════════════════════════════════════${RESET}"
echo -e "${BOLD}BILAN DU TEST ACME STUB${RESET}"
echo -e "═══════════════════════════════════════════════════════════"
echo -e "  ${GREEN}✓ Succès  : $PASS${RESET}"
echo -e "  ${RED}✗ Échecs  : $FAIL${RESET}"
echo -e "═══════════════════════════════════════════════════════════"

if [ "$FAIL" -eq 0 ]; then
  echo -e "\n${GREEN}${BOLD}✅ TOUS LES CHECKS PASSENT — Flux ACME stub validé end-to-end${RESET}\n"
  exit 0
else
  echo -e "\n${RED}${BOLD}❌ $FAIL CHECK(S) ÉCHOUÉ(S) — Voir les détails ci-dessus${RESET}\n"
  exit 1
fi
