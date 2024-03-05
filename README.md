# Agora

## Table of Content
- [1. Pre-requisites](#1-pre-requisites)
- [2. Launch back-end server](#2-launch-back-end-server)
- [3. Endpoints](#3-endpoints)
- [4. Add some data](#4-add-some-data)

## 1. Pre-requisites
- Java 17
- PostGreSQL server with a `agora` database and `backend` user
- Redis server
- Define those environment variables:
```
export PORT="8080"
export DATABASE_URL="postgresql://backend:@localhost:5432/agora"
export DATABASE_MAX_POOL_SIZE="1"
export DATABASE_SHOW_SQL="true"
export REDIS_URL="redis://default:@localhost:6379"
export JWT_SECRET='LZp/cGPJ1mkWst0HEfKVITrnHwYbyak7KWpQysxke8hgrg+CBGGmAqV3RXhOz2+6Fm4aforbrdPDy8Q4VCFr1g=='
export LOGIN_TOKEN_ENCODE_SECRET='E2M9xqpEuqZRkYWNmgIjbw=='
export LOGIN_TOKEN_DECODE_SECRET='E2M9xqpEuqZRkYWNmgIjbw=='
export LOGIN_TOKEN_ENCODE_TRANSFORMATION='AES/ECB/PKCS5Padding'
export LOGIN_TOKEN_DECODE_TRANSFORMATION='AES/ECB/PKCS5Padding'
export LOGIN_TOKEN_ENCODE_ALGORITHM='AES'
export LOGIN_TOKEN_DECODE_ALGORITHM='AES'
export REMOTE_ADDRESS_SECRET_KEY_ALGORITHM='PBKDF2WithHmacSHA512'
export REMOTE_ADDRESS_HASH_ITERATIONS=1000
export REMOTE_ADDRESS_HASH_KEY_LENGTH=256
export REMOTE_ADDRESS_HASH_SALT="a9a4ba4b6130c11686faaf6984bc0be5"
export CONTACT_EMAIL="contact@agora.gouv.fr"
export IS_ASK_QUESTION_ENABLED="true"
export IS_SIGNUP_ENABLED="true"
export IS_LOGIN_ENABLED="true"
export IS_QAG_ARCHIVE_ENABLED="false"
export IS_QAG_SELECT_ENABLED="false"
export IS_FEEDBACK_ON_RESPONSE_QAG_ENABLED="true"
export ERROR_TEXT_QAG_DISABLED="QaG disabled ¯\_(ツ)_/¯"
export REQUIRED_IOS_VERSION="12"
export REQUIRED_ANDROID_VERSION="18"
export REQUIRED_WEB_VERSION="1"
export DEFAULT_EXPLANATION_IMAGE_URL=""
export ALLOWED_ORIGINS="*"
```

## 2. Launch back-end server
- Simply open the project using IntelliJ or your favorite IDE
- Run `fr.gouv.agora.AgoraBackApplicationKt`

## 3. Endpoints
You can use a Postman collection provided in `/docs` directory to have a list of this back-end's endpoints.

To use any endpoints, please make sure beforehand to call `/signup` the first time you use it, otherwise call `/login`. This will generate and automatically store a JWT that will last **24 hours**.

## 4. Add some data

Use these SQL instructions to have some data on your newly installed Agora Server.
You can also create your owns, but it might be a bit tedious as there are no back-office at all, for now.

### a. Consultations
<details>
    <summary>Consultation 1 - Click to expand SQL data</summary>

```
INSERT INTO consultations(id, title, start_date, end_date, cover_url, details_cover_url, question_count, estimated_time, participant_count_goal, description, tips_description, thematique_id) VALUES (
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    'Le petit déjeuner, le rituel sacré de la matinée.',
    '2023-01-01',
    '2025-01-01',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Breakfast2.jpg/1024px-Breakfast2.jpg',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Breakfast2.jpg/1024px-Breakfast2.jpg',
    '6 questions',
    '5 minutes',
    100,
    '<body>Ceci est une consultation fictive, elle contiendra des questions pour tester les différents types de questions possibles</body>',
    '<body>🗣 Consultation proposée par le <b>Capitaine P’tit Dej’</b><br/><br/>🎯<b> Objectif</b> : améliorer la qualité des petits déjeuners apportés à l’équipe AGORA 🤤.</body>',
    '5cdb4732-0153-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates(id, step, description, consultation_id) VALUES (
    '3c2d744f-7143-442b-aaa4-53bfe5f5f451',
    1,
    '<body>👉 Capitaine P’tit Dej’ recevra les résultats et en tirera les enseignements pour la suite et les actions qui découleront de vos réponses !</body>',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, popup_description, ordre, type, description, max_choices, consultation_id) VALUES (
    '32a4b9b4-e9aa-4930-9983-31f477abc279',
    'Êtes vous plutôt ?',
    'Il s’agit d’une estimation globale, si vous prenez un oeuf (salé) mais un verre de jus d’orange (sucré) vous pouvez considérer qu’il s’agit d’un petit déjeuner salé... Après c’est vous qui voyez.',
    1,
    'unique',
    null,
    null,
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '351fb2dd-8737-49ef-b7a9-7057dbd5920c',
    'Sucré',
    1,
    '32a4b9b4-e9aa-4930-9983-31f477abc279',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '35ccc5f3-02d0-4704-a02d-972b777bcb63',
    'Salé',
    2,
    '32a4b9b4-e9aa-4930-9983-31f477abc279',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '865b3e7b-1fe8-4c55-b056-3042ca34bc36',
    'Les deux',
    3,
    '32a4b9b4-e9aa-4930-9983-31f477abc279',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '3e811511-5aba-4257-9c9f-e371e3c77619',
    'Je ne sais pas',
    4,
    '32a4b9b4-e9aa-4930-9983-31f477abc279',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, popup_description, ordre, type, description, max_choices, consultation_id) VALUES (
    '7bb4b993-6cc1-44d3-adf2-338df840dca0',
    'Votre boisson du matin ?',
    'Si vous prenez un thé goût café ou café goût thé... 🤔',
    2,
    'conditional',
    null,
    null,
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '865f783c-ece3-4f05-a0a2-39d1e77b39de',
    'Café ☕️',
    1,
    '7bb4b993-6cc1-44d3-adf2-338df840dca0',
    'e63da297-b427-4713-96c3-e495d95e5dcc',
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '25a8f600-d71a-409d-b403-e266d1639235',
    'Thé 🍵',
    2,
    '7bb4b993-6cc1-44d3-adf2-338df840dca0',
    'cde0382a-206d-4d3a-9c4e-665d336fbd21',
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '054563f4-b40a-4313-82cc-40f28d3a78d7',
    'SBF (Sans Boisson Fixe)',
    3,
    '7bb4b993-6cc1-44d3-adf2-338df840dca0',
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '8eca7400-cd7a-4187-8f4e-5d027b6fa7a1',
    'Autre',
    4,
    '7bb4b993-6cc1-44d3-adf2-338df840dca0',
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    1
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, popup_description, ordre, type, description, max_choices, next_question_id, consultation_id) VALUES (
    'e63da297-b427-4713-96c3-e495d95e5dcc',
    'Quel type de café ?',
    null,
    3,
    'unique',
    null,
    null,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '00e1838c-8ba2-4680-bc0d-e5a90f6ac89d',
    'Expresso',
    1,
    'e63da297-b427-4713-96c3-e495d95e5dcc',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    'e1f209fd-fd76-45a0-81e4-09e3bb8a34a0',
    'Allongé',
    2,
    'e63da297-b427-4713-96c3-e495d95e5dcc',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '8b5daea7-f33e-4932-ab3d-839c1d2a22c1',
    'Café au lait / noisette',
    3,
    'e63da297-b427-4713-96c3-e495d95e5dcc',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '3aaf4d54-840e-4912-b8a9-1f60a8b14d8c',
    'Autre',
    4,
    'e63da297-b427-4713-96c3-e495d95e5dcc',
    null,
    1
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, popup_description, ordre, type, description, max_choices, next_question_id,consultation_id) VALUES (
    'cde0382a-206d-4d3a-9c4e-665d336fbd21',
    'Quel type de thé ?',
    null,
    4,
    'unique',
    null,
    null,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '695a7d4f-e5d3-49db-b7bd-5a6d29c54314',
    'Vert',
    1,
    'cde0382a-206d-4d3a-9c4e-665d336fbd21',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    'b2963501-b70d-4c5d-8a9b-2e7c37e7dec0',
    'Noir',
    2,
    'cde0382a-206d-4d3a-9c4e-665d336fbd21',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '4c489d92-2866-4df0-81f1-d44484304d48',
    'Blanc',
    3,
    'cde0382a-206d-4d3a-9c4e-665d336fbd21',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    '8497bb69-a0f2-4832-acb6-e8d93fb9f05c',
    'Infusion',
    4,
    'cde0382a-206d-4d3a-9c4e-665d336fbd21',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id, next_question_id, has_open_text_field) VALUES (
    'c27c3f3e-5f02-447f-a8eb-8ff45cb91b15',
    'Autre',
    5,
    'cde0382a-206d-4d3a-9c4e-665d336fbd21',
    null,
    1
) ON CONFLICT DO NOTHING;

INSERT INTO questions (id, title, popup_description, ordre, type, description, max_choices, next_question_id, consultation_id) VALUES (
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    'Quels accompagnements ?',
    null,
    5,
    'multiple',
    null,
    11,
    null,
    '66013d57-d305-4deb-8990-fc3e53826244'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'a7b035b0-a518-4299-a1b2-9323f064d386',
    'Viennoiseries, tartines, crêpes...',
    1,
    0,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'cb766cbe-f52d-4783-ae05-69ad8a198745',
    'Fruits',
    2,
    0,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '19a5ab00-ba40-4fa6-86a8-e0463e07a2c4',
    'Légumes',
    3,
    0,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '29878a94-282a-4765-8fb2-934a85fa86f0',
    'Viandes, poissons, oeufs...',
    4,
    0,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'e9fa5448-1312-42e8-9ff9-347b69172f7e',
    'Yaourt, fromage blanc...',
    5,
    0,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'c7607f1c-4a9e-445e-a306-0c8a8b99f125',
    'Céréales',
    6,
    0,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '84d5887c-4739-45c9-8d79-cbe4f72536ba',
    'Gateaux, bonbons...',
    7,
    0,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '855cec83-20d0-4025-b4b7-99a3815dcbad',
    'Pâtes, riz, semoule, quinoa...',
    8,
    0,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '0eba1b17-7603-4838-8bc3-8b16ee17c70a',
    'Fromages',
    9,
    0,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'bd77e1ba-fd6c-4101-97d5-275e3fa7b4fb',
    'Soupes',
    10,
    0,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'b8f35a89-0a33-46c4-8601-4136a686a274',
    'Autres',
    11,
    1,
    'b16627fa-1a46-43f7-a179-5e72c6105aa6',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, popup_description, ordre, type, description, max_choices, consultation_id) VALUES (
    '12f262e7-7a2e-4318-914b-825cddbab3e8',
    'En résumé, votre petit déjeuner idéal ?',
    null,
    6,
    'open',
    null,
    null,
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates_v2(id, consultation_id, is_visible_to_unanswered_users_only, update_label, update_date, share_text_template, has_questions_info, responses_info_picto, responses_info_description, info_header_picto, info_header_description, has_participation_info, download_analysis_url, feedback_question_picto, feedback_question_title, feedback_question_description, footer_title, footer_description) VALUES(
    'aa0180a2-fa10-4cdf-888b-bd58bb6f1709',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    1,
    null,
    '2023-04-01 23:59:59.999',
    'Comme moi, tu peux participer à la Consultation : {title}
    https://agora.beta.gouv.fr/consultations/{id}',
    1,
    null,
    null,
    null,
    null,
    0,
    null,
    null,
    null,
    null,
    null,
    '<body><noa11y>🗣</noa11y> Consultation proposée par le <b>Capitaine P’tit Dej’</b><br/><br/><noa11y>🎯</noa11y><b> Objectif</b> : améliorer la qualité des petits déjeuners apportés à l’équipe AGORA <noa11y>🤤</noa11y>.</body>'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '8d72595b-6def-4cc3-9c2b-32a90f8336f9',
    'aa0180a2-fa10-4cdf-888b-bd58bb6f1709',
    null,
    'title',
    1,
    1,
    'Pourquoi cette consultation ?',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '4eb57ee9-190d-4999-94ad-09fd809911ae',
    'aa0180a2-fa10-4cdf-888b-bd58bb6f1709',
    null,
    'title',
    1,
    0,
    'Pourquoi cette consultation ?',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '09476a85-5115-4cca-a125-310b6e7b2f27',
    'aa0180a2-fa10-4cdf-888b-bd58bb6f1709',
    null,
    'richText',
    2,
    1,
    null,
    '<body><i>Ceci est une consultation fictive</i></body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    'b3e99d8c-6146-4b54-ae05-3359553b1a42',
    'aa0180a2-fa10-4cdf-888b-bd58bb6f1709',
    null,
    'richText',
    2,
    0,
    null,
    '<body><i>Ceci est une consultation fictive</i><br/><br/>Cette consultation est une démonstration des différents types de questions possibles avec cette application.</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates_v2(id, consultation_id, is_visible_to_unanswered_users_only, update_label, update_date, share_text_template, has_questions_info, responses_info_picto, responses_info_description, info_header_picto, info_header_description, has_participation_info, download_analysis_url, feedback_question_picto, feedback_question_title, feedback_question_description, footer_title, footer_description) VALUES(
    '86a3356d-7dad-48be-8bff-368279f8be07',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    0,
    null,
    '2023-04-01 23:59:59.999',
    'Comme moi, tu peux participer à la Consultation : {title}
    https://agora.beta.gouv.fr/consultations/{id}',
    0,
    '🙌',
    '<body><b>Merci pour votre participation</b> à cette consultation !</body>',
    null,
    null,
    1,
    null,
    null,
    null,
    null,
    'Envie d’aller plus loin ?',
    '<body>Rendez-vous sur:<br/><ul><li><a href="https://www.mangerbouger.fr/">Manger Bouger.fr</a></li><li>...</li></ul></body>'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '07253fad-bf64-43e1-9585-9b1a7f0c6c4c',
    '86a3356d-7dad-48be-8bff-368279f8be07',
    null,
    'title',
    1,
    1,
    'Les prochaines étapes',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '8f7313dc-8179-4771-a2b7-d96f12ee1ef6',
    '86a3356d-7dad-48be-8bff-368279f8be07',
    null,
    'title',
    1,
    0,
    'Les prochaines étapes',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '07253fad-bf64-43e1-9585-9b1a7f0c6c4c',
    '86a3356d-7dad-48be-8bff-368279f8be07',
    null,
    'richText',
    2,
    1,
    null,
    '<body><noa11y>👉</noa11y> Capitaine P’tit Dej’ recevra les résultats et les analysera.</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '5f75e508-1430-4944-b84e-2c75e264a539',
    '86a3356d-7dad-48be-8bff-368279f8be07',
    null,
    'richText',
    2,
    0,
    null,
    '<body><noa11y>👉</noa11y> Capitaine P’tit Dej’ recevra les résultats et les analysera.<br/>Une synthèse de vos réponses sera ensuite publiée pour vous informer des actions qui seront mises en oeuvre par la suite !</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates_v2(id, consultation_id, is_visible_to_unanswered_users_only, update_label, update_date, share_text_template, has_questions_info, responses_info_picto, responses_info_description, info_header_picto, info_header_description, has_participation_info, download_analysis_url, feedback_question_picto, feedback_question_title, feedback_question_description, footer_title, footer_description) VALUES(
    '6f372d50-e921-417a-8ed0-684a6c32e820',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    0,
    null,
    '2023-04-01 23:59:59.999',
    'Comme moi, tu peux participer à la Consultation : {title}
    https://agora.beta.gouv.fr/consultations/{id}',
    0,
    '🏁',
    '<body><b>Cette consultation est maintenant terminée.</b>
    Merci à tous !</body>',
    null,
    null,
    1,
    null,
    null,
    null,
    null,
    'Envie d’aller plus loin ?',
    '<body>Rendez-vous sur:<br/><ul><li><a href="https://www.mangerbouger.fr/">Manger Bouger.fr</a></li><li>...</li></ul></body>'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '07253fad-bf64-43e1-9585-9b1a7f0c6c4c',
    '6f372d50-e921-417a-8ed0-684a6c32e820',
    null,
    'title',
    1,
    1,
    'Les prochaines étapes',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '8f7313dc-8179-4771-a2b7-d96f12ee1ef6',
    '6f372d50-e921-417a-8ed0-684a6c32e820',
    null,
    'title',
    1,
    0,
    'Les prochaines étapes',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '07253fad-bf64-43e1-9585-9b1a7f0c6c4c',
    '6f372d50-e921-417a-8ed0-684a6c32e820',
    null,
    'richText',
    2,
    1,
    null,
    '<body><noa11y>👉</noa11y> Capitaine P’tit Dej’ recevra les résultats et les analysera.</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '5f75e508-1430-4944-b84e-2c75e264a539',
    '6f372d50-e921-417a-8ed0-684a6c32e820',
    null,
    'richText',
    2,
    0,
    null,
    '<body><noa11y>👉</noa11y> Capitaine P’tit Dej’ recevra les résultats et les analysera.<br/>Une synthèse de vos réponses sera ensuite publiée pour vous informer des actions qui seront mises en oeuvre par la suite !</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_history(id, consultation_id, step_number, type, consultation_update_id, title, action_text) VALUES(
    '08bd2b70-d667-4d5f-b69d-e6056f2fe1a0',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    1,
    'update',
    'aa0180a2-fa10-4cdf-888b-bd58bb6f1709',
    'Lancement',
    'Voir les objectifs'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_history(id, consultation_id, step_number, type, consultation_update_id, title, action_text) VALUES(
    '7aaaec7e-74af-4ae8-b028-3a74864a3dc7',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    2,
    'results',
    '6f372d50-e921-417a-8ed0-684a6c32e820',
    'Fin de la Consultation',
    'Consulter toutes les réponses'
) ON CONFLICT DO NOTHING;
```

</details>

<details>
    <summary>Consultation 2 - Click to expand SQL data</summary>

```
INSERT INTO consultations(id, title, start_date, end_date, cover_url, details_cover_url, question_count, estimated_time, participant_count_goal, description, tips_description, thematique_id) VALUES (
    'b7454b2e-74b9-454c-9cc5-afc411f962f1',
    'Des vacances pour tous !',
    '2023-7-1 23:59:59.999',
    '2023-9-30 23:59:59.999',
    'https://img.freepik.com/photos-gratuite/plage-parapluie-ete-concept-vacances-generative-ai_60438-2518.jpg',
    'https://img.freepik.com/photos-gratuite/plage-parapluie-ete-concept-vacances-generative-ai_60438-2518.jpg',
    '7',
    '10',
    10000,
    '<body>Ca y est, c’est l’heure de la rentrée ! Faisons le bilan <i>collectif </i>sur nos vacances, afin de savoir si celles-ci se sont bien déroulées pour nos concitoyens.<br/><br/>En effet c’est particulièrement important pour le <b>Ministre des vacances</b> d’être au courant afin d’ : <br/><ul><li>Améliorer l’<b>offre des prochaines vacances d’été</b><br/></li><li>Partir sur le bon <b>pied pour la rentrée</b>. <br/></li></ul><br/>Nous comptons sur vous pour tout nous dire !</body>',
    '<body>🗣 Consultation proposée par le <b>Ministre des vacances </b><br/><br/>🎯 Objectif : améliorer les vacances de nos concitoynes<br/><br/>🚀 Axe gouvernemental : Planifier et accélérer la transition écologique</body>',
    'a4bb4b27-3271-4278-83c9-79ac3eee843a'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates(id, step, description, consultation_id, explanations_title, video_title, video_intro, video_url, video_width, video_height, video_transcription, conclusion_title, conclusion_description) VALUES (
    'dba54fe2-7188-4612-b864-a2a7f690abdf',
    1,
    '<body><noa11y>👉</noa11y> Les réponses à cette consultation seront présentées au ministre des Vacances le 30 septembre.<br/>D’ici la fin de l’année 2023, le <b>Plan national des Vacances </b>doit être révisé et ses mesures renforcées afin de prendre en compte les nouveaux effets des vacances.<br/><br/>—<br/><br/><noa11y>🌳</noa11y><b> Envie d’aller plus loin ?</b><br/><br/><a href="https://www.service-public.fr/particuliers/vosdroits/F31952">Rendez-vous ici </a>pour voir les dates des prochaines vacances.</body>',
    'b7454b2e-74b9-454c-9cc5-afc411f962f1',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO questions (id, title, popup_description, ordre, type, description, max_choices, next_question_id, consultation_id) VALUES (
    '2c4ce51d-5278-4853-8b83-14fe5424ef9d',
    'Où avez-vous passé vos vacances ?',
    '<body>Vous pouvez avoir passé vos vacances à plusieurs endroits, auquel cas mettez tout.</body>',
    1,
    'multiple',
    null,
    4,
    null,
    'b7454b2e-74b9-454c-9cc5-afc411f962f1'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'c784bbc5-6ced-4e8f-8354-c44918ae2051',
    'A la mer',
    1,
    0,
    '2c4ce51d-5278-4853-8b83-14fe5424ef9d',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'b6bf1e1b-e790-472a-a688-16bbea410111',
    'A la montagne',
    2,
    0,
    '2c4ce51d-5278-4853-8b83-14fe5424ef9d',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'b0ed651a-7363-4ba5-96db-8c79f9fe75e5',
    'A la maison',
    3,
    0,
    '2c4ce51d-5278-4853-8b83-14fe5424ef9d',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '782f3844-0ea5-49b7-bd6f-24cffbfdb9fe',
    'Pas eu de vacances cet été !',
    4,
    0,
    '2c4ce51d-5278-4853-8b83-14fe5424ef9d',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO questions (id, title, popup_description, ordre, type, description, max_choices, next_question_id, consultation_id) VALUES (
    'd4a629f7-fb7b-464a-a3ad-43874c63bc9f',
    'Avez-vous passé de bonnes vacances ?',
    '<body>Dites-nous la <b>vérité</b> ! </body>',
    2,
    'conditional',
    null,
    null,
    null,
    'b7454b2e-74b9-454c-9cc5-afc411f962f1'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '0fdb3663-a062-4a4e-80d1-a68f32d4f866',
    'Ouiiii super',
    1,
    0,
    'd4a629f7-fb7b-464a-a3ad-43874c63bc9f',
    'e885dee0-db77-4558-8aec-f9d9a7f10e2d'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'd3c21f13-7483-4d1b-9b64-d72f73c0a8e5',
    'Ca va',
    2,
    0,
    'd4a629f7-fb7b-464a-a3ad-43874c63bc9f',
    'e885dee0-db77-4558-8aec-f9d9a7f10e2d'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'f5d8a70f-a9a5-4f88-8dfd-4e04e8de5322',
    'Catastrophique',
    3,
    0,
    'd4a629f7-fb7b-464a-a3ad-43874c63bc9f',
    'e885dee0-db77-4558-8aec-f9d9a7f10e2d'
) ON CONFLICT DO NOTHING;

INSERT INTO questions (id, title, popup_description, ordre, type, description, max_choices, next_question_id, consultation_id) VALUES (
    'e2801d38-7a7e-4544-b7a2-4e708a0eaba8',
    'Qu’est-ce qui vous a le plus plu',
    null,
    3,
    'unique',
    null,
    null,
    '31f3fd47-9aae-449d-80a9-690baceb0076',
    'b7454b2e-74b9-454c-9cc5-afc411f962f1'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'e6ff8a83-0d38-43ff-9f7f-df60887b354b',
    'Mes amis, ma famille',
    1,
    0,
    'e2801d38-7a7e-4544-b7a2-4e708a0eaba8',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    'e63a0cb7-8b41-43b0-8a9f-08a900513fa2',
    'Voir la nature',
    2,
    0,
    'e2801d38-7a7e-4544-b7a2-4e708a0eaba8',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '3e02156a-48e9-4f62-bd5c-6ad3425f3513',
    'Ne rien faire',
    3,
    0,
    'e2801d38-7a7e-4544-b7a2-4e708a0eaba8',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO questions (id, title, popup_description, ordre, type, description, max_choices, next_question_id, consultation_id) VALUES (
    '8724fee3-4bb1-4765-815d-ea88db14bde6',
    'C’est un petit ça va ça.',
    null,
    4,
    'chapter',
    '<body>Parfois la vie c’est comme ça. C’est <b>moyen</b> ! Mais c’est bien ! </body>',
    null,
    '31f3fd47-9aae-449d-80a9-690baceb0076',
    'b7454b2e-74b9-454c-9cc5-afc411f962f1'
) ON CONFLICT DO NOTHING;

INSERT INTO questions (id, title, popup_description, ordre, type, description, max_choices, next_question_id, consultation_id) VALUES (
    '68966e00-6d3d-4e7c-a974-4394aa9aebeb',
    'Oh noooon, que s’est il passé?',
    null,
    5,
    'open',
    null,
    null,
    null,
    'b7454b2e-74b9-454c-9cc5-afc411f962f1'
) ON CONFLICT DO NOTHING;

INSERT INTO questions (id, title, popup_description, ordre, type, description, max_choices, next_question_id, consultation_id) VALUES (
    '31f3fd47-9aae-449d-80a9-690baceb0076',
    'Quel est pour vous un congé idéal ?',
    null,
    6,
    'unique',
    null,
    null,
    null,
    'b7454b2e-74b9-454c-9cc5-afc411f962f1'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '013347d8-2d43-4e1d-b63d-910e2747333e',
    'Une semaine',
    1,
    0,
    '31f3fd47-9aae-449d-80a9-690baceb0076',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '299d6369-631d-41f7-9dc6-39d2b079dbaa',
    '2 semaines',
    2,
    0,
    '31f3fd47-9aae-449d-80a9-690baceb0076',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '5fe38e8f-584c-48f2-98a6-7f102d616f13',
    'Un mois',
    3,
    0,
    '31f3fd47-9aae-449d-80a9-690baceb0076',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible (id, label, ordre, has_open_text_field, question_id, next_question_id) VALUES (
    '71a20d27-b187-4e3c-b211-457f534b63ec',
    'Un an',
    4,
    0,
    '31f3fd47-9aae-449d-80a9-690baceb0076',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates_v2(id, consultation_id, is_visible_to_unanswered_users_only, update_label, update_date, share_text_template, has_questions_info, responses_info_picto, responses_info_description, info_header_picto, info_header_description, has_participation_info, download_analysis_url, feedback_question_picto, feedback_question_title, feedback_question_description, footer_title, footer_description) VALUES(
    'ccbecde2-52e7-4094-8c2b-0c56cb4d53ce',
    'b7454b2e-74b9-454c-9cc5-afc411f962f1',
    1,
    null,
    '2023-09-1 23:59:59.999',
    'Comme moi, tu peux participer à la Consultation : {title}
    https://agora.beta.gouv.fr/consultations/{id}',
    1,
    null,
    null,
    null,
    null,
    0,
    null,
    null,
    null,
    null,
    null,
    '<body>🗣 Consultation proposée par le <b>Ministre des vacances </b><br/><br/>🎯 Objectif : améliorer les vacances de nos concitoynes<br/><br/>🚀 Axe gouvernemental : Planifier et accélérer la transition écologique</body>'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    'ed39b6ec-40c3-4656-83cd-5c1ca2f16441',
    'ccbecde2-52e7-4094-8c2b-0c56cb4d53ce',
    null,
    'title',
    1,
    1,
    'Pourquoi cette consultation ?',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '86a10a97-9178-4dbe-b965-e4189b1693a5',
    'ccbecde2-52e7-4094-8c2b-0c56cb4d53ce',
    null,
    'title',
    1,
    0,
    'Pourquoi cette consultation ?',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '23f7672e-f151-495f-a462-e148301328eb',
    'ccbecde2-52e7-4094-8c2b-0c56cb4d53ce',
    null,
    'richText',
    2,
    1,
    null,
    '<body>Ca y est, c’est l’heure de la rentrée ! Faisons le bilan <i>collectif </i>sur nos vacances, afin de savoir si celles-ci se sont bien déroulées pour nos concitoyens.</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '6207c968-ae62-4658-9bcd-711f73945519',
    'ccbecde2-52e7-4094-8c2b-0c56cb4d53ce',
    null,
    'richText',
    2,
    0,
    null,
    '<body>Ca y est, c’est l’heure de la rentrée ! Faisons le bilan <i>collectif </i>sur nos vacances, afin de savoir si celles-ci se sont bien déroulées pour nos concitoyens.<br/><br/>En effet c’est particulièrement important pour le <b>Ministre des vacances</b> d’être au courant afin d’ : <br/><ul><li>Améliorer l’<b>offre des prochaines vacances d’été</b><br/></li><li>Partir sur le bon <b>pied pour la rentrée</b>. <br/></li></ul><br/>Nous comptons sur vous pour tout nous dire !</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates_v2(id, consultation_id, is_visible_to_unanswered_users_only, update_label, update_date, share_text_template, has_questions_info, responses_info_picto, responses_info_description, info_header_picto, info_header_description, has_participation_info, download_analysis_url, feedback_question_picto, feedback_question_title, feedback_question_description, footer_title, footer_description) VALUES(
    '897369f5-cb04-4b75-960a-62cdfe47422f',
    'b7454b2e-74b9-454c-9cc5-afc411f962f1',
    0,
    null,
    '2023-9-1 23:59:59.999',
    'Comme moi, tu peux participer à la Consultation : {title}
    https://agora.beta.gouv.fr/consultations/{id}',
    0,
    '🙌',
    '<body><b>Merci pour votre participation</b> à cette consultation !</body>',
    null,
    null,
    1,
    null,
    null,
    null,
    null,
    'Envie d’aller plus loin ?',
    '<body><a href="https://www.service-public.fr/particuliers/vosdroits/F31952">Rendez-vous ici pour voir les dates des prochaines vacances.</a></body>'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    'a5159139-2f39-4745-9149-e94d13134888',
    '897369f5-cb04-4b75-960a-62cdfe47422f',
    null,
    'title',
    1,
    1,
    'Les prochaines étapes',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    'c642b86f-7662-4445-b69f-8a65878fe68f',
    '897369f5-cb04-4b75-960a-62cdfe47422f',
    null,
    'title',
    1,
    0,
    'Les prochaines étapes',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '1fe66260-338c-4fda-9170-90081d6b53d1',
    '897369f5-cb04-4b75-960a-62cdfe47422f',
    null,
    'richText',
    2,
    1,
    null,
    '<body><noa11y>👉</noa11y> Les réponses à cette consultation seront présentées au ministre des Vacances le 30 septembre.</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '6ddb1fee-bd83-4f66-a58f-2664f2aefee4',
    '897369f5-cb04-4b75-960a-62cdfe47422f',
    null,
    'richText',
    2,
    0,
    null,
    '<body><noa11y>👉</noa11y> Les réponses à cette consultation seront présentées au ministre des Vacances le 30 septembre.<br/>D’ici la fin de l’année 2023, le <b>Plan national des Vacances </b>doit être révisé et ses mesures renforcées afin de prendre en compte les nouveaux effets des vacances.<br/><br/>—<br/><br/><noa11y>🌳</noa11y></body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates_v2(id, consultation_id, is_visible_to_unanswered_users_only, update_label, update_date, share_text_template, has_questions_info, responses_info_picto, responses_info_description, info_header_picto, info_header_description, has_participation_info, download_analysis_url, feedback_question_picto, feedback_question_title, feedback_question_description, footer_title, footer_description) VALUES(
    '2efd7a52-ec78-4fa1-8978-3b1610b23d34',
    'b7454b2e-74b9-454c-9cc5-afc411f962f1',
    0,
    null,
    '2023-9-30 23:59:59.999',
    'Comme moi, tu peux participer à la Consultation : {title}
    https://agora.beta.gouv.fr/consultations/{id}',
    0,
    '🏁',
    '<body><b>Cette consultation est maintenant terminée.</b>
    Merci à tous !</body>',
    null,
    null,
    1,
    null,
    null,
    null,
    null,
    'Envie d’aller plus loin ?',
    '<body><a href="https://www.service-public.fr/particuliers/vosdroits/F31952">Rendez-vous ici pour voir les dates des prochaines vacances.</a></body>'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '6c05ad2a-0ec4-4025-88b9-16bd4c3cd8ec',
    '2efd7a52-ec78-4fa1-8978-3b1610b23d34',
    null,
    'title',
    1,
    1,
    'Les prochaines étapes',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '26e85a92-ed12-48b5-a608-c6c37c6ac1aa',
    '2efd7a52-ec78-4fa1-8978-3b1610b23d34',
    null,
    'title',
    1,
    0,
    'Les prochaines étapes',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '451cc0d5-e155-4b7e-96cb-7a9157917fef',
    '2efd7a52-ec78-4fa1-8978-3b1610b23d34',
    null,
    'richText',
    2,
    1,
    null,
    '<body><noa11y>👉</noa11y> Les réponses à cette consultation seront présentées au ministre des Vacances le 30 septembre.</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '0238d7ed-5cc1-4384-a35f-8755a9526bdd',
    '2efd7a52-ec78-4fa1-8978-3b1610b23d34',
    null,
    'richText',
    2,
    0,
    null,
    '<body><noa11y>👉</noa11y> Les réponses à cette consultation seront présentées au ministre des Vacances le 30 septembre.<br/>D’ici la fin de l’année 2023, le <b>Plan national des Vacances </b>doit être révisé et ses mesures renforcées afin de prendre en compte les nouveaux effets des vacances.<br/><br/>—<br/><br/><noa11y>🌳</noa11y></body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates_v2(id, consultation_id, is_visible_to_unanswered_users_only, update_label, update_date, share_text_template, has_questions_info, responses_info_picto, responses_info_description, info_header_picto, info_header_description, has_participation_info, download_analysis_url, feedback_question_picto, feedback_question_title, feedback_question_description, footer_title, footer_description) VALUES(
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    'b7454b2e-74b9-454c-9cc5-afc411f962f1',
    0,
    'Synthèse disponible !',
    '2024-02-25 18:00:00',
    'Les résultats de la Consultation {title} sont disponibles !
    https://agora.beta.gouv.fr/consultations/{id}',
    0,
    null,
    null,
    null,
    null,
    0,
    'https://content.agora.beta.gouv.fr/participation_citoyenne/step2-rapportvdef.pdf',
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '4acf6a22-e321-45ba-9ff0-2712de1463fb',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'title',
    1,
    1,
    'Synthèse de vos réponses',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    'd27ec1e0-df80-4bcd-af74-894c6421192e',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'title',
    1,
    0,
    'Synthèse de vos réponses',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '96567802-6cc9-45e6-8e95-6ac2594ea642',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'richText',
    2,
    1,
    null,
    '<body>Beaucoup d‘entre vous sont allés en vacances pendant <b>au moins 2 semaines</b> ! Retour sur les principaux enseignements de cette consultation</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '9544d622-550d-4d13-95c2-ea514730fad7',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'richText',
    2,
    0,
    null,
    '<body>Beaucoup d‘entre vous sont allés en vacances pendant <b>au moins 2 semaines</b> ! Retour sur les principaux enseignements de cette consultation</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '9075f12a-89b1-45e2-ab75-de19d7f9ddda',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'title',
    3,
    0,
    'Le trio gagnant: se baigner, bronzer, profiter',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    'c5ce49ae-3633-47b5-a0c2-1a1772e2942f',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'focusNumber',
    4,
    0,
    '50%',
    'Des <b>citoyens</b> ont préférés profiter des chaleurs d‘été pour se rafraîchir et prendre des couleurs à la plage.',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '2f87dc12-8011-41f6-bb1a-e8b816654810',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'image',
    5,
    0,
    null,
    'Nuage de mots avec en plus gros: gouvernement, application et curiosité',
    'https://content.agora.incubateur.net/participation_citoyenne/step2-b07.png',
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '9544d622-550d-4d13-95c2-ea514730fad7',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'richText',
    6,
    0,
    null,
    '<body><noa11y>💡</noa11y> En résumé, vos principales préoccupations pour avoir des vacances idéales: soleil, empreinte carbonne et... Serge le lama</body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '500280b5-b5d1-4fb3-a2f0-93e43c6aa928',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'quote',
    7,
    0,
    null,
    '<i>Joff-tchoff-tchoffo-tchoffo-tchoff!
    Tchoff-tchoff-tchoffo-tchoffo-tchoff!
    Joff-tchoff-tchoffo-tchoffo-tchoff!</i> - Serge',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    'f35f76b0-89ab-406e-ad13-5740f34dce74',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'video',
    8,
    0,
    null,
    null,
    'https://content.agora.incubateur.net/qag_responses/pourquoiCreerAgoraQagResponse.mp4',
    480,
    854,
    'Olivier Véran',
    'Ancien ministre du Renouvellement Démocratique et porte parole du gouvernement',
    '2023-06-05',
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '0015dff2-458a-451c-b0a3-61b7e0511fa8',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    null,
    'accordion',
    9,
    0,
    'Premières analyses de vos réponses ouvertes',
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    'ed71caaf-9705-4d10-8c1f-a2d3d2dc4e21',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    '0015dff2-458a-451c-b0a3-61b7e0511fa8',
    'quote',
    1,
    0,
    null,
    '<i>La plupart du séjour s‘est plutôt bien passé, par contre la météo s‘est gâtée sur la fin</i> - <b>Un Agoranaute bien malchanceux</b>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, is_preview, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '03db3cc1-304c-4e53-9c99-1ce25c2bcb2e',
    'fba95928-dbf4-4456-995c-f423e81da7aa',
    '0015dff2-458a-451c-b0a3-61b7e0511fa8',
    'richText',
    2,
    0,
    null,
    '<body>Cette phrase résume assez bien la majorité de vos feedbacks pour les personnes ayant <b>passé leur vacances en France</b></body>',
    null,
    null,
    null,
    null,
    null,
    null,
    null
) ON CONFLICT DO NOTHING;
    
INSERT INTO consultation_update_history(id, consultation_id, step_number, type, consultation_update_id, title, action_text) VALUES(
    'a3b0a137-0fcd-4d97-9062-8f2321a7511f',
    'b7454b2e-74b9-454c-9cc5-afc411f962f1',
    1,
    'update',
    'ccbecde2-52e7-4094-8c2b-0c56cb4d53ce',
    'Lancement',
    'Voir les objectifs'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_history(id, consultation_id, step_number, type, consultation_update_id, title, action_text) VALUES(
    'c52a6eb6-3ad9-4642-8169-988695c407a1',
    'b7454b2e-74b9-454c-9cc5-afc411f962f1',
    2,
    'results',
    '2efd7a52-ec78-4fa1-8978-3b1610b23d34',
    'Fin de la Consultation',
    'Consulter toutes les réponse'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_history(id, consultation_id, step_number, type, consultation_update_id, title, action_text) VALUES(
    '8e57bea1-c7e4-49c6-a3c1-8558c6b16a32',
    'b7454b2e-74b9-454c-9cc5-afc411f962f1',
    3,
    'update',
    null,
    'Synthèse des données',
    null
) ON CONFLICT DO NOTHING;
```
</details>

### b. Questions

<details>
    <summary>Click to expand SQL data</summary>

```
INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    'f29c5d6f-9838-4c57-a7ec-0612145bb0c8',
    'Pourquoi avoir créé l’application Agora ?',
    'Quel est le but de l’application Agora ? A quel besoin des Français et des Françaises espérez-vous qu’elle réponde ? Que signifie son caractère expérimental ?',
    '2023-09-27',
    1,
    'Agora',
    '30671310-ee62-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO responses_qag(id, author, author_portrait_url, author_description, response_date, video_url, transcription, qag_id) VALUES (
    '38990baf-b0ed-4db0-99a5-7ec01790720e',
    'Olivier Véran',
    'https://content.agora.incubateur.net/portraits/QaG-OlivierVeran.png',
    'Ministre délégué auprès de la Première ministre, chargé du Renouveau démocratique, porte-parole du Gouvernement',
    '2023-06-05',
    'https://content.agora.incubateur.net/qag_responses/pourquoiCreerAgoraQagResponse.mp4',
    'Sur votre téléphone, vous avez sans doute des applis pour plein de choses : pour commander un train, pour réserver un hôtel, pour noter un restaurant, pour faire des jeux… Mais jusqu’ici, vous n’aviez pas d’application pour la démocratie !                            

Vous n’aviez pas d’application qui vous permette de donner votre avis sur les sujets qui vous préoccupent : sujets environnementaux, d’emploi, d’immigration, de santé…                                                                                                    

Et nous, on est le Gouvernement du « avec vous », c’est-à-dire on est le Gouvernement qui veut avancer avec les Français.            

Vous avez envie d’être utile ? Vous aimez la politique ? Vous en parlez autour de vous avec vos collègues, dans les repas de famille, à la machine à café ? Maintenant, vous allez pouvoir aussi en parler sur cette application qui va vous permettre de donner vos avis. 

A nous d’avoir vos opinions, d’avoir aussi les idées que vous pouvez porter et de mieux faire le travail de loi pour être plus en contact avec vous.                                                                                                                       

C’est dans la continuité de ce que le Président de la République a commencé à faire avec le Grand débat national, plus de 2 millions de Français y ont participé, mais aussi avec les conventions citoyennes. La dernière en date sur la fin de vie va d’ailleurs permettre d’éclairer le travail de la loi.                                     +

Et donc chaque semaine, on va vous poser des questions. On va vous interroger sur des thématiques particulières pour nous aider à pré
parer des lois qui vous correspondent.                                                                                                

L’autre fonctionnalité, c’est celle à laquelle je me prête actuellement, vous allez pouvoir nous interroger. Et vous allez pouvoir vo
ter pour la ou les meilleures questions qui vous semblent devoir être posées au Gouvernement. Et moi, chaque semaine, j’irai chercher 
mes collègues pour qu’ils puissent vous répondre directement en vidéo.

Donc on va aussi rapprocher le politique du citoyen et le citoyen du politique. C’est ça, aussi, le Renouveau démocratique !',
    'f29c5d6f-9838-4c57-a7ec-0612145bb0c8'
) ON CONFLICT DO NOTHING;


INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '996436ca-ee69-11ed-a05b-0242ac120003',
    'On parle beaucoup de l’intelligence artificielle… vous comptez faire quoi pour les emplois qu’elle risque de remplacer ?',
    'ChatGPT est arrivé avec fureur et on voit déjà combien un certain nombre de tâches et de métier pourraient être rapidement remplacés par l’intelligence artificielle… Faut-il interdire ChatGPT? Faut-il ré-orienter les travailleurs concernés ?',
    '2024-02-18',
    1,
    'Nina',
    '1f3dbdc6-cff7-4d6a-88b5-c5ec84c55d15',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    'f8776dd0-ee69-11ed-a05b-0242ac120003',
    'Pourquoi ne taxe-t-on pas le kérosène des avions ? La France doit militer pour !',
    'Je trouve ça absolument lunaire que le kérosène des avions ne soit pas taxés, alors que les carburants des autres modes de transports le sont. Il s’agit d’une mesure à prendre au niveau international : la France défend-elle cette position de pur bon sens et quand peut-on espérer avoir des résultats ?',
    '2024-02-20',
    1,
    'Elliott',
    'bb051bf2-644b-47b6-9488-7759fa727dc0',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '40c04882-ee6a-11ed-a05b-0242ac120003',
    'Peut-on refuser d’être pris en charge par un soignant non-vacciné ? Personnellement, ça me dérange.',
    'Au pays de Pasteur, je trouve ça inadmissible que les soignants non-vaccinés soient ré-intégrés dans le corps hospitalier. Est-ce possible de refuser qu’un tel soignant me soigne ? Désolée mais je ne fais pas confiance à quelqu’un qui ne croit pas à la science.',
    '2024-02-20',
    1,
    'Laure',
    'a4bb4b27-3271-4278-83c9-79ac3eee843a',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '8171c50e-ee6a-11ed-a05b-0242ac120003',
    'Quel va être l’impact de la baisse de la note de la France par l’agence de notation financière Fitch ?',
    'L’agence de notation financière Fitch a baissé la note de la France, passant de AA à AA-. Qu’est-ce que ça veut dire concrètement ? Est-ce un signal pour l’avenir ou cela a-t-il un impact financier direct ?',
    '2024-02-18',
    1,
    'Patrick',
    'c97c3afd-1940-4b6d-950a-734b885ee5cb',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '1731a370-ee6b-11ed-a05b-0242ac120003',
    'Ma question, c’est pourquoi on n’arrive pas, dans ce pays, à recruter davantage de professeurs ?!',
    'Il y a bien sûr la question de la rémunération, mais au-delà de ça, c’est aussi la position sociale et la reconnaissance même du professeur qui semble avoir “baissé” ces 20 dernières années. Comment remédier à cela ?',
    '2024-02-18',
    1,
    'Agny',
    '5b9180e6-3e43-4c63-bcb5-4eab621fc016',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '5c5f5460-ee6b-11ed-a05b-0242ac120003',
    'Aurait-on pu faire quelque chose pour éviter la guerre en Ukraine ? Et les prochaines ? Je m’interroge',
    'La guerre en Ukraine semble partie pour durer, avec les conséquences dramatiques que l’on sait. Aurait-on pu éviter cette guerre ? Peut-on faire quelque chose pour éviter qu’il y en ait d’autres ?',
    '2024-02-18',
    1,
    'Béatrice',
    '8e200137-df3b-4bde-9981-b39a3d326da7',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '96e8341c-ee6b-11ed-a05b-0242ac120003',
    'Pourquoi ne pas tester un tarif unique de transport en commun pas trop cher partout en France comme en Allemagne ?',
    'L’Allemagne a lancé en mai DeutschlandTicket à 49 euros par mois.qui permet avec un seul billet d’aller dans n’importe quel transport en commun local et régional. Ne serait-ce pas un bon coup de pouce pour le pouvoir d’achat et la promotion des transports en commun ?',
    '2024-02-20',
    1,
    'Danny',
    '0f644115-08f3-46ff-b776-51f19c65fdd1',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    'd09f2788-ee6b-11ed-a05b-0242ac120003',
    'La France devrait autoriser la reconnaissance faciale par vidéo-surveillance pour lutter contre les délits du quotidien : qu’est-ce qu’on attend ?',
    'Y-a-t-il eu des expérimentations sur ces dispositifs ? Est-ce que c’est efficace ? Est-ce que les JO vont être l’occasion de tester des choses sur ce sujet ?',
    '2024-02-18',
    1,
    'Josette',
    'b276606e-f251-454e-9a73-9b70a6f30bfd',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '1cde43fe-ee6c-11ed-a05b-0242ac120003',
    'Est-ce qu’il ne serait pas temps d’avoir enfin (!) un vrai débat sur la légalisation du cannabis, comme ils le font dans plein de pays ?',
    'On consomme beaucoup de cannabis en France, même si c’est illégal. Ne devrait-on pas avoir un débat sur une potentielle légalisation pour que cette réalité cesse de nourrir des trafics illégaux ? Arrêtons l’hypocrisie ! (Et ce serait sûrement bon pour les finances de l’Etat …)',
    '2024-02-18',
    1,
    'Tyron',
    '47897e51-8e94-4920-a26a-1b1e5e232e82',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '59028e08-ee6c-11ed-a05b-0242ac120003',
    'Quel est le niveau de la menace terroriste sur la France aujourd’hui ?',
    'On se doute qu’il y a des attentats déjoués dont nous ne sommes pas au courant et c’est bien normal. Mais pouvez-vous nous dire par rapport aux années précédentes et des événements dans le monde actuel à quel niveau de menace nous situons-nous ?',
    '2024-02-20',
    1,
    'Kevin',
    'b276606e-f251-454e-9a73-9b70a6f30bfd',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO qag_updates(id, qag_id, user_id, status, moderated_date, reason, should_delete_flag) VALUES (
    '996436ca-ee69-11ed-a05b-0242ac120003',
    '996436ca-ee69-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000',
    1,
    '2024-02-18',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO qag_updates(id, qag_id, user_id, status, moderated_date, reason, should_delete_flag) VALUES (
    'f8776dd0-ee69-11ed-a05b-0242ac120003',
    'f8776dd0-ee69-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000',
    1,
    '2024-02-20',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO qag_updates(id, qag_id, user_id, status, moderated_date, reason, should_delete_flag) VALUES (
    '40c04882-ee6a-11ed-a05b-0242ac120003',
    '40c04882-ee6a-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000',
    1,
    '2024-02-20',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO qag_updates(id, qag_id, user_id, status, moderated_date, reason, should_delete_flag) VALUES (
    '8171c50e-ee6a-11ed-a05b-0242ac120003',
    '8171c50e-ee6a-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000',
    1,
    '2024-02-18',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO qag_updates(id, qag_id, user_id, status, moderated_date, reason, should_delete_flag) VALUES (
    '1731a370-ee6b-11ed-a05b-0242ac120003',
    '1731a370-ee6b-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000',
    1,
    '2024-02-18',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO qag_updates(id, qag_id, user_id, status, moderated_date, reason, should_delete_flag) VALUES (
    '5c5f5460-ee6b-11ed-a05b-0242ac120003',
    '5c5f5460-ee6b-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000',
    1,
    '2024-02-18',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO qag_updates(id, qag_id, user_id, status, moderated_date, reason, should_delete_flag) VALUES (
    '96e8341c-ee6b-11ed-a05b-0242ac120003',
    '96e8341c-ee6b-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000',
    1,
    '2024-02-20',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO qag_updates(id, qag_id, user_id, status, moderated_date, reason, should_delete_flag) VALUES (
    'd09f2788-ee6b-11ed-a05b-0242ac120003',
    'd09f2788-ee6b-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000',
    1,
    '2024-02-18',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO qag_updates(id, qag_id, user_id, status, moderated_date, reason, should_delete_flag) VALUES (
    '1cde43fe-ee6c-11ed-a05b-0242ac120003',
    '1cde43fe-ee6c-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000',
    1,
    '2024-02-18',
    null,
    0
) ON CONFLICT DO NOTHING;

INSERT INTO qag_updates(id, qag_id, user_id, status, moderated_date, reason, should_delete_flag) VALUES (
    '59028e08-ee6c-11ed-a05b-0242ac120003',
    '59028e08-ee6c-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000',
    1,
    '2024-02-20',
    null,
    0
) ON CONFLICT DO NOTHING;
```
</details>
