# ACME — Support des SANs (Subject Alternative Names) pour les certificats multi-domaines

📅 Date : 22 septembre 2026

---

## Contexte

Le flux ACME existant (voir `2026-06-25-acme-v2-renouvellement-certificats.md`) gère aujourd'hui
un **unique domaine** (`ACME_DOMAIN`). Certains déploiements nécessitent un certificat couvrant
simultanément le domaine principal **et** des sous-domaines (ex : `agora.gouv.fr` +
`www.agora.gouv.fr`). Le standard X.509 permet cela via les **Subject Alternative Names (SANs)**.

---

## Fonctionnement du protocole ACME avec les SANs (RFC 8555)

Avec les SANs, un seul `Order` couvre plusieurs identifiants. Le serveur ACME crée alors
**une authorization distincte par domaine**, chacune nécessitant son propre challenge HTTP-01 :

```
Order  →  [identifier: agora.gouv.fr, identifier: www.agora.gouv.fr]
           ├── authorization #1 → challenge HTTP-01 token_A  (pour agora.gouv.fr)
           └── authorization #2 → challenge HTTP-01 token_B  (pour www.agora.gouv.fr)
```

La CA (Sectigo) va valider chaque domaine indépendamment en appelant :
- `http://agora.gouv.fr/.well-known/acme-challenge/{token_A}`
- `http://www.agora.gouv.fr/.well-known/acme-challenge/{token_B}`

Le certificat émis contient alors un seul `CN` (domaine principal) et les SANs listés dans
l'extension `Subject Alternative Name`.

---

## Analyse d'impact par composant

### Ce qui **ne change pas**

| Composant | Raison |
|---|---|
| `AcmeController` (`GET /.well-known/acme-challenge/{token}`) | L'endpoint répond sur le path sans notion de domaine source. Tous les domaines/sous-domaines pointent vers le même agora-back via Cloudflare. Il est déjà domain-agnostique. |
| `WebSecurityConfig` | `/.well-known/acme-challenge/**` est déjà `permitAll()`. |
| `AcmeChallengeStore` / `AcmeChallengeStoreImpl` | Stockage clé/valeur `token → keyAuthorization` en base. Supporte N tokens naturellement, sans modification. |
| `AcmeCertificate` / tables BDD | Le certificat final est un unique PEM multi-SAN. Rien ne change côté stockage. |
| `CloudflareCertificateDeployer` | Upload d'un seul PEM, inchangé. |
| Cloudflare Cache Rules | Déjà configurées sur `/.well-known/acme-challenge/*`, valables pour tous les sous-domaines de la zone. |

### Ce qui **doit évoluer**

| Composant | Nature de la modification |
|---|---|
| `AcmeConfig` | Nouvelle variable `ACME_SANS` + helper `allDomains` |
| `AcmeCertificateRenewalUseCase.startNewOrder` | Order multi-domaines, itération sur N challenges, CSR multi-SANs |
| `AcmeCertificateRenewalUseCase.resumeOrder` | Itération sur toutes les authorizations (cas `CHALLENGE_PENDING`) |
| `AcmeStubController` (stub local) | Gérer N authorizations lors du `newOrder` |
| Tests unitaires | Adapter les cas existants + ajouter des cas multi-SANs |
| `.env.example` | Documenter `ACME_SANS` |

---

## Plan d'implémentation détaillé

### 1. `AcmeConfig` — nouvelle variable `ACME_SANS`

```kotlin
@Value("\${ACME_SANS:}")
val sans: String = ""  // ex: "www.agora.gouv.fr,api.agora.gouv.fr" (liste séparée par virgule, vide = aucun SAN)

// Helper calculé : domaine principal + SANs
val allDomains: List<String>
    get() = listOf(domain) + sans.split(",").map(String::trim).filter(String::isNotBlank)
```

Variable d'environnement à ajouter sur Scalingo :

| Variable | Description | Exemple |
|---|---|---|
| `ACME_SANS` | Sous-domaines à inclure dans le certificat (SANs), séparés par des virgules. Vide = certificat mono-domaine. | `www.agora.gouv.fr,api.agora.gouv.fr` |

### 2. `AcmeCertificateRenewalUseCase.startNewOrder` — order multi-domaines

**2a. Création de l'Order avec tous les domaines :**

```kotlin
// Avant
val order = account.newOrder().domain(domain).create()

// Après
val orderBuilder = account.newOrder()
acmeConfig.allDomains.forEach { orderBuilder.domain(it) }
val order = orderBuilder.create()
```

**2b. Récupération et stockage de tous les challenges :**

```kotlin
// Avant : un seul challenge
val authorization = order.authorizations.first()
val challenge = authorization.findChallenge(Http01Challenge.TYPE) as Http01Challenge
challengeStore.storeChallenge(challenge.token, challenge.authorization)
challenge.trigger()
pollUntilChallengeValid(challenge, domain)
challengeStore.clearChallenge(challenge.token)

// Après : un challenge par domaine
val challenges = order.authorizations.mapIndexed { index, authorization ->
    val domainForAuthz = acmeConfig.allDomains.getOrElse(index) { authorization.identifier.domain }
    val challenge = authorization.findChallenge(Http01Challenge.TYPE) as Http01Challenge?
        ?: throw IllegalStateException("No HTTP-01 challenge available for ${authorization.identifier}")
    challengeStore.storeChallenge(challenge.token, challenge.authorization)
    Pair(domainForAuthz, challenge)
}

// Trigger de tous les challenges
challenges.forEach { (_, challenge) -> challenge.trigger() }

// Polling de chaque challenge
challenges.forEach { (domainForChallenge, challenge) ->
    pollUntilChallengeValid(challenge, domainForChallenge)
}

// Nettoyage
challenges.forEach { (_, challenge) -> challengeStore.clearChallenge(challenge.token) }
```

**2c. CSR avec tous les domaines (CN + SANs) :**

```kotlin
// Avant
csrBuilder.addDomain(domain)

// Après
acmeConfig.allDomains.forEach { csrBuilder.addDomain(it) }
// Note : acme4j utilise le premier domaine comme CN automatiquement
```

### 3. `AcmeCertificateRenewalUseCase.resumeOrder` — reprise multi-authorizations

Dans le cas `CHALLENGE_PENDING`, la reprise doit itérer sur **toutes** les authorizations et
re-stocker / re-trigger chaque challenge non encore validé :

```kotlin
// Avant : order.authorizations.first()
// Après : order.authorizations — itérer sur toutes
val allChallenges = order.authorizations.map { authorization ->
    val challenge = authorization.findChallenge(Http01Challenge.TYPE) as Http01Challenge?
        ?: throw IllegalStateException("No HTTP-01 challenge available for ${authorization.identifier} during resume")
    // Re-stockage défensif
    challengeStore.storeChallenge(challenge.token, challenge.authorization)
    challenge
}
```

Règles de reprise :
- Si **toutes** les authorizations sont `VALID` → procéder à la finalisation
- Si **au moins une** est `INVALID` → erreur non récupérable
- Sinon → re-trigger les challenges non `PROCESSING`, retourner `null` (retry au prochain run)

### 4. `AcmeStubController` — stub local multi-authorizations

Le stub doit créer autant d'authorizations que de domaines dans la config :

```kotlin
// newOrder : créer une authz par domaine dans acmeConfig.allDomains
val authzList = acmeConfig.allDomains.map { domain ->
    val authzId = UUID.randomUUID().toString().take(8)
    val token = UUID.randomUUID().toString().replace("-", "").take(22)
    orderStore.authzs[authzId] = StubAuthz(id = authzId, orderId = orderId, challengeToken = token)
    orderStore.challengeToOrder[token] = orderId
    authzId
}

// Response body
"authorizations" to authzList.map { "$base/authz/$it" }
```

### 5. Tests unitaires à adapter

**Tests existants à mettre à jour :**
- Tous les tests `startNewOrder` qui mockent `order.authorizations` → remplacer `.first()` par une liste

**Nouveaux cas à ajouter :**

```
- renewIfNeeded - when ACME_SANS is configured with one SAN - should create order with two domains
- renewIfNeeded - when ACME_SANS is configured with one SAN - should store two challenge tokens
- renewIfNeeded - when ACME_SANS is configured - should include all domains in CSR
- renewIfNeeded - when one SAN challenge fails - should clear all stored tokens and throw
- renewIfNeeded - when ACME_SANS is empty - should behave as single domain (backward compatibility)
- resumeOrder - when challenge pending with SANs - should restore all challenge tokens
- resumeOrder - when all authorizations VALID with SANs - should finalize order with multi-domain CSR
```

---

## Gestion des erreurs partielles

Si **N challenges** sont en cours et que l'un échoue :

1. Les tokens **déjà stockés** dans `AcmeChallengeStore` doivent être nettoyés pour les challenges
   ayant réussi. Le challenge échoué lève une exception qui remonte dans le `try/catch` de
   `startNewOrder`.
2. L'`AcmeOrder` reste en base avec statut `CHALLENGE_PENDING` pour reprise possible.
3. Si l'erreur est `INVALID` (non récupérable), nettoyer **tous** les tokens et supprimer l'order.

---

## Compatibilité ascendante

- Si `ACME_SANS` est vide (ou non défini), `allDomains` retourne `[domain]` — comportement
  identique à l'actuel.
- Aucune migration de base de données n'est nécessaire.
- Les certificats existants déjà en base ne sont pas impactés.

---

## Prérequis Cloudflare

Les configurations Cloudflare existantes (Cache Rule + Page Rule) s'appliquent sur le **path**
`/.well-known/acme-challenge/*` indépendamment du domaine/sous-domaine — elles couvrent donc
automatiquement les SANs sans modification supplémentaire, **à condition que tous les SANs soient
dans la même zone Cloudflare** (ce qui est le cas pour un domaine principal et ses sous-domaines).

---

## Contraintes protocole HTTP-01 et SANs

> ⚠️ Le challenge **HTTP-01 ne supporte pas les wildcards** (ex : `*.agora.gouv.fr`).
> Chaque sous-domaine doit être listé explicitement dans `ACME_SANS`.
> Si des wildcards sont nécessaires, le challenge **DNS-01** est requis (hors scope de cette évolution).

---

## Conséquences

- 1 nouvelle variable d'environnement : `ACME_SANS`
- Aucune migration de base de données
- Aucune modification de `AcmeController`, `WebSecurityConfig`, `AcmeChallengeStore`
- Le stub local (`ACME_STUB_MODE=true`) doit être adapté pour les tests multi-domaines
- Tests unitaires à enrichir avec des cas multi-SANs
