# Migration Strapi v4.24.2 → v5.47.0

## Contexte

Ce document décrit le plan de migration des appels API Strapi dans `agora-back` depuis la version **v4.24.2** vers la **v5**.

La migration se fait en **deux phases** pour éviter toute interruption de service :
- **Phase 1** (court terme) : activer le header de compatibilité v4 → aucune rupture, déployable immédiatement
- **Phase 2** (moyen terme) : migrer le code pour utiliser le nouveau format natif v5

---

## Breaking changes de l'API REST Strapi v5

### 1. Suppression de la couche `attributes` dans les réponses

En v4, chaque objet était enveloppé dans `{ id, attributes: { ... } }`.
En v5, les champs sont **directement à la racine** de l'objet.

**v4 :**
```json
{
  "data": [
    {
      "id": 1,
      "attributes": {
        "titre": "...",
        "thematique": {
          "data": { "id": 2, "attributes": { "label": "..." } }
        }
      }
    }
  ],
  "meta": { "pagination": { "page": 1, "pageSize": 100, "pageCount": 1, "total": 1 } }
}
```

**v5 :**
```json
{
  "data": [
    {
      "id": 1,
      "documentId": "abc123",
      "titre": "...",
      "thematique": { "id": 2, "documentId": "def456", "label": "..." }
    }
  ],
  "meta": { "pagination": { "page": 1, "pageSize": 100, "pageCount": 1, "total": 1 } }
}
```

### 2. Remplacement du paramètre `publicationState=preview`

**v4 :** `&publicationState=preview`
**v5 :** `&status=draft`

### 3. Suppression du support de `populate=deep`

**v4 :** `&populate=deep` (via plugin tiers, toléré)
**v5 :** `&populate=*` pour un niveau, ou syntaxe explicite par champ pour plusieurs niveaux

### 4. Changement des IDs : entiers → `documentId` (string)

En v5, les entrées sont identifiées par un `documentId` (string UUID-like, ex: `"abc123def456"`).
L'id numérique auto-incrémenté existe toujours mais ne doit plus être utilisé pour identifier une entrée de manière fiable.

---

## Phase 1 — Compatibilité immédiate (header `Strapi-Response-Format: v4`)

> **Objectif :** Faire fonctionner le backend avec Strapi v5 sans modifier la logique métier.
>
> Strapi v5 propose un header de rétrocompatibilité qui force le retour du format de réponse v4.

### Étape 1.1 — Modifier `CmsStrapiHttpClient.kt`

**Fichier :** `src/main/kotlin/fr/gouv/agora/config/CmsStrapiHttpClient.kt`

Ajouter le header `Strapi-Response-Format: v4` dans la méthode `getClientRequest` :

```kotlin
// AVANT
return HttpRequest.newBuilder()
    .uri(URI("$apiUrl$uriWithoutSpace"))
    .setHeader("Authorization", "Bearer $authToken")

// APRÈS
return HttpRequest.newBuilder()
    .uri(URI("$apiUrl$uriWithoutSpace"))
    .setHeader("Authorization", "Bearer $authToken")
    .setHeader("Strapi-Response-Format", "v4")
```

### Étape 1.2 — Vérifier et déployer

```bash
./gradlew test
```

Tous les tests doivent passer. Déployable sur l'environnement avec Strapi v5.

> ⚠️ **Note :** Le header `Strapi-Response-Format: v4` est une fonctionnalité de dépréciation temporaire. Il sera retiré dans une future version de Strapi v5. La Phase 2 doit être planifiée.

---

## Phase 2 — Migration complète vers le format natif v5

> **Objectif :** Supprimer le header de compatibilité et migrer tout le code pour utiliser le format de réponse natif Strapi v5.
>
> À faire **après** la Phase 1, sur une branche dédiée.

### Étape 2.1 — Adapter `StrapiRequestBuilder.kt`

**Fichier :** `src/main/kotlin/fr/gouv/agora/infrastructure/common/StrapiRequestBuilder.kt`

**Changement 1 :** `populate=deep` → `populate=*`
```kotlin
// AVANT
private var populate = "&populate=deep"

// APRÈS
private var populate = "&populate=*"
```

> ⚠️ **Attention (vérifié sur v5.47.0) :** En Strapi v5, `populate=*` ne remonte que **le premier niveau de relations**. Il ne remplace pas `populate=deep` de manière transparente pour les structures imbriquées.
>
> Par exemple, pour une consultation qui possède des relations imbriquées (`thematique`, `imageDeCouverture`, `contenuAvantReponse` lui-même avec des `sections`, etc.), `populate=*` ne suffira pas à tout remonter.
>
> Pour les endpoints complexes, il faudra utiliser une syntaxe explicite :
> ```
> &populate[thematique][populate]=*
> &populate[imageDeCouverture][populate]=*
> &populate[contenuAvantReponse][populate][sections][populate]=*
> ```
>
> **Action recommandée :** Tester chaque endpoint Strapi après migration et adapter la méthode `populate()` du `StrapiRequestBuilder` avec un populate explicite pour les collections complexes (consultations notamment). La valeur par défaut `populate=*` restera suffisante pour les collections simples (thématiques, thème hebdo, news, etc.).

**Changement 2 :** `publicationState=preview` → `status=draft`
```kotlin
// AVANT
fun withUnpublished(): StrapiRequestBuilder {
    unpublished = "&publicationState=preview"
    return this
}

// APRÈS
fun withUnpublished(): StrapiRequestBuilder {
    unpublished = "&status=draft"
    return this
}
```

**Changement 3 :** `getByIds` accepte des `String` (documentId) au lieu des `Int`
```kotlin
// AVANT
fun getByIds(ids: List<Int>): StrapiRequestBuilder {
    return filterIn("id", ids.map { it.toString() })
}

// APRÈS
fun getByIds(ids: List<String>): StrapiRequestBuilder {
    return filterIn("documentId", ids)
}
```

---

### Étape 2.2 — Refactoriser `StrapiDTO.kt`

**Fichier :** `src/main/kotlin/fr/gouv/agora/infrastructure/common/StrapiDTO.kt`

En v5, la couche `attributes` disparaît. Les données sont directement dans l'objet racine.

**Changement principal — `StrapiAttributes<T>` :**
```kotlin
// AVANT : enveloppe { id, attributes: T }
data class StrapiAttributes<T>(
    @JsonProperty(value = "attributes")
    val attributes: T,
    @JsonProperty(value = "id")
    val id: String
)

// APRÈS : les données T sont directement désérialisées (plus de wrapper)
// StrapiAttributes<T> est remplacé par T directement, avec l'id/documentId intégré dans T
// OU on garde une classe conteneur mais sans "attributes"
data class StrapiAttributes<T>(
    @JsonProperty(value = "documentId")
    val documentId: String,
    // les champs de T sont inlinés via @JsonUnwrapped ou T doit intégrer l'id
)
```

> 💡 **Conseil d'implémentation :** La façon la plus propre est de faire hériter les DTOs d'une interface `WithStrapiId` qui porte le `documentId`, et de supprimer le wrapper `StrapiAttributes<T>`.

**Changement — Relations imbriquées `StrapiData<T>`, `StrapiDataNullable<T>`, `StrapiDataList<T>` :**

Ces classes modélisent les relations Strapi v4 sous la forme `{ data: { id, attributes: T } }`.
En v5, une relation est directement `T` (avec `documentId` et les champs à plat).

```kotlin
// AVANT
data class StrapiData<T>(
    @JsonProperty("data")
    val data: StrapiAttributes<T>,
)

// APRÈS : plus de wrapper "data" ni "attributes", c'est directement T
// StrapiData<T> est remplacé par T?
// StrapiDataNullable<T> est remplacé par T?
// StrapiDataList<T> est remplacé par List<T>
```

---

### Étape 2.3 — Adapter les DTOs Strapi

Pour chaque DTO ci-dessous, remplacer les types `StrapiData<X>`, `StrapiDataNullable<X>`, `StrapiDataList<X>` par `X`, `X?`, `List<X>` directement.

#### `ThematiqueStrapiDTO.kt`
Pas de changement structurel : c'est un DTO simple sans relations imbriquées.
Ajouter `documentId: String` pour remplacer l'id numérique si nécessaire.

#### `ConcertationStrapiDTO.kt`
```kotlin
// AVANT
val thematique: StrapiData<ThematiqueStrapiDTO>
val image: StrapiDataNullable<StrapiMediaPicture>

// APRÈS
val thematique: ThematiqueStrapiDTO
val image: StrapiMediaPicture?
```
Adapter la méthode `getUrlImageCouverture()` : `image.data == null` → `image == null`, `image.data.attributes.mediaUrl()` → `image.mediaUrl()`

#### `FicheInventaireStrapiDTO.kt`
```kotlin
// AVANT
val thematique: StrapiData<ThematiqueStrapiDTO>
val illustration: StrapiData<StrapiMediaPicture>

// APRÈS
val thematique: ThematiqueStrapiDTO
val illustration: StrapiMediaPicture
```

#### `ConsultationStrapiDTO.kt`
```kotlin
// AVANT
val thematique: StrapiData<ThematiqueStrapiDTO>
val questions: List<StrapiConsultationQuestion>  // pas de changement (déjà inline)
val contenuAvantReponse: StrapiData<StrapiConsultationContenuAvantReponse>
val contenuApresReponseOuTerminee: StrapiData<StrapiConsultationContenuApresReponse>
val consultationContenuAnalyseDesReponses: StrapiDataNullable<StrapiConsultationAnalyseDesReponses>
val consultationContenuReponseDuCommanditaire: StrapiDataNullable<StrapiConsultationReponseCommanditaire>
val consultationContenuAutres: StrapiDataList<StrapiConsultationContenuAutre>
val consultationContenuAVenir: StrapiDataNullable<StrapiConsultationAVenir>
val imageDeCouverture: StrapiDataNullable<StrapiMediaPicture>
val imagePageDeContenu: StrapiDataNullable<StrapiMediaPicture>

// APRÈS
val thematique: ThematiqueStrapiDTO
val contenuAvantReponse: StrapiConsultationContenuAvantReponse
val contenuApresReponseOuTerminee: StrapiConsultationContenuApresReponse
val consultationContenuAnalyseDesReponses: StrapiConsultationAnalyseDesReponses?
val consultationContenuReponseDuCommanditaire: StrapiConsultationReponseCommanditaire?
val consultationContenuAutres: List<StrapiConsultationContenuAutre>
val consultationContenuAVenir: StrapiConsultationAVenir?
val imageDeCouverture: StrapiMediaPicture?
val imagePageDeContenu: StrapiMediaPicture?
```

Adapter les méthodes internes de `ConsultationStrapiDTO` :
- `getImageCouverture()` : `imageDeCouverture.data?.attributes?.mediaUrl()` → `imageDeCouverture?.mediaUrl()`
- `getImagePageContenu()` : idem
- `getLatestUpdateDate()` : `consultationContenuAutres.data.map { it.attributes... }` → `consultationContenuAutres.map { it... }`
- `getNextQuestionId()` : pas de changement
- `getFlammeLabel()` : `consultationContenuAnalyseDesReponses.data?.attributes?.flammeLabel` → `consultationContenuAnalyseDesReponses?.flammeLabel`
- `getLastContenuAutre()` : `consultationContenuAutres.data.filter { it.attributes... }` → `consultationContenuAutres.filter { it... }`

#### `ConsultationContenuStrapiDTO.kt`
```kotlin
// StrapiConsultationAnalyseDesReponses
// AVANT
val analysePdf: StrapiDataNullable<StrapiMediaPdf>

// APRÈS
val analysePdf: StrapiMediaPdf?
```
Adapter `getAnalysePdfUrl()` : `analysePdf.data?.attributes?.url` → `analysePdf?.url`

#### `ConsultationQuestionStrapiDTO.kt`
```kotlin
// StrapiConsultationQuestionDescription
// AVANT
val image: StrapiDataNullable<StrapiMediaPicture>

// APRÈS
val image: StrapiMediaPicture?
```
Adapter `getImageUrl()` : `image.data?.attributes?.mediaUrl()` → `image?.mediaUrl()`

#### `ThemeHebdoStrapiDTO.kt`
```kotlin
// AVANT
val photo: StrapiDataNullable<StrapiMediaPicture>

// APRÈS
val photo: StrapiMediaPicture?
```

---

### Étape 2.4 — Adapter `StrapiMediaPicture`

Le champ `formats` en v5 reste identique, pas de changement structurel attendu. À confirmer lors des tests.

---

### Étape 2.5 — Adapter les mappers

Pour chaque mapper, remplacer les accès `.data`, `.attributes` par un accès direct.

#### `ThematiqueMapper.kt`
```kotlin
// AVANT
fun toDomain(dto: StrapiAttributes<ThematiqueStrapiDTO>) = Thematique(id = dto.id, ...)
fun toDomain(strapiDTO: StrapiDTO<ThematiqueStrapiDTO>) = strapiDTO.data.map { it.attributes... }
fun toDomain(strapiDTO: StrapiData<ThematiqueStrapiDTO>) = toDomain(strapiDTO.data)

// APRÈS
fun toDomain(dto: ThematiqueStrapiDTO) = Thematique(id = dto.documentId, ...)
fun toDomain(strapiDTO: StrapiDTO<ThematiqueStrapiDTO>) = strapiDTO.data.map { ... }
// StrapiData<T> supprimé → utiliser directement ThematiqueStrapiDTO
```

#### `ConcertationMapper.kt`
```kotlin
// AVANT
concertations.data.mapNotNull { concertation ->
    concertation.attributes.thematique.data.attributes.label
    concertation.attributes.image.data?.attributes?.mediaUrl()
}

// APRÈS
concertations.data.mapNotNull { concertation ->
    concertation.thematique.label
    concertation.image?.mediaUrl()
}
```

#### `ThemeHebdoMapper.kt`
```kotlin
// AVANT
strapiDTO.data.map { item ->
    item.attributes.photo.data?.attributes?.mediaUrl()
    item.id
}

// APRÈS
strapiDTO.data.map { item ->
    item.photo?.mediaUrl()
    item.documentId
}
```

#### `ConsultationInfoMapper.kt`, `ConsultationUpdateInfoV2Mapper.kt`, `ConsultationUpdateHistoryMapper.kt`
Remplacer tous les accès `.attributes.` sur les `StrapiAttributes<ConsultationStrapiDTO>` par un accès direct.

```kotlin
// AVANT
val consultation = consultationDTO.attributes
consultationDTO.id

// APRÈS
val consultation = consultationDTO  // les données sont directement dans l'objet
consultationDTO.documentId
```

Remplacer également les accès aux relations imbriquées :
```kotlin
// AVANT
consultation.consultationContenuAutres.data.map { it.attributes.datetimePublication }
consultation.consultationContenuAnalyseDesReponses.data?.attributes?.flammeLabel

// APRÈS
consultation.consultationContenuAutres.map { it.datetimePublication }
consultation.consultationContenuAnalyseDesReponses?.flammeLabel
```

#### `FicheInventaireRepositoryImpl.kt`
```kotlin
// AVANT
private fun toFicheInventaire(fiche: StrapiAttributes<FicheInventaireStrapiDTO>): FicheInventaire {
    fiche.id
    fiche.attributes.titre
    fiche.attributes.thematique.data.attributes.label

// APRÈS
private fun toFicheInventaire(fiche: FicheInventaireStrapiDTO): FicheInventaire {
    fiche.documentId
    fiche.titre
    fiche.thematique.label
```

---

### Étape 2.6 — Adapter les repositories (IDs)

Tous les endroits où un ID Strapi est converti en entier doivent utiliser le `documentId` (string).

#### `ConsultationStrapiRepository.kt`
```kotlin
// AVANT (×4 occurrences)
val strapiConsultationId = consultationId.toIntOrNull() ?: return null
val uriBuilder = StrapiRequestBuilder("consultations").getByIds(listOf(strapiConsultationId))

// APRÈS
val uriBuilder = StrapiRequestBuilder("consultations").getByIds(listOf(consultationId))
```

#### `FicheInventaireStrapiRepository.kt`
```kotlin
// AVANT
val strapiFicheId = ficheId.toIntOrNull() ?: return null
val uriBuilder = StrapiRequestBuilder("fiche-inventaires").getByIds(listOf(strapiFicheId))

// APRÈS
val uriBuilder = StrapiRequestBuilder("fiche-inventaires").getByIds(listOf(ficheId))
```

#### `StrapiRequestBuilder.getByIds`
Déjà couvert en étape 2.1 : signature change de `List<Int>` → `List<String>` et filtre sur `documentId`.

---

### Étape 2.7 — Supprimer le header de compatibilité

**Fichier :** `src/main/kotlin/fr/gouv/agora/config/CmsStrapiHttpClient.kt`

Supprimer la ligne ajoutée en Phase 1 :
```kotlin
// SUPPRIMER
.setHeader("Strapi-Response-Format", "v4")
```

---

### Étape 2.8 — Valider

```bash
./gradlew test
```

Tous les tests doivent passer. Vérifier manuellement les endpoints clés sur l'environnement de staging.

---

## Récapitulatif des fichiers à modifier

### Phase 1 (1 fichier)
| Fichier | Modification |
|---|---|
| `config/CmsStrapiHttpClient.kt` | Ajouter header `Strapi-Response-Format: v4` |

### Phase 2 (liste complète)
| Fichier | Modification |
|---|---|
| `config/CmsStrapiHttpClient.kt` | Supprimer le header de compatibilité |
| `infrastructure/common/StrapiRequestBuilder.kt` | `populate=deep`→`populate=*`, `publicationState`→`status`, `getByIds(List<Int>)`→`getByIds(List<String>)` |
| `infrastructure/common/StrapiDTO.kt` | Supprimer `StrapiAttributes`, `StrapiData`, `StrapiDataNullable`, `StrapiDataList`, aplatir les structures |
| `infrastructure/thematique/dto/ThematiqueStrapiDTO.kt` | Ajouter `documentId` |
| `infrastructure/thematique/repository/ThematiqueMapper.kt` | Supprimer accès `.attributes` |
| `infrastructure/concertations/ConcertationStrapiDTO.kt` | Remplacer `StrapiData<T>` par `T` direct |
| `infrastructure/concertations/ConcertationMapper.kt` | Supprimer accès `.data.attributes` |
| `infrastructure/ficheInventaire/FicheInventaireStrapiDTO.kt` | Remplacer `StrapiData<T>` par `T` direct |
| `infrastructure/ficheInventaire/FicheInventaireStrapiRepository.kt` | Supprimer `toIntOrNull()` |
| `infrastructure/ficheInventaire/FicheInventaireRepositoryImpl.kt` | Supprimer accès `.attributes` |
| `infrastructure/themeHebdo/repository/ThemeHebdoStrapiDTO.kt` | `StrapiDataNullable<T>` → `T?` |
| `infrastructure/themeHebdo/repository/ThemeHebdoMapper.kt` | Supprimer accès `.attributes` |
| `infrastructure/consultation/dto/strapi/ConsultationStrapiDTO.kt` | Remplacer tous les `StrapiData<T>` |
| `infrastructure/consultation/dto/strapi/ConsultationContenuStrapiDTO.kt` | `StrapiDataNullable<T>` → `T?` |
| `infrastructure/consultation/dto/strapi/ConsultationQuestionStrapiDTO.kt` | `StrapiDataNullable<T>` → `T?` |
| `infrastructure/consultation/repository/ConsultationStrapiRepository.kt` | Supprimer `toIntOrNull()` (×4) |
| `infrastructure/consultation/repository/ConsultationInfoMapper.kt` | Supprimer accès `.attributes` |
| `infrastructure/consultationUpdates/repository/ConsultationUpdateInfoV2Mapper.kt` | Supprimer accès `.attributes`, `.data` |
| `infrastructure/consultationUpdates/repository/ConsultationUpdateHistoryMapper.kt` | Supprimer accès `.attributes`, `.data` |
| `infrastructure/question/repository/QuestionMapper.kt` | Supprimer accès `.attributes` |
| `infrastructure/question/repository/QuestionsMapper.kt` | Supprimer accès `.attributes` |
| `infrastructure/responseQag/repository/ResponseQagMapper.kt` | Supprimer accès `.attributes` |
| `infrastructure/welcomePage/repository/NewsStrapiRepository.kt` | Supprimer accès `.attributes` |
| `infrastructure/headerQag/repository/HeaderQagStrapiRepository.kt` | Supprimer accès `.attributes` |
| `infrastructure/participationCharter/repository/ParticipationCharterStrapiRepository.kt` | Supprimer accès `.attributes` |
| `infrastructure/content/repository/ContentStrapiRepository.kt` | `StrapiAttributes<T>` → `T` direct |
