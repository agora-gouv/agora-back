# Journal de migration Strapi v4 → v5

> Ce fichier retrace toutes les actions réalisées dans le cadre de la migration Strapi v4.24.2 → v5.47.0.
> Document de référence : `architecture-decisions/migration-strapi-v4-vers-v5.md`

---

## Phase 1 — Compatibilité immédiate (header `Strapi-Response-Format: v4`)

**Objectif :** Faire fonctionner le backend avec Strapi v5 sans modifier la logique métier, via le header de rétrocompatibilité.

### ✅ Étape 1.1 — Header `Strapi-Response-Format: v4` dans `CmsStrapiHttpClient.kt`

- **Date :** 2026-06-01
- **Statut :** ✅ **Déjà en place** (header présent avant le début de la session de migration)
- **Fichier modifié :** `src/main/kotlin/fr/gouv/agora/config/CmsStrapiHttpClient.kt`
- **Commit de référence :** état initial du dépôt au moment de la vérification (`b8820b1d8062713e3aa3081326fde37f12c113d8`)

**Code actuel (ligne 61-64) :**
```kotlin
return HttpRequest.newBuilder()
    .uri(URI("$apiUrl$uriWithoutSpace"))
    .setHeader("Authorization", "Bearer $authToken")
    .setHeader("Strapi-Response-Format", "v4")  // ← header de compatibilité
```

### ✅ Étape 1.2 — Validation par les tests

- **Date :** 2026-06-01
- **Commande :** `JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.19/libexec/openjdk.jdk/Contents/Home ./gradlew test`
- **Résultat :** ✅ `BUILD SUCCESSFUL` — tous les tests passent (exit code 0)

---

## Harnais de tests E2E Strapi

**Objectif :** Poser un filet de sécurité sur les endpoints Strapi pour détecter toute régression lors de la Phase 2.

### ✅ Mise en place du harnais (2026-06-01)

**Fichiers créés :**

| Fichier | Rôle |
|---|---|
| `src/test/kotlin/fr/gouv/agora/e2e/StrapiE2ETestBase.kt` | Classe de base : setup `CmsStrapiHttpClient`, skip automatique si credentials absents |
| `src/test/kotlin/fr/gouv/agora/e2e/content/ContentStrapiE2ETest.kt` | 10 tests E2E — tag `Content` |
| `src/test/kotlin/fr/gouv/agora/e2e/ficheInventaire/FicheInventaireStrapiE2ETest.kt` | 2 tests E2E — tag `FicheInventaire` |

**Modification `build.gradle.kts` :**
```kotlin
tasks.withType<Test> {
    useJUnitPlatform {
        if (!project.hasProperty("e2e")) {
            excludeTags("e2e")  // E2E exclus par défaut
        }
    }
}
```

**Commandes :**
- `./gradlew test` → tests unitaires uniquement (E2E exclus)
- `./gradlew test -Pe2e` → tests E2E uniquement (unitaires exclus), relancés à chaque fois sans cache (nécessite `CMS_AUTH_TOKEN` + `CMS_API_URL`)

**Résultats de validation (2026-06-01) :**
- `./gradlew test` → ✅ `BUILD SUCCESSFUL` (E2E bien exclus)
- `./gradlew test -Pe2e` → ✅ `BUILD SUCCESSFUL` — **12/12 tests E2E passés** (0 skipped, 0 failures)
  - `ContentStrapiE2ETest` : 10/10 ✅
  - `FicheInventaireStrapiE2ETest` : 2/2 ✅

**Endpoints Strapi couverts :**
- `page-questions-au-gouvernement`
- `page-reponse-aux-questions-au-gouvernement`
- `page-poser-ma-question`
- `site-vitrine-accueil`
- `site-vitrine-conditions-generales-d-utilisation`
- `site-vitrine-consultation`
- `site-vitrine-declaration-d-accessibilite`
- `site-vitrine-mentions-legale`
- `site-vitrine-politique-de-confidentialite`
- `site-vitrine-question-au-gouvernement`
- `fiche-inventaires` (liste)
- `fiche-inventaires/{id}` (détail)

> ⚠️ **Note :** Ces tests constituent la baseline v4. Ils doivent être relancés après chaque étape de la Phase 2 pour s'assurer qu'il n'y a pas de régression.

---

## Phase 2 — Migration complète vers le format natif v5

**Statut : 🔄 En cours**

Voir `architecture-decisions/migration-strapi-v4-vers-v5.md` pour le détail complet.

Étapes à réaliser (dans l'ordre) :
- [x] 2.1 — Adapter `StrapiRequestBuilder.kt` (`populate=deep`→`populate=*`, `publicationState`→`status=draft`, `getByIds(List<Int>)`→`getByIds(List<String>)`)
- [x] 2.2 — Refactoriser `StrapiDTO.kt` (supprimer `StrapiAttributes`, `StrapiData`, etc.)
- [x] 2.3 — Adapter tous les DTOs Strapi
- [x] 2.4 — Adapter `StrapiMediaPicture`
- [ ] 2.5 — Adapter tous les mappers
- [ ] 2.6 — Adapter les repositories (IDs entiers → `documentId` string)
- [ ] 2.7 — Supprimer le header de compatibilité ajouté en Phase 1
- [ ] 2.8 — Valider (`./gradlew test` + tests manuels sur staging)

---

### ✅ Étape 2.1 — Adapter `StrapiRequestBuilder.kt`

- **Date :** 2026-06-01
- **Statut :** ✅ Terminée
- **Tests :** ✅ `BUILD SUCCESSFUL` — tous les tests passent (exit code 0)

**Fichiers modifiés :**

| Fichier | Modification |
|---|---|
| `src/main/kotlin/fr/gouv/agora/infrastructure/common/StrapiRequestBuilder.kt` | 3 changements (voir ci-dessous) |
| `src/main/kotlin/fr/gouv/agora/infrastructure/consultation/repository/ConsultationStrapiRepository.kt` | Suppression des `toIntOrNull()` sur les 4 appels à `getByIds` |
| `src/main/kotlin/fr/gouv/agora/infrastructure/ficheInventaire/FicheInventaireStrapiRepository.kt` | Suppression du `toIntOrNull()` sur l'appel à `getByIds` |
| `src/test/kotlin/fr/gouv/agora/infrastructure/common/StrapiRequestBuilderTest.kt` | Mise à jour des assertions + 2 nouveaux tests |

**Changements dans `StrapiRequestBuilder.kt` :**

1. `populate=deep` → `populate=*`
```kotlin
// AVANT
private var populate = "&populate=deep"
// APRÈS
private var populate = "&populate=*"
```

2. `publicationState=preview` → `status=draft`
```kotlin
// AVANT
unpublished = "&publicationState=preview"
// APRÈS
unpublished = "&status=draft"
```

3. `getByIds(List<Int>)` → `getByIds(List<String>)` filtrant sur `documentId`
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

### ✅ Étape 2.2 — Refactoriser `StrapiDTO.kt`

- **Date :** 2026-06-01
- **Statut :** ✅ Terminée
- **Tests :** ✅ `BUILD SUCCESSFUL` — tous les tests passent (exit code 0)

**Objectif :** Supprimer les classes `StrapiData`, `StrapiAttributes` et `StrapiSingleData` qui modélisaient le format v4 (`data.attributes`), et les remplacer par `StrapiDTO<T>` et `StrapiSingleDTO<T>` qui contiennent directement les données à plat (format v5).

**Fichiers modifiés :**

| Fichier | Modification |
|---|---|
| `src/main/kotlin/fr/gouv/agora/infrastructure/common/StrapiDTO.kt` | Suppression de `StrapiData`, `StrapiAttributes`, `StrapiSingleData` ; `StrapiDTO<T>` contient désormais `data: List<T>` directement |
| `src/main/kotlin/fr/gouv/agora/infrastructure/common/StrapiMediaPicture.kt` | Suppression du wrapper `data`/`attributes`, `formats` devient non-nullable avec `@JsonIgnoreProperties` |
| `src/main/kotlin/fr/gouv/agora/infrastructure/common/StrapiMediaVideo.kt` | Idem : suppression du wrapper v4 |
| `src/main/kotlin/fr/gouv/agora/infrastructure/common/StrapiMediaPdf.kt` | Idem |
| Tous les DTOs Strapi (consultation, thematique, thème hebdo, header QAG, news, etc.) | Suppression des `.attributes` dans les champs relationnels ; les relations sont désormais des types directs |
| `src/main/kotlin/fr/gouv/agora/infrastructure/consultation/dto/strapi/ConsultationContenuStrapiDTO.kt` | `StrapiConsultationAVenir` passe à `@JsonIgnoreProperties(ignoreUnknown = true)` |
| `src/test/kotlin/fr/gouv/agora/infrastructure/themeHebdo/repository/ThemeHebdoMapperTest.kt` | JSON de test mis à jour (format v5 à plat) |
| `src/test/kotlin/fr/gouv/agora/infrastructure/question/repository/QuestionsMapperTest.kt` | JSON de test mis à jour (format v5 à plat) |
| `src/test/kotlin/fr/gouv/agora/infrastructure/consultation/dto/ConsultationStrapiDTOTest.kt` | JSON de test mis à jour (format v5 à plat, suppression `data`/`attributes` dans images et vidéos) |

**Avant (format v4) :**
```json
{
  "data": [
    {
      "id": 1,
      "attributes": {
        "label": "Démocratie",
        "pictogramme": "🗳",
        "image": {
          "data": {
            "id": 3,
            "attributes": { "url": "...", "formats": { "medium": { "url": "..." } } }
          }
        }
      }
    }
  ]
}
```

**Après (format v5 natif) :**
```json
{
  "data": [
    {
      "documentId": "thema-9",
      "label": "Démocratie",
      "pictogramme": "🗳",
      "image": {
        "url": "...",
        "formats": { "medium": { "url": "..." } }
      }
    }
  ]
}
```

---

### ✅ Étape 2.3 — Adapter tous les DTOs Strapi

- **Date :** 2026-06-01
- **Statut :** ✅ Terminée (réalisée dans le cadre de l'étape 2.2)
- **Tests :** ✅ `BUILD SUCCESSFUL` — tous les tests passent (exit code 0)

**Constat :** Tous les DTOs Strapi avaient déjà été migrés vers le format v5 natif lors de l'étape 2.2. Vérification complète effectuée sur chacun des DTOs listés dans le plan de migration.

**DTOs vérifiés :**

| DTO | État |
|---|---|
| `ThematiqueStrapiDTO.kt` | ✅ `documentId: String` présent, aucune relation imbriquée |
| `ConcertationStrapiDTO.kt` | ✅ `thematique: ThematiqueStrapiDTO`, `image: StrapiMediaPicture?`, `getUrlImageCouverture()` correcte |
| `FicheInventaireStrapiDTO.kt` | ✅ `thematique: ThematiqueStrapiDTO`, `illustration: StrapiMediaPicture` |
| `ThemeHebdoStrapiDTO.kt` | ✅ `photo: StrapiMediaPicture?` direct (sans wrapper) |
| `ConsultationStrapiDTO.kt` | ✅ Tous les champs directs : `thematique`, `contenuAvantReponse`, `consultationContenuAutres: List<...>`, `imageDeCouverture: StrapiMediaPicture?`, méthodes corrigées |
| `ConsultationContenuStrapiDTO.kt` | ✅ `analysePdf: StrapiMediaPdf?` direct, `getAnalysePdfUrl()` correcte |
| `ConsultationQuestionStrapiDTO.kt` | ✅ `image: StrapiMediaPicture?` direct, `getImageUrl()` correcte |

**Tests de désérialisation validant le format v5 :**
- `ConsultationStrapiDTOTest` — JSON v5 à plat (sans `data`/`attributes`)
- `ThemeHebdoMapperTest` — `StrapiMediaPicture` directement dans le DTO
- `QuestionsMapperTest` — `image: StrapiMediaPicture?` directement dans `StrapiConsultationQuestionDescription`

---

### ✅ Étape 2.4 — Adapter `StrapiMediaPicture`

- **Date :** 2026-06-01
- **Statut :** ✅ Terminée
- **Tests :** ✅ `BUILD SUCCESSFUL` — tous les tests passent (exit code 0)

**Constat :** `StrapiMediaPicture` était déjà au format v5 natif (champs à plat, sans wrapper `data`/`attributes`). L'étape consistait à sécuriser la désérialisation pour les cas où le champ `formats` est absent ou `null` en v5 (images uploadées sans traitement de redimensionnement).

**Fichiers modifiés :**

| Fichier | Modification |
|---|---|
| `src/main/kotlin/fr/gouv/agora/infrastructure/common/StrapiDTO.kt` | Ajout de `@JsonIgnoreProperties(ignoreUnknown = true)` sur `StrapiMediaPicture` + `formats` rendu nullable (`StrapiMediaPictureFormats?`) + adaptation de `mediaUrl()` (`formats?.medium?.url`) |
| `src/test/kotlin/fr/gouv/agora/infrastructure/common/StrapiMediaPictureTest.kt` | Nouveau fichier : 7 tests unitaires couvrant `mediaUrl()` (formats présent/absent/null) et la désérialisation JSON (formats présent, absent, null, vide) |

**Changement dans `StrapiDTO.kt` :**

```kotlin
// AVANT
data class StrapiMediaPicture(
    @JsonProperty("formats")
    val formats: StrapiMediaPictureFormats,
    @JsonProperty("url")
    val pictureUrlNotOptimized: String,
) {
    fun mediaUrl(): String {
        return formats.medium?.url ?: pictureUrlNotOptimized
    }
}

// APRÈS
@JsonIgnoreProperties(ignoreUnknown = true)
data class StrapiMediaPicture(
    @JsonProperty("formats")
    val formats: StrapiMediaPictureFormats?,
    @JsonProperty("url")
    val pictureUrlNotOptimized: String,
) {
    fun mediaUrl(): String {
        return formats?.medium?.url ?: pictureUrlNotOptimized
    }
}
```

---

## Résumé de l'état actuel

| Phase | Statut |
|-------|--------|
| Phase 1 — Header de compatibilité | ✅ Terminée |
| Phase 2 — Migration format natif v5 | 🔄 En cours (étapes 2.1 ✅, 2.2 ✅, 2.3 ✅, 2.4 ✅) |
