# Plan d'implémentation — ACME V2 Renouvellement automatique de certificats

📅 Date : 25 juin 2026
🔗 ADR de référence : [2026-06-25-acme-v2-renouvellement-certificats.md](./2026-06-25-acme-v2-renouvellement-certificats.md)

---

## Suivi d'avancement

| Étape | Description | Statut |
|---|---|---|
| 1.1 | Dépendances acme4j dans `build.gradle.kts` | ⬜ TODO |
| 1.2 | Variables d'environnement dans `.env.example` | ⬜ TODO |
| 2.1 | Script SQL migration (2 tables) | ⬜ TODO |
| 2.2 | Entités JPA + repositories Spring Data | ⬜ TODO |
| 3.1 | Domain `AcmeCertificate.kt` | ⬜ TODO |
| 3.2 | Domain `AcmeAccount.kt` | ⬜ TODO |
| 4.1 | Interface `AcmeChallengeStore.kt` | ⬜ TODO |
| 4.2 | Interface `AcmeCertificateRepository.kt` | ⬜ TODO |
| 4.3 | Interface `AcmeAccountRepository.kt` | ⬜ TODO |
| 4.4 | Interface `CloudflareCertificateDeployer.kt` | ⬜ TODO |
| 5.1 | Config Spring `AcmeConfig.kt` | ⬜ TODO |
| 6.1 | Implémentation `AcmeChallengeStoreImpl.kt` | ⬜ TODO |
| 6.2 | Implémentation `AcmeCertificateRepositoryImpl.kt` | ⬜ TODO |
| 6.3 | Implémentation `AcmeAccountRepositoryImpl.kt` | ⬜ TODO |
| 6.4 | Utilitaire `AcmeCryptoHelper.kt` (chiffrement AES-256) | ⬜ TODO |
| 6.5 | Implémentation `CloudflareCertificateDeployerImpl.kt` | ⬜ TODO |
| 7.1 | Controller `AcmeController.kt` (endpoint HTTP-01) | ⬜ TODO |
| 7.2 | Modifier `WebSecurityConfig.kt` (endpoint public) | ⬜ TODO |
| 8.1 | Use case `AcmeCertificateRenewalUseCase.kt` | ⬜ TODO |
| 9.1 | Brancher dans `DailyTasksHandler.kt` | ⬜ TODO |
| 10.1 | Tests `AcmeCertificateRenewalUseCaseTest` | ⬜ TODO |
| 10.2 | Tests `AcmeChallengeStoreImplTest` | ⬜ TODO |
| 10.3 | Tests `AcmeCryptoHelperTest` | ⬜ TODO |
| 11.1 | Validation finale : tous les tests passent | ⬜ TODO |
| 11.2 | Checklist Cloudflare (hors code) | ⬜ TODO |

---

## Détail des étapes

---

### 🏗️ Étape 1 — Dépendances & configuration de base

#### 1.1 — Ajouter les dépendances acme4j dans `build.gradle.kts`

Dans le bloc `dependencies {}`, ajouter :
```kotlin
implementation("org.shredzone.acme4j:acme4j-client:3.3.0")
implementation("org.shredzone.acme4j:acme4j-utils:3.3.0")
```

- `acme4j-client` : client ACME V2 (Session, Account, Order, Challenge)
- `acme4j-utils` : utilitaires de génération de keypairs et de CSR

→ Valider : `JAVA_HOME=... ./gradlew build` doit compiler sans erreur.

#### 1.2 — Documenter les variables d'environnement dans `.env.example`

```
# ACME / Renouvellement automatique certificat TLS
ACME_ENABLED=false                               # true pour activer le renouvellement automatique
ACME_SERVER_URL=https://acme.sectigo.com/v2/DV  # URL ACME Sectigo (DV ou OV selon le type de certificat)
ACME_DOMAIN=agora.gouv.fr                        # Domaine à certifier
ACME_ACCOUNT_KEY_BASE64=                         # Keypair compte ACME encodée en base64 (généré au 1er lancement si absent)
ACME_EAB_KID=                                    # Key ID EAB fourni par le portail Sectigo Certificate Manager
ACME_EAB_HMAC_KEY=                               # HMAC key EAB fournie par Sectigo (format base64url)
ACME_ENCRYPTION_KEY=                             # Clé AES-256 pour chiffrer les clés privées en base (32 octets base64)
CLOUDFLARE_ZONE_ID=                              # ID de la zone Cloudflare (Dashboard > Overview > Zone ID)
CLOUDFLARE_API_TOKEN=                            # Token API Cloudflare (scope : Zone > SSL and Certificates > Edit)
```

---

### 🗄️ Étape 2 — Migration base de données

#### 2.1 — Script SQL de migration

Créer `data/migration_acme/create_acme_tables.sql` :

```sql
CREATE TABLE acme_certificate (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    domain      VARCHAR(255) NOT NULL,
    certificate TEXT NOT NULL,       -- PEM chain complet (cert + intermédiaires)
    private_key TEXT NOT NULL,       -- PEM clé privée domaine (chiffrée AES-256-GCM)
    expires_at  TIMESTAMP NOT NULL,  -- date d'expiration extraite du certificat X.509
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE acme_account (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    server_url  VARCHAR(500) NOT NULL,    -- URL du répertoire ACME (pour retrouver le bon compte par CA)
    account_url VARCHAR(500),             -- URL du compte ACME enregistré (null avant 1ère création)
    key_pem     TEXT NOT NULL,            -- keypair RSA/EC du compte (chiffrée AES-256-GCM)
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);
```

> `spring.jpa.hibernate.ddl-auto=update` dans `application.properties` créera les tables automatiquement via les entités JPA.
> Le script SQL sert de référence documentaire et pour les opérations manuelles éventuelles.

#### 2.2 — Entités JPA et repositories Spring Data

**`infrastructure/acme/repository/AcmeCertificateDAO.kt`** :
- `@Entity @Table(name = "acme_certificate")`
- Champs : `id: UUID`, `domain: String`, `certificate: String`, `privateKey: String`, `expiresAt: LocalDateTime`, `createdAt: LocalDateTime`

**`infrastructure/acme/repository/AcmeCertificateJpaRepository.kt`** :
- `interface AcmeCertificateJpaRepository : JpaRepository<AcmeCertificateDAO, UUID>`
- Méthode : `findFirstByDomainOrderByCreatedAtDesc(domain: String): AcmeCertificateDAO?`

**`infrastructure/acme/repository/AcmeAccountDAO.kt`** :
- `@Entity @Table(name = "acme_account")`
- Champs : `id: UUID`, `serverUrl: String`, `accountUrl: String?`, `keyPem: String`, `createdAt: LocalDateTime`

**`infrastructure/acme/repository/AcmeAccountJpaRepository.kt`** :
- `interface AcmeAccountJpaRepository : JpaRepository<AcmeAccountDAO, UUID>`
- Méthode : `findFirstByServerUrlOrderByCreatedAtDesc(serverUrl: String): AcmeAccountDAO?`

---

### 📦 Étape 3 — Domain layer

#### 3.1 — `domain/AcmeCertificate.kt`

```kotlin
data class AcmeCertificate(
    val domain: String,
    val certificatePem: String,   // PEM chain complet (cert + intermédiaires)
    val privateKeyPem: String,    // PEM clé privée (en clair en mémoire, chiffré en base)
    val expiresAt: LocalDateTime,
)
```

#### 3.2 — `domain/AcmeAccount.kt`

```kotlin
data class AcmeAccount(
    val serverUrl: String,
    val accountUrl: String?,      // null si pas encore enregistré auprès de la CA
    val keyPem: String,           // PEM keypair du compte (en clair en mémoire)
)
```

---

### 🔌 Étape 4 — Interfaces de repository (usecase layer)

> Les interfaces sont dans `usecase/acme/repository/`. Les usecases ne dépendent jamais de l'infrastructure directement.

#### 4.1 — `usecase/acme/repository/AcmeChallengeStore.kt`

```kotlin
interface AcmeChallengeStore {
    fun storeChallenge(token: String, keyAuthorization: String)
    fun getChallenge(token: String): String?   // null si token inconnu ou expiré
    fun clearChallenge(token: String)
}
```

> La `keyAuthorization` est la concaténation `<token>.<thumbprint>` produite par acme4j.
> C'est cette valeur que retourne l'endpoint public `/.well-known/acme-challenge/{token}`.

#### 4.2 — `usecase/acme/repository/AcmeCertificateRepository.kt`

```kotlin
interface AcmeCertificateRepository {
    fun loadCertificate(domain: String): AcmeCertificate?   // null si aucun certificat en base
    fun saveCertificate(certificate: AcmeCertificate)
}
```

#### 4.3 — `usecase/acme/repository/AcmeAccountRepository.kt`

```kotlin
interface AcmeAccountRepository {
    fun loadAccount(serverUrl: String): AcmeAccount?   // null si pas encore de compte créé pour cette CA
    fun saveAccount(account: AcmeAccount)
}
```

#### 4.4 — `usecase/acme/repository/CloudflareCertificateDeployer.kt`

```kotlin
interface CloudflareCertificateDeployer {
    /** Lance le PATCH Cloudflare. Lève une exception si l'API répond != 2xx */
    fun deployCertificate(certificatePem: String, privateKeyPem: String)
}
```

---

### ⚙️ Étape 5 — Config Spring (AcmeConfig)

#### 5.1 — `config/AcmeConfig.kt`

```kotlin
@Configuration
class AcmeConfig {
    @Value("\${ACME_ENABLED:false}") val enabled: Boolean = false
    @Value("\${ACME_SERVER_URL:}") val serverUrl: String = ""
    @Value("\${ACME_DOMAIN:}") val domain: String = ""
    @Value("\${ACME_EAB_KID:}") val eabKid: String = ""
    @Value("\${ACME_EAB_HMAC_KEY:}") val eabHmacKey: String = ""
    @Value("\${ACME_ENCRYPTION_KEY:}") val encryptionKey: String = ""
    @Value("\${CLOUDFLARE_ZONE_ID:}") val cloudflareZoneId: String = ""
    @Value("\${CLOUDFLARE_API_TOKEN:}") val cloudflareApiToken: String = ""
}
```

> `ACME_ENABLED=false` par défaut : aucun effet si la variable n'est pas positionnée.

---

### 🧱 Étape 6 — Implémentations infrastructure

#### 6.1 — `infrastructure/acme/repository/AcmeChallengeStoreImpl.kt`

Implémentation in-memory avec `ConcurrentHashMap<String, String>` :
- `storeChallenge(token, keyAuthorization)` → `map[token] = keyAuthorization`
- `getChallenge(token)` → `map[token]`
- `clearChallenge(token)` → `map.remove(token)`

> Pourquoi pas Redis ? Le challenge HTTP-01 ne dure que quelques secondes (polling acme4j synchrone).
> Une Map en mémoire suffit. Si plusieurs instances tournent en parallèle, migrer vers Redis est trivial
> (changer uniquement cette implémentation, sans toucher au usecase).

#### 6.2 — `infrastructure/acme/repository/AcmeCertificateRepositoryImpl.kt`

- Injection de `AcmeCertificateJpaRepository` et `AcmeCryptoHelper`
- `loadCertificate(domain)` :
  1. `jpaRepository.findFirstByDomainOrderByCreatedAtDesc(domain)` → null si absent
  2. Déchiffre `dao.privateKey` via `cryptoHelper.decrypt(...)`
  3. Mappe en `AcmeCertificate`
- `saveCertificate(cert)` :
  1. Chiffre `cert.privateKeyPem` via `cryptoHelper.encrypt(...)`
  2. Crée `AcmeCertificateDAO` et appelle `jpaRepository.save(...)`

#### 6.3 — `infrastructure/acme/repository/AcmeAccountRepositoryImpl.kt`

- Injection de `AcmeAccountJpaRepository` et `AcmeCryptoHelper`
- `loadAccount(serverUrl)` :
  1. `jpaRepository.findFirstByServerUrlOrderByCreatedAtDesc(serverUrl)` → null si absent
  2. Déchiffre `dao.keyPem` via `cryptoHelper.decrypt(...)`
  3. Mappe en `AcmeAccount`
- `saveAccount(account)` :
  1. Chiffre `account.keyPem`
  2. Cherche l'entrée existante pour `serverUrl` → update ou insert

#### 6.4 — `infrastructure/acme/repository/AcmeCryptoHelper.kt`

`@Component` partagé par les deux repos :
- Clé AES-256 dérivée de `AcmeConfig.encryptionKey` (base64 → 32 bytes)
- Algorithme : AES-256-GCM (authentifié, résistant à la falsification)
- `encrypt(plainText: String): String` → encode en base64 le résultat (IV + ciphertext + tag)
- `decrypt(cipherText: String): String` → decode base64 puis déchiffre
- Si la clé est vide ou invalide → lève une `IllegalStateException` explicite

> ⚠️ Ne jamais logger les valeurs en clair ni chiffrées des clés privées.

#### 6.5 — `infrastructure/acme/repository/CloudflareCertificateDeployerImpl.kt`

- Injection de `RestTemplate` (déjà configuré dans le projet) et des variables config Cloudflare
- Construit le body JSON :
  ```json
  {
    "certificate": "<PEM chain>",
    "private_key": "<PEM clé privée>",
    "bundle_method": "ubiquitous"
  }
  ```
- `PATCH https://api.cloudflare.com/client/v4/zones/{zoneId}/custom_certificates`
- Headers : `Authorization: Bearer {apiToken}`, `Content-Type: application/json`
- Si réponse != 2xx → lève une exception typée (ex: `CloudflareDeploymentException`) pour que Sentry l'intercepte

---

### 🎯 Étape 7 — Controller HTTP-01 challenge

#### 7.1 — `infrastructure/acme/AcmeController.kt`

```kotlin
@RestController
class AcmeController(private val challengeStore: AcmeChallengeStore) {

    @GetMapping(
        "/.well-known/acme-challenge/{token}",
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun getChallenge(@PathVariable token: String): ResponseEntity<String> {
        val keyAuthorization = challengeStore.getChallenge(token)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(keyAuthorization)
    }
}
```

- Retourne `text/plain` (requis par le protocole ACME HTTP-01)
- 200 + `<token>.<thumbprint>` si le token est connu
- 404 si le token est inconnu (challenge non actif ou déjà nettoyé)

#### 7.2 — Modifier `WebSecurityConfig.kt`

Ajouter avant `.anyRequest().authenticated()` :
```kotlin
.requestMatchers("/.well-known/acme-challenge/**")
.permitAll()
```

> Cet endpoint doit être accessible sans JWT car Sectigo y accède directement (sans token d'auth).

---

### 💡 Étape 8 — Use case principal

#### 8.1 — `usecase/acme/AcmeCertificateRenewalUseCase.kt`

```kotlin
@Service
class AcmeCertificateRenewalUseCase(
    private val acmeConfig: AcmeConfig,
    private val certificateRepository: AcmeCertificateRepository,
    private val accountRepository: AcmeAccountRepository,
    private val challengeStore: AcmeChallengeStore,
    private val cloudflareDeployer: CloudflareCertificateDeployer,
    private val clock: Clock,  // injecté via ClockConfig existant
)
```

**Méthode `renewIfNeeded()` — flux complet :**

1. **Guard clause** : si `!acmeConfig.enabled` → log info + return immédiatement
2. **Vérification expiration** :
   - `certificateRepository.loadCertificate(domain)` → si `expiresAt > now() + 30 jours` → log "pas de renouvellement nécessaire" + return
   - Si null → procéder à la première émission
3. **Chargement/création keypair compte** :
   - `accountRepository.loadAccount(serverUrl)` → si null, générer une nouvelle keypair RSA-2048 avec `KeyPairUtils.createKeyPair(2048)` d'acme4j
4. **Session acme4j** : `Session(acmeConfig.serverUrl)`
5. **Compte ACME** :
   - Si `account.accountUrl != null` → `AccountBuilder().onlyExisting().create(session)`
   - Sinon → `AccountBuilder().withKeyIdentifier(eabKid, eabHmacKey).agreeToTermsOfService().create(session)`
6. **Order** : `account.newOrder().domain(acmeConfig.domain).create()`
7. **Challenge HTTP-01** :
   - Récupérer l'`Authorization` de l'order → challenge HTTP-01
   - `challengeStore.storeChallenge(challenge.token, challenge.authorization)`
8. **Trigger validation** : `challenge.trigger()`
9. **Polling jusqu'à VALID** :
   - Boucle max 10 tentatives avec `Thread.sleep(3_000)`
   - `challenge.update()` puis vérifier `challenge.status`
   - Si `INVALID` → `challengeStore.clearChallenge(token)` + throw `AcmeChallengeFailedException`
   - Si timeout → idem + throw `AcmeChallengeTimeoutException`
10. **Nettoyage** : `challengeStore.clearChallenge(challenge.token)`
11. **Keypair domaine + CSR** :
    - `val domainKeyPair = KeyPairUtils.createKeyPair(2048)`
    - `val csrBuilder = CSRBuilder()` → `csrBuilder.addDomain(domain)` → `csrBuilder.sign(domainKeyPair)`
12. **Finalisation Order** : `order.execute(csrBuilder.encoded)` puis polling sur `order.status == VALID`
13. **Téléchargement certificat** : `order.certificate.download()` → convertir en PEM chain
14. **Extraction date d'expiration** : parser le certificat X.509 pour lire `notAfter`
15. **Persistance** : `certificateRepository.saveCertificate(AcmeCertificate(domain, certPem, domainPrivKeyPem, expiresAt))`
16. **Déploiement Cloudflare** : `cloudflareDeployer.deployCertificate(certPem, domainPrivKeyPem)`
17. **Persistance compte** : `accountRepository.saveAccount(AcmeAccount(serverUrl, account.location.toString(), accountKeyPem))`

---

### 📅 Étape 9 — Scheduler quotidien

#### 9.1 — Brancher dans `DailyTasksHandler.kt`

Injecter `AcmeCertificateRenewalUseCase` et appeler dans `handleTask()` :

```kotlin
acmeCertificateRenewalUseCase.renewIfNeeded()
```

> La guard clause `if (!acmeConfig.enabled) return` dans le usecase garantit que rien ne se passe
> si `ACME_ENABLED=false` (valeur par défaut → safe en local et dans les environnements sans cert custom).

> `DailyTasksHandler` implémente `CustomCommandHandler` → déclenchable aussi manuellement via commande
> custom Scalingo pour forcer un renouvellement ou tester en prod sans attendre 3h du matin.

---

### 🧪 Étape 10 — Tests unitaires

> Conventions : JUnit 5 + Mockito (`@ExtendWith(MockitoExtension::class)`, `@InjectMocks`, `@Mock`) + AssertJ + BDDMockito.
> Pattern **Given / When / Then** dans chaque test.
> Structure miroir : `src/test/kotlin/...` reflète exactement `src/main/kotlin/...`

#### 10.1 — `src/test/kotlin/.../usecase/acme/AcmeCertificateRenewalUseCaseTest.kt`

```
@Nested inner class RenewIfNeeded {
  `renewIfNeeded - when ACME_ENABLED is false - should do nothing and not call any dependency`
  `renewIfNeeded - when certificate expires in more than 30 days - should not renew`
  `renewIfNeeded - when certificate expires in less than 30 days - should trigger renewal`
  `renewIfNeeded - when no certificate exists - should trigger first issuance`
  `renewIfNeeded - when ACME challenge validation fails - should throw and not persist certificate`
  `renewIfNeeded - when Cloudflare deployment fails - should throw and not update stored certificate`
  `renewIfNeeded - when renewal succeeds - should persist new certificate and deploy to Cloudflare`
}
```

#### 10.2 — `src/test/kotlin/.../infrastructure/acme/AcmeChallengeStoreImplTest.kt`

```
@Nested inner class StoreAndGet {
  `storeChallenge - when token is stored - should be retrievable via getChallenge`
  `getChallenge - when token does not exist - should return null`
}
@Nested inner class Clear {
  `clearChallenge - when called - should remove token so getChallenge returns null`
  `clearChallenge - when token does not exist - should not throw`
}
```

#### 10.3 — `src/test/kotlin/.../infrastructure/acme/AcmeCryptoHelperTest.kt`

```
`encrypt then decrypt - should return original plaintext`
`encrypt - two calls with same input - should produce different ciphertexts (IV aléatoire)`
`decrypt - when ciphertext is tampered - should throw exception`
```

---

### ✅ Étape 11 — Validation finale

#### 11.1 — Lancer tous les tests du projet

```bash
JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.19/libexec/openjdk.jdk/Contents/Home ./gradlew test
```

→ exit code **0**, aucune régression, tous les nouveaux tests passent.

#### 11.2 — Checklist Cloudflare (opérations hors code — à faire en parallèle ou avant la mise en prod)

- [ ] Créer la **Cache Rule** : URI Path commence par `/.well-known/acme-challenge/` → Bypass Cache + ne pas forcer HTTPS
- [ ] Créer la **Page Rule** : `agora.gouv.fr/.well-known/acme-challenge/*` → SSL Off (désactiver la redirection HTTPS sur ce chemin)
- [ ] Ajouter les 9 variables d'environnement sur Scalingo (`ACME_*` + `CLOUDFLARE_*`)
- [ ] Récupérer les credentials EAB (KID + HMAC key) depuis le portail Sectigo Certificate Manager
- [ ] Générer une clé AES-256 pour `ACME_ENCRYPTION_KEY` (ex: `openssl rand -base64 32`)
- [ ] Tester l'endpoint challenge sur un domaine de staging avant la prod (Sectigo n'a pas d'environnement de test public)

---

## Récap des fichiers

### Fichiers à créer

| Fichier | Couche |
|---|---|
| `domain/AcmeCertificate.kt` | domain |
| `domain/AcmeAccount.kt` | domain |
| `usecase/acme/repository/AcmeChallengeStore.kt` | usecase (interface) |
| `usecase/acme/repository/AcmeCertificateRepository.kt` | usecase (interface) |
| `usecase/acme/repository/AcmeAccountRepository.kt` | usecase (interface) |
| `usecase/acme/repository/CloudflareCertificateDeployer.kt` | usecase (interface) |
| `usecase/acme/AcmeCertificateRenewalUseCase.kt` | usecase |
| `infrastructure/acme/AcmeController.kt` | infrastructure |
| `infrastructure/acme/repository/AcmeChallengeStoreImpl.kt` | infrastructure |
| `infrastructure/acme/repository/AcmeCertificateRepositoryImpl.kt` | infrastructure |
| `infrastructure/acme/repository/AcmeAccountRepositoryImpl.kt` | infrastructure |
| `infrastructure/acme/repository/CloudflareCertificateDeployerImpl.kt` | infrastructure |
| `infrastructure/acme/repository/AcmeCertificateDAO.kt` | infrastructure |
| `infrastructure/acme/repository/AcmeCertificateJpaRepository.kt` | infrastructure |
| `infrastructure/acme/repository/AcmeAccountDAO.kt` | infrastructure |
| `infrastructure/acme/repository/AcmeAccountJpaRepository.kt` | infrastructure |
| `infrastructure/acme/repository/AcmeCryptoHelper.kt` | infrastructure |
| `config/AcmeConfig.kt` | config |
| `data/migration_acme/create_acme_tables.sql` | migration SQL |
| `src/test/.../usecase/acme/AcmeCertificateRenewalUseCaseTest.kt` | tests |
| `src/test/.../infrastructure/acme/AcmeChallengeStoreImplTest.kt` | tests |
| `src/test/.../infrastructure/acme/AcmeCryptoHelperTest.kt` | tests |

### Fichiers à modifier

| Fichier | Modification |
|---|---|
| `build.gradle.kts` | Ajout dépendances acme4j |
| `.env.example` | Ajout des 9 variables ACME + Cloudflare |
| `config/WebSecurityConfig.kt` | `permitAll()` sur `/.well-known/acme-challenge/**` |
| `oninit/DailyTasksHandler.kt` | Injection + appel de `AcmeCertificateRenewalUseCase` |
