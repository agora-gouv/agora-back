# ACME V2 — Renouvellement automatique de certificats TLS

📅 Date : 25 juin 2026

---

## Contexte

L'application **agora-back** est déployée sur **Scalingo** et exposée publiquement via **Cloudflare** (proxy total). L'architecture TLS est la suivante :

```
Utilisateur ──(TLS Sectigo, cert public)──> Cloudflare ──(TLS Scalingo auto)──> agora-back (Scalingo)
```

- **Segment public** (Utilisateur → Cloudflare) : certificat custom géré par Sectigo, exposé par Cloudflare. C'est ce certificat que ce chantier vise à renouveler automatiquement.
- **Segment interne** (Cloudflare → Scalingo) : certificat TLS technique généré automatiquement par la plateforme Scalingo, hors périmètre de ce chantier.

L'objectif est de mettre en place le **protocole ACME V2** (RFC 8555) pour automatiser le renouvellement du certificat public directement depuis l'application Spring Boot, en utilisant :

- **acme4j** comme client Java ACME V2
- Le challenge **HTTP-01** (validation via fichier accessible sur le domaine)
- **Sectigo** comme Certificate Authority (CA)
- L'**API Cloudflare** pour déployer le certificat renouvelé

---

## Options envisagées 💡

### Option A — Renouvellement manuel ou via portail Sectigo

Renouveler le certificat manuellement depuis l'interface Sectigo, puis l'uploader sur Cloudflare via le Dashboard.

✅ Avantages : aucun code à écrire

🚫 Inconvénients : processus manuel, risque d'oubli → expiration du certificat public, pas de traçabilité applicative

---

### Option B — Intégration ACME V2 dans l'application (retenue ✅)

Implémenter le protocole ACME V2 directement dans agora-back via la librairie **acme4j**, avec un challenge HTTP-01, un scheduler Spring pour déclencher le renouvellement automatiquement, et un push final vers l'API Cloudflare.

✅ Avantages :
- Renouvellement 100% automatique, sans intervention humaine
- Contrôle total du cycle de vie du certificat avec logs applicatifs
- Compatible avec toute CA ACME V2 (Sectigo, Let's Encrypt, ZeroSSL, CA interne DINUM, etc.)
- Renouvellement proactif configurable (ex : 30 jours avant expiration)
- Alertes possibles via Sentry en cas d'échec

🚫 Inconvénients : développement et maintenance à charge de l'équipe, dépendance à l'API Cloudflare

---

## Décision 🏆

**Option B** — Intégration ACME V2 dans l'application via `acme4j` + challenge `HTTP-01` + déploiement via API Cloudflare.

---

## Prérequis Cloudflare

Avant toute mise en œuvre, deux configurations Cloudflare sont nécessaires :

### 1. Cache Rule — Bypass sur le chemin ACME

Sectigo valide le challenge en HTTP (port 80). Cloudflare peut cacher ou transformer cette réponse, ce qui ferait échouer la validation. Il faut créer une **Cache Rule** pour bypasser le cache et transmettre directement la requête à l'origin :

```
Si : URI Path commence par /.well-known/acme-challenge/
Action : Bypass Cache + ne pas forcer HTTPS
```

### 2. "Always Use HTTPS" — Exception sur le chemin ACME

Sectigo accède au challenge en HTTP non chiffré. Si Cloudflare est configuré en "Always Use HTTPS", la requête sera redirigée vers HTTPS avant d'atteindre l'origin, ce qui invalide le challenge. Créer une **Page Rule** d'exception :

```
URL : domain.fr/.well-known/acme-challenge/*
Action : SSL → Off (désactiver la redirection HTTPS sur ce chemin uniquement)
```

---

## Architecture technique

### Dépendances à ajouter (`build.gradle.kts`)

```kotlin
implementation("org.shredzone.acme4j:acme4j-client:3.3.0")
implementation("org.shredzone.acme4j:acme4j-utils:3.3.0")
```

---

### Structure en couches (conventions du projet)

```
domain/acme/
  └── AcmeCertificate.kt              ← data class : domaine, PEM cert, date d'expiration
  └── AcmeChallenge.kt                ← data class : token, contenu du fichier de challenge

usecase/acme/
  └── AcmeCertificateRenewalUseCase.kt  ← @Service : orchestre le flux ACME + push Cloudflare
  └── repository/
      └── AcmeCertificateRepository.kt  ← interface : stockage/chargement du certificat et keypair
      └── AcmeChallengeStore.kt         ← interface : stockage temporaire du token/challenge
      └── CloudflareCertificateDeployer.kt ← interface : déploiement du certificat sur Cloudflare

infrastructure/acme/
  └── AcmeController.kt               ← @RestController : expose /.well-known/acme-challenge/{token}
  └── repository/
      └── AcmeCertificateRepositoryImpl.kt    ← implémentation : PostgreSQL
      └── AcmeChallengeStoreImpl.kt           ← implémentation : stockage Redis ou Map en mémoire
      └── CloudflareCertificateDeployerImpl.kt ← implémentation : appel API Cloudflare

config/
  └── AcmeConfig.kt                   ← @Configuration : bean Session acme4j, variables d'environnement
```

---

### Flux de renouvellement ACME V2 (HTTP-01)

```
┌─────────────────────────────────────────────────────────────────┐
│  Scheduler (DailyTasksHandler)                                  │
│  → vérifie si le certificat expire dans moins de 30 jours       │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│  AcmeCertificateRenewalUseCase                                  │
│                                                                 │
│  1. Charger / créer la keypair du compte ACME                   │
│  2. Se connecter à Sectigo (Session acme4j → ACME_SERVER_URL)   │
│  3. Créer ou récupérer le compte ACME (avec EAB Sectigo)        │
│  4. Créer un Order pour le domaine cible (ACME_DOMAIN)          │
│  5. Récupérer l'Authorization + le challenge HTTP-01            │
│  6. Stocker le token dans AcmeChallengeStore                    │
│  7. Déclencher la validation côté Sectigo                       │
│  8. Polling jusqu'à VALID (avec timeout)                        │
│  9. Nettoyer le token du AcmeChallengeStore                     │
│  10. Générer une keypair domaine + CSR                          │
│  11. Finaliser l'Order avec le CSR                              │
│  12. Télécharger le certificat signé                            │
│  13. Persister le certificat via AcmeCertificateRepository      │
│  14. Déployer le certificat sur Cloudflare via API              │
│      → PATCH /zones/{zone_id}/custom_certificates               │
└─────────────────────────────────────────────────────────────────┘
                           │
         HTTP GET (port 80) │  ← Sectigo valide le challenge
         /.well-known/      │
         acme-challenge     │
         /{token}           │
                           ▼
              Cloudflare (Cache Rule bypass)
                           │
                           ▼
              AcmeController (agora-back)
              → lit le token dans AcmeChallengeStore
              → retourne <token>.<thumbprint> (text/plain)
                           │
                           ▼
              Sectigo confirme la validation du domaine
```

---

### Endpoint HTTP-01 challenge

L'endpoint suivant doit être **public** (sans JWT) :

```
GET /.well-known/acme-challenge/{token}
Content-Type: text/plain
Réponse : <token>.<thumbprint-clé-publique-compte>
```

**Modification de `WebSecurityConfig.kt`** :

```kotlin
.requestMatchers("/.well-known/acme-challenge/**")
.permitAll()
```

---

### External Account Binding (EAB) — Spécifique Sectigo

Contrairement à Let's Encrypt, **Sectigo exige un External Account Binding** pour créer un compte ACME. Les credentials EAB sont obtenus depuis le portail Sectigo Certificate Manager.

Implémentation dans `acme4j` :

```kotlin
val account = AccountBuilder()
    .withKeyIdentifier(eabKid, eabHmacKey)  // EAB Sectigo obligatoire
    .agreeToTermsOfService()
    .create(session)
```

---

### Variables d'environnement

| Variable | Description | Exemple |
|---|---|---|
| `ACME_SERVER_URL` | URL du répertoire ACME Sectigo | `https://acme.sectigo.com/v2/DV` |
| `ACME_DOMAIN` | Domaine à certifier | `agora.gouv.fr` |
| `ACME_ACCOUNT_KEY_BASE64` | Keypair du compte ACME encodée en base64 | `LS0tLS1CRUdJTi...` |
| `ACME_EAB_KID` | Key ID EAB fourni par Sectigo | `abc123` |
| `ACME_EAB_HMAC_KEY` | HMAC key EAB fournie par Sectigo (base64url) | `xyz789...` |
| `ACME_ENABLED` | Active/désactive le renouvellement automatique | `true` / `false` |
| `CLOUDFLARE_ZONE_ID` | ID de la zone Cloudflare du domaine | `abcdef1234567890` |
| `CLOUDFLARE_API_TOKEN` | Token API Cloudflare (scope : `Zone > SSL and Certificates > Edit`) | `Bearer xxxxxxxx` |

> **URL ACME Sectigo selon le type de certificat :**
> - DV (Domain Validated) : `https://acme.sectigo.com/v2/DV`
> - OV (Organization Validated) : `https://acme.sectigo.com/v2/OV`

---

### Déploiement du certificat sur Cloudflare (API)

Une fois le certificat émis par Sectigo, il est uploadé sur Cloudflare via l'API REST :

```http
PATCH https://api.cloudflare.com/client/v4/zones/{CLOUDFLARE_ZONE_ID}/custom_certificates
Authorization: Bearer {CLOUDFLARE_API_TOKEN}
Content-Type: application/json

{
  "certificate": "<PEM certificate chain>",
  "private_key": "<PEM private key>",
  "bundle_method": "ubiquitous"
}
```

> ⚠️ La clé privée est transmise à Cloudflare lors de l'upload. S'assurer que le token API a un scope **minimal** (uniquement `SSL and Certificates > Edit` sur la zone concernée).

---

### Stockage du certificat et des clés

#### Problématique Scalingo

Le filesystem de Scalingo **n'est pas persistant** entre les déploiements : les fichiers écrits sur disque sont perdus à chaque nouveau déploiement ou restart. Il faut donc externaliser le stockage.

#### Solution retenue : PostgreSQL

Stocker le certificat courant et la keypair du compte ACME dans la base PostgreSQL existante (sert d'état de référence pour savoir quand renouveler) :

```sql
CREATE TABLE acme_certificate (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    domain      VARCHAR(255) NOT NULL,
    certificate TEXT NOT NULL,          -- PEM chain
    private_key TEXT NOT NULL,          -- PEM clé privée domaine (chiffrée)
    expires_at  TIMESTAMP NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE acme_account (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    server_url  VARCHAR(500) NOT NULL,
    account_url VARCHAR(500),           -- URL du compte ACME une fois enregistré
    key_pem     TEXT NOT NULL,          -- keypair du compte (chiffrée)
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);
```

> ⚠️ Les clés privées stockées en base doivent être **chiffrées** (ex : AES-256 avec une clé dérivée d'une variable d'environnement secrète).

---

### Planification du renouvellement

Intégration dans le `DailyTasksHandler` existant :

```kotlin
// Vérification quotidienne, renouvellement si expiration < 30 jours
@Scheduled(cron = "0 0 3 * * *")  // chaque nuit à 3h
fun checkAndRenewCertificate() {
    acmeCertificateRenewalUseCase.renewIfNeeded()
}
```

---

### Tests unitaires

Conformément aux conventions du projet (JUnit 5 + Mockito + AssertJ + BDDMockito) :

#### `AcmeCertificateRenewalUseCaseTest`

```
- renewIfNeeded - when certificate expires in more than 30 days - should not renew
- renewIfNeeded - when certificate expires in less than 30 days - should trigger renewal
- renewIfNeeded - when no certificate exists - should trigger first issuance
- renewIfNeeded - when ACME challenge validation fails - should throw exception and not persist
- renewIfNeeded - when Cloudflare deployment fails - should throw exception and not update stored certificate
- renewIfNeeded - when renewal succeeds - should persist new certificate and deploy to Cloudflare
```

#### `AcmeChallengeStoreImplTest` (si logique non triviale)

```
- storeChallenge - when token is stored - should be retrievable
- getChallenge - when token does not exist - should return null
- clearChallenge - when called - should remove token from store
```

---

## Considérations de sécurité

1. **Clés privées** : ne jamais logger les clés, les stocker chiffrées en base et les transmettre uniquement via HTTPS (API Cloudflare)
2. **Credentials EAB Sectigo** : traiter `ACME_EAB_HMAC_KEY` comme un secret — rotation possible si compromis depuis le portail Sectigo
3. **Token API Cloudflare** : scope minimal (`Zone > SSL and Certificates > Edit` uniquement), rotation régulière recommandée
4. **Wildcard** : le challenge HTTP-01 **ne supporte pas** les certificats wildcard (ex : `*.agora.gouv.fr`). Si nécessaire, passer au challenge **DNS-01**
5. **Endpoint challenge** : le chemin `/.well-known/acme-challenge/**` est public par nature (requis par le protocole) mais ne retourne que le token attendu — pas de risque de fuite d'information

---

## Risques et points d'attention

| Risque | Mitigation |
|---|---|
| Certificat public expiré si le renouvellement échoue silencieusement | Alertes Sentry sur exception + monitoring externe de la date d'expiration du certificat Cloudflare |
| Cache Rule Cloudflare manquante → validation HTTP-01 échoue | Documenter et tester la Cache Rule en environnement de staging avant mise en production |
| Cloudflare redirige HTTP → HTTPS sur le chemin ACME | Page Rule d'exception sur `/.well-known/acme-challenge/*` |
| Race condition si plusieurs instances tournent en parallèle | Verrou distribué via Redis ou table `acme_renewal_lock` en base |
| Perte de la keypair du compte ACME | Backup régulier de la variable `ACME_ACCOUNT_KEY_BASE64` |
| Expiration ou révocation du token API Cloudflare | Monitoring + rotation du token, alertes Sentry sur erreur 401/403 Cloudflare |
| Credentials EAB Sectigo compromis | Rotation immédiate depuis le portail Sectigo Certificate Manager |
| Sectigo n'a pas d'environnement de staging public | Utiliser un domaine de test dédié pour les premiers essais |

---

## Conséquences

- Ajout des dépendances `acme4j-client` et `acme4j-utils` dans `build.gradle.kts`
- Création de 2 nouvelles tables PostgreSQL (`acme_certificate`, `acme_account`)
- Ajout de 8 variables d'environnement sur Scalingo
- Endpoint `/.well-known/acme-challenge/**` déclaré public dans `WebSecurityConfig`
- 2 configurations Cloudflare : Cache Rule bypass + Page Rule HTTP exception sur le chemin ACME
- Scheduler quotidien de vérification/renouvellement
- Tests unitaires obligatoires pour le usecase et le challenge store
