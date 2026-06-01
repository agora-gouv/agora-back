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

**Statut : ⏳ À faire**

Voir `architecture-decisions/migration-strapi-v4-vers-v5.md` pour le détail complet.

Étapes à réaliser (dans l'ordre) :
- [ ] 2.1 — Adapter `StrapiRequestBuilder.kt` (`populate=deep`→`populate=*`, `publicationState`→`status=draft`, `getByIds(List<Int>)`→`getByIds(List<String>)`)
- [ ] 2.2 — Refactoriser `StrapiDTO.kt` (supprimer `StrapiAttributes`, `StrapiData`, etc.)
- [ ] 2.3 — Adapter tous les DTOs Strapi
- [ ] 2.4 — Adapter `StrapiMediaPicture`
- [ ] 2.5 — Adapter tous les mappers
- [ ] 2.6 — Adapter les repositories (IDs entiers → `documentId` string)
- [ ] 2.7 — Supprimer le header de compatibilité ajouté en Phase 1
- [ ] 2.8 — Valider (`./gradlew test` + tests manuels sur staging)

---

## Résumé de l'état actuel

| Phase | Statut |
|-------|--------|
| Phase 1 — Header de compatibilité | ✅ Terminée |
| Phase 2 — Migration format natif v5 | ⏳ À démarrer |
