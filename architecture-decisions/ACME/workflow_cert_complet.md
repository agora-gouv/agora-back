
# Schéma complet du workflow de renouvellement de certificat ACME

---

## 1. Vue d'ensemble — Déclencheurs

```
┌────────────────────────────────────────────────────────────────────┐
│  DÉCLENCHEURS                                                      │
│                                                                    │
│  A) Scheduler quotidien — DailyTasksHandler                       │
│     @Scheduled(cron = "0 0 3 * * *")  ← chaque nuit à 3h         │
│                                                                    │
│  B) Admin manuel — POST /admin/acme/renew-certificate             │
│     (force l'exécution sans attendre le scheduler)                │
│                                                                    │
│  C) Upload manuel — POST /admin/acme/upload-certificate           │
│     (injection directe d'un certificat tiers en statut TO_DEPLOY) │
└────────────────────────────┬───────────────────────────────────────┘
                             │
                             ▼
                 AcmeCertificateRenewalUseCase
                       renewIfNeeded()
```

---

## 2. Phase 0 — Guards & décision

```
renewIfNeeded()
     │
     ├─── ACME_ENABLED = false ? ──────────────────► STOP (log info)
     │
     ▼
Charger le certificat en base
certificateRepository.loadCertificate(domain)
     │
     │  [Résultat = null]
     ├──────────────────────────────────────────────► needsProvisioning = true
     │
     │  [expiresAt > now+30j  ET  status = DEPLOYED]
     ├──────────────────────────────────────────────► STOP (certificat valide, déjà déployé)
     │
     │  [expiresAt > now+30j  ET  status = TO_DEPLOY]
     ├──────────────────────────────────────────────► needsProvisioning = false
     │                                                (ré-essai du déploiement Cloudflare seulement)
     │
     │  [expiresAt ≤ now+30j]
     └──────────────────────────────────────────────► needsProvisioning = true
                                                      (renouvellement complet)
```

**États en base à cette étape :**
| Table | Colonne | Valeur |
|---|---|---|
| `acme_certificate` | `status` | `TO_DEPLOY` ou `DEPLOYED` |
| `acme_certificate` | `expires_at` | date d'expiration du certificat X.509 |

---

## 3. Phase 1 — Reprise sur incident (si needsProvisioning = true)

```
Charger l'order en cours
orderRepository.loadOrder(domain)
     │
     │  [Pas d'order en base]
     ├──────────────────────────────────────────────► startNewOrder()  (§ Phase 2)
     │
     │  [Order existant, âge > 24h]
     ├──── deleteOrder(domain) ────────────────────► startNewOrder()  (§ Phase 2)
     │     (order périmé, on repart de zéro)
     │
     │  [Order existant, âge ≤ 24h, status = CHALLENGE_PENDING]
     └──────────────────────────────────────────────► resumeOrder() depuis CHALLENGE_PENDING (§ Phase 4a)
     
     │  [Order existant, âge ≤ 24h, status = ORDER_FINALIZING]
     └──────────────────────────────────────────────► resumeOrder() depuis ORDER_FINALIZING  (§ Phase 4b)
```

**États en base à cette étape :**
| Table | Colonne | Valeur |
|---|---|---|
| `acme_order` | `status` | `CHALLENGE_PENDING` ou `ORDER_FINALIZING` |
| `acme_order` | `created_at` | horodatage de création (pour calcul de l'âge) |
| `acme_order` | `order_url` | URL ACME pour rebinder l'order |
| `acme_order` | `domain_key_pem` | clé privée domaine chiffrée AES-256-GCM |

---

## 4. Phase 2 — startNewOrder() : création d'un nouvel order ACME

```
startNewOrder(domain, serverUrl)
     │
     ├── 1. Charger/créer la keypair du compte ACME
     │       accountRepository.loadAccount(serverUrl)
     │         ├── [trouvé] → KeyPairUtils.readKeyPair(storedAccount.keyPem)
     │         └── [absent] → KeyPairUtils.createKeyPair(2048)   ← génération RSA-2048
     │
     ├── 2. Session acme4j  →  Session(serverUrl)
     │
     ├── 3. Compte ACME
     │       ├── [accountUrl connu]  → AccountBuilder().onlyExisting().create(session)
     │       └── [accountUrl absent] → AccountBuilder()
     │                                   .withKeyIdentifier(eabKid, eabHmacKey)  ← EAB Sectigo
     │                                   .agreeToTermsOfService()
     │                                   .create(session)
     │
     ├── 4. Persistance immédiate du compte  ← (avant le challenge, DEBT-02)
     │       accountRepository.saveAccount(AcmeAccount(serverUrl, accountUrl, keyPem))
     │       ┌──────────────────────────────────────────────────────┐
     │       │ acme_account :                                       │
     │       │   server_url  = ACME_SERVER_URL                      │
     │       │   account_url = URL retournée par Sectigo            │
     │       │   key_pem     = keypair chiffrée AES-256-GCM        │
     │       └──────────────────────────────────────────────────────┘
     │
     ├── 5. Créer l'Order ACME
     │       account.newOrder().domain(domain).create()
     │
     ├── 6. Récupérer le challenge HTTP-01
     │       authorization.findChallenge(Http01Challenge.TYPE)
     │
     ├── 7. Stocker le token en base
     │       challengeStore.storeChallenge(token, keyAuthorization)
     │       ┌──────────────────────────────────────────────────────┐
     │       │ acme_challenge :                                     │
     │       │   token             = token HTTP-01 Sectigo          │
     │       │   key_authorization = <token>.<thumbprint>           │
     │       └──────────────────────────────────────────────────────┘
     │
     ├── 8. Générer la keypair domaine  →  KeyPairUtils.createKeyPair(2048)
     │
     ├── 9. Persister l'Order en base  (status = CHALLENGE_PENDING)
     │       orderRepository.saveOrder(AcmeOrder(...))
     │       ┌──────────────────────────────────────────────────────┐
     │       │ acme_order :                                         │
     │       │   domain         = domaine à certifier              │
     │       │   order_url      = URL de l'order ACME              │
     │       │   domain_key_pem = clé privée domaine chiffrée      │
     │       │   status         = CHALLENGE_PENDING  ◄─────────────┤
     │       │   created_at     = now()                             │
     │       └──────────────────────────────────────────────────────┘
     │
     └──► § Phase 3 — Validation HTTP-01
```

---

## 5. Phase 3 — Validation HTTP-01 (challenge Sectigo)

```
                  challenge.trigger()
                       │
                       ▼
     ┌─────────────────────────────────────────────┐
     │ Sectigo CA                                   │
     │  GET http://agora.gouv.fr                    │
     │       /.well-known/acme-challenge/{token}    │
     └─────────────────┬───────────────────────────┘
                       │  HTTP port 80 (non chiffré)
                       ▼
     ┌─────────────────────────────────────────────┐
     │ Cloudflare                                   │
     │  Cache Rule : bypass cache                   │
     │  Page Rule  : SSL=Off sur /acme-challenge/*  │
     └─────────────────┬───────────────────────────┘
                       │
                       ▼
     ┌─────────────────────────────────────────────┐
     │ AcmeController (agora-back)                  │
     │  GET /.well-known/acme-challenge/{token}     │
     │  → challengeStore.getChallenge(token)        │
     │     [trouvé]  → 200  <token>.<thumbprint>    │
     │     [absent]  → 404                          │
     └─────────────────────────────────────────────┘

Polling côté agora-back (max 10 × 3s = 30s) :
     challenge.update() → vérifier challenge.status
          │
          ├── VALID    ──────────────────────────────► Continuer §Phase 4a
          ├── INVALID  ── challengeStore.clearChallenge(token) ──► AcmeChallengeFailedException
          └── TIMEOUT  ── challengeStore.clearChallenge(token) ──► AcmeChallengeTimeoutException

     [dans tous les cas, finally]
     challengeStore.clearChallenge(token)
     ┌──────────────────────────────────────┐
     │ acme_challenge : ligne supprimée     │
     └──────────────────────────────────────┘
```

---

## 6. Phase 4a — Finalisation de l'Order

```
Challenge VALID
     │
     ├── orderRepository.updateOrderStatus(domain, ORDER_FINALIZING)
     │   ┌──────────────────────────────────────────────────────────┐
     │   │ acme_order :                                             │
     │   │   status = ORDER_FINALIZING  ◄────────────────────────  │
     │   └──────────────────────────────────────────────────────────┘
     │
     ├── Générer le CSR
     │       CSRBuilder().addDomain(domain).sign(domainKeyPair)
     │
     ├── Finaliser l'Order
     │       order.execute(csrBuilder.encoded)
     │
     └── Polling jusqu'à VALID (max 10 × 3s)
              order.update() → vérifier order.status
                   ├── VALID   ──────────────────────────────► §Phase 4b
                   ├── INVALID ────────────────────────────► AcmeChallengeFailedException
                   └── TIMEOUT ────────────────────────────► AcmeChallengeTimeoutException
```

---

## 7. Phase 4b — Téléchargement et persistance du certificat

```
Order VALID
     │
     ├── Télécharger le certificat PEM
     │       order.certificate.certificateChain
     │       → buildString { certChain.forEach { append PEM block } }
     │
     ├── Extraire la date d'expiration
     │       x509Cert.notAfter → LocalDateTime
     │
     └── Persister le certificat (status = TO_DEPLOY)
             certificateRepository.saveCertificate(AcmeCertificate(...))
             ┌──────────────────────────────────────────────────────────┐
             │ acme_certificate :                                       │
             │   domain       = domaine certifié                       │
             │   certificate  = PEM chain chiffré                      │
             │   private_key  = clé privée domaine chiffrée AES-256    │
             │   expires_at   = date notAfter du certificat X.509      │
             │   status       = TO_DEPLOY  ◄──────────────────────     │
             │   deployed_at  = NULL                                    │
             └──────────────────────────────────────────────────────────┘
```

---

## 8. Phase 5 — Déploiement Cloudflare

```
     ├── ACME_CLOUDFLARE_INTERACTION_ENABLED = false ?
     │       └──► STOP (mode dry-run)
     │
     ├── cloudflareDeployer.deployCertificate(certPem, domainPrivKeyPem)
      │       POST https://api.cloudflare.com/client/v4/zones/{zoneId}/custom_certificates
     │       Authorization: Bearer {CLOUDFLARE_API_TOKEN}
     │       Body: { certificate, private_key, bundle_method: "ubiquitous" }
     │
     │       [réponse != 2xx] ──────────────────────────────► CloudflareDeploymentException
     │                                                          (Sentry + log error)
     │
     ├── Mise à jour du statut → DEPLOYED + deployed_at
     │       certificateRepository.markAsDeployed(domain, now())
     │       ┌──────────────────────────────────────────────────────────┐
     │       │ acme_certificate :                                       │
     │       │   status      = DEPLOYED  ◄─────────────────────────    │
     │       │   deployed_at = now()                                    │
     │       └──────────────────────────────────────────────────────────┘
     │
     └── Supprimer l'order de la base
             orderRepository.deleteOrder(domain)
             ┌──────────────────────────────────────────────────────────┐
             │ acme_order : ligne supprimée  ◄──────────────────────    │
             └──────────────────────────────────────────────────────────┘
                                         ✅ FIN — Certificat déployé
```

---

## 9. Schéma de reprise sur incident (interruptions)

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                       POINTS DE REPRISE POSSIBLE                                │
│                                                                                 │
│  Crash AVANT storeChallenge()                                                   │
│  → acme_order absent → startNewOrder() au prochain réveil   ✅                 │
│                                                                                 │
│  Crash APRÈS storeChallenge, AVANT saveOrder()                                  │
│  → acme_order absent → startNewOrder() (nouveau token généré)  ✅             │
│                                                                                 │
│  Crash APRÈS saveOrder (status=CHALLENGE_PENDING)                               │
│  → resumeOrder() depuis CHALLENGE_PENDING                                      │
│  → re-storeChallenge + re-trigger + re-poll → ORDER_FINALIZING → suite ✅      │
│                                                                                 │
│  Crash APRÈS updateOrderStatus (status=ORDER_FINALIZING)                        │
│  → resumeOrder() depuis ORDER_FINALIZING                                       │
│  → poll order.status → téléchargement cert → TO_DEPLOY → Cloudflare  ✅        │
│                                                                                 │
│  Crash APRÈS saveCertificate (status=TO_DEPLOY), AVANT deployCertificate()      │
│  → Au prochain réveil : cert valide mais status=TO_DEPLOY                      │
│  → needsProvisioning=false → skip ACME → retry Cloudflare direct  ✅           │
│                                                                                 │
│  Crash APRÈS deployCertificate(), AVANT markAsDeployed()                        │
│  → Cloudflare a le certificat, mais status=TO_DEPLOY en base                   │
│  → Au prochain réveil : retry deployCertificate() (idempotent Cloudflare)  ✅  │
│                                                                                 │
│  Order périmé (âge > 24h en base)                                              │
│  → deleteOrder() + startNewOrder() (repartir de zéro proprement)  ✅           │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 10. États en base — Récapitulatif complet

### Table `acme_certificate`
| Statut | Signification | Action au prochain réveil |
|---|---|---|
| `TO_DEPLOY` + expiresAt > now+30j | Cert valide, déploiement Cloudflare échoué ou interrompu | Retry déploiement Cloudflare uniquement (pas de contact ACME) |
| `DEPLOYED` + expiresAt > now+30j | Nominal — certificat actif et déployé | Aucune action |
| `TO_DEPLOY` ou `DEPLOYED` + expiresAt ≤ now+30j | Renouvellement nécessaire | Nouveau cycle ACME complet |
| Absent | Première émission | Nouveau cycle ACME complet |

### Table `acme_order`
| Statut | Signification | Reprise |
|---|---|---|
| `CHALLENGE_PENDING` | Challenge HTTP-01 déclenché, polling en cours | Re-trigger challenge + re-poll + finalisation |
| `ORDER_FINALIZING` | Challenge validé, CSR envoyé, polling final en cours | Re-poll order + téléchargement cert |
| Absent | Pas d'order en cours | Démarrer un nouvel order |
| Présent mais âge > 24h | Order périmé (Sectigo l'a probablement annulé) | Supprimer + repartir de zéro |

### Table `acme_account`
| Colonne | Contenu |
|---|---|
| `server_url` | URL du répertoire ACME (clé de lookup par CA) |
| `account_url` | URL du compte ACME enregistré (null = pas encore enregistré) |
| `key_pem` | Keypair RSA-2048 du compte, chiffrée AES-256-GCM |

### Table `acme_challenge`
| Durée de vie | Description |
|---|---|
| Quelques secondes | Stocké juste avant `challenge.trigger()`, supprimé dans le `finally` après le polling |
| Partagé entre instances | Toutes les instances Scalingo peuvent répondre au challenge |

---

## 11. Flux alternatif — Upload manuel (hors ACME)

```
POST /admin/acme/upload-certificate
     │
     ├── AcmeCertificateUploadUseCase.uploadCertificate(certPem, privKeyPem, expiresAt)
     │
     └── saveCertificate(status = TO_DEPLOY)
             ┌──────────────────────────────────────────────────┐
             │ acme_certificate : status = TO_DEPLOY            │
             └──────────────────────────────────────────────────┘
                      │
                      ▼
              Prochain appel renewIfNeeded()
              (scheduler 3h ou POST /admin/acme/renew-certificate)
                      │
                      ▼
              needsProvisioning = false  (cert valide mais TO_DEPLOY)
              → skip ACME → deployCertificate() → DEPLOYED   ✅
```

Ce flux permet de **bootstrapper** le premier certificat (émis manuellement depuis Sectigo) ou de **récupérer manuellement** après une panne sans avoir à toucher au code.
