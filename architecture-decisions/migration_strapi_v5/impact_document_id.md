## Cartographie des `documentId` Strapi utilisés comme liens et stockés en base SQL

Voici tous les endroits où un `documentId` Strapi est utilisé comme **clé de liaison** et persisté en base de données PostgreSQL.

---

### 1. 🗂️ `consultation_id` → `documentId` d'une **Consultation** Strapi

**Tables SQL concernées :**
- `reponses_consultation` — colonne `consultation_id` (`TEXT`)
- `user_answered_consultation` — colonne `consultation_id` (`TEXT`)
- `consultation_results` — colonne `consultation_id` (`TEXT`)

**Comment ça arrive :**
- Le `documentId` Strapi de la consultation est lu via `ConsultationStrapiRepository`
- Il est mappé dans `ConsultationInfoMapper.toConsultationInfo()` → `ConsultationInfo.id = consultation.documentId`
- Ce `ConsultationInfo.id` est ensuite passé comme `insertParameters.consultationId` dans `InsertReponseConsultationRepositoryImpl`
- Il est stocké dans le `ReponseConsultationDTO.consultationId` (colonne `consultation_id`)

**Fichiers clés :**
- `infrastructure/consultation/repository/ConsultationInfoMapper.kt` → `id = consultation.documentId`
- `infrastructure/consultationResponse/dto/ReponseConsultationDTO.kt` → `consultationId: String`
- `infrastructure/userAnsweredConsultation/dto/UserAnsweredConsultationDTO.kt` → `consultationId: String`
- `infrastructure/consultationAggregate/dto/ConsultationResultAggregatedDTO.kt` → `consultationId: String`

---

### 2. 🗂️ `consultation_update_id` → `documentId` d'un **contenu de consultation** Strapi

**Table SQL concernée :**
- `feedbacks_consultation_update` — colonne `consultationUpdateId` (`TEXT`)

**Comment ça arrive :**
- L'URL de l'endpoint contient `{consultationUpdateId}` (ex: `POST /consultations/{id}/updates/{consultationUpdateId}/feedback`)
- Ce `consultationUpdateId` est le `documentId` Strapi d'un contenu (avant réponse, après réponse, autre contenu, analyse, réponse commanditaire), qui est comparé dans `ConsultationUpdateV2RepositoryImpl` :
  ```kotlin
  contenuAvantReponse.documentId == consultationUpdateIdOrSlug
  contenuApresReponse.documentId == consultationUpdateIdOrSlug
  // etc.
  ```
- Il est stocké directement dans `FeedbackConsultationUpdateDTO.consultationUpdateId`

**Fichiers clés :**
- `infrastructure/feedbackConsultationUpdate/FeedbackConsultationUpdateController.kt` → reçoit `consultationUpdateId` en path variable
- `infrastructure/feedbackConsultationUpdate/dto/FeedbackConsultationUpdateDTO.kt` → `consultationUpdateId: String`
- `infrastructure/consultationUpdates/repository/ConsultationUpdateV2RepositoryImpl.kt` → résolution par `documentId` ou `slug`

---

### 3. 🗂️ `thematique_id` → `documentId` d'une **Thématique** Strapi

**Table SQL concernée :**
- `qags` — colonne `thematique_id` (`TEXT`)

**Comment ça arrive :**
- Le `documentId` Strapi de la thématique est mappé dans `ThematiqueMapper.toDomain()` → `Thematique.id = dto.documentId`
- Quand un utilisateur crée une QaG, il envoie un `thematiqueId` (= `documentId` Strapi)
- Ce `thematiqueId` est stocké dans `QagDTO.thematiqueId` et sauvegardé dans `QagInfoMapper.toDto()`

**Fichiers clés :**
- `infrastructure/thematique/repository/ThematiqueMapper.kt` → `id = dto.documentId`
- `infrastructure/qag/dto/QagDTO.kt` → `thematiqueId: String`
- `infrastructure/qag/repository/QagInfoMapper.kt` → `thematiqueId = domain.thematiqueId`

---

### 4. 🗂️ `question_id` / `choice_id` → IDs de **Questions/Choix** Strapi (component IDs)

**Tables SQL concernées :**
- `reponses_consultation` — colonnes `question_id` et `choice_id` (`TEXT`)
- `consultation_results` — colonnes `question_id` et `choice_id` (`TEXT`)

**Note :** Ce ne sont pas des `documentId` au sens strict, mais des IDs de **composants Strapi** (des IDs numériques internes des items de relations). Ils sont mappés depuis `StrapiConsultationQuestion*.id` dans `QuestionMapper`.

---

### Résumé en tableau

| Table SQL | Colonne | `documentId` de quoi ? | Fichier source |
|---|---|---|---|
| `reponses_consultation` | `consultation_id` | Consultation Strapi | `ConsultationInfoMapper` |
| `user_answered_consultation` | `consultation_id` | Consultation Strapi | `ConsultationInfoMapper` |
| `consultation_results` | `consultation_id` | Consultation Strapi | `ConsultationInfoMapper` |
| `feedbacks_consultation_update` | `consultationUpdateId` | Contenu de consultation Strapi (avant/après/autre/analyse/commanditaire) | `ConsultationUpdateV2RepositoryImpl` |
| `qags` | `thematique_id` | Thématique Strapi | `ThematiqueMapper` |
| `reponses_consultation` | `question_id` / `choice_id` | Component IDs Strapi (questions/choix) | `QuestionMapper` |

---

**En résumé**, les `documentId` Strapi circulent directement comme des `String` dans tout le système — il n'y a pas de table de correspondance dédiée. Ils sont utilisés comme clé étrangère fonctionnelle dans les tables SQL sans contrainte FK formelle (puisque les données Strapi vivent dans un CMS externe).
