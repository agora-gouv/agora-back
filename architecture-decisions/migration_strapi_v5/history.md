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
