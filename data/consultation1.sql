DELETE FROM consultations WHERE id = 'f5fd9c1d-6583-494c-8b0f-78129d6a0382';
DELETE FROM consultation_updates WHERE id = '3c2d744f-7143-442b-aaa4-53bfe5f5f451';
DELETE FROM questions WHERE id = '32a4b9b4-e9aa-4930-9983-31f477abc279';
DELETE FROM choixpossible WHERE id = '351fb2dd-8737-49ef-b7a9-7057dbd5920c';
DELETE FROM choixpossible WHERE id = '35ccc5f3-02d0-4704-a02d-972b777bcb63';
DELETE FROM choixpossible WHERE id = '865b3e7b-1fe8-4c55-b056-3042ca34bc36';
DELETE FROM choixpossible WHERE id = '3e811511-5aba-4257-9c9f-e371e3c77619';
DELETE FROM questions WHERE id = '7bb4b993-6cc1-44d3-adf2-338df840dca0';
DELETE FROM choixpossible WHERE id = '865f783c-ece3-4f05-a0a2-39d1e77b39de';
DELETE FROM choixpossible WHERE id = '25a8f600-d71a-409d-b403-e266d1639235';
DELETE FROM choixpossible WHERE id = '054563f4-b40a-4313-82cc-40f28d3a78d7';
DELETE FROM choixpossible WHERE id = '8eca7400-cd7a-4187-8f4e-5d027b6fa7a1';
DELETE FROM questions WHERE id = 'e63da297-b427-4713-96c3-e495d95e5dcc';
DELETE FROM choixpossible WHERE id = '00e1838c-8ba2-4680-bc0d-e5a90f6ac89d';
DELETE FROM choixpossible WHERE id = 'e1f209fd-fd76-45a0-81e4-09e3bb8a34a0';
DELETE FROM choixpossible WHERE id = '8b5daea7-f33e-4932-ab3d-839c1d2a22c1';
DELETE FROM choixpossible WHERE id = '3aaf4d54-840e-4912-b8a9-1f60a8b14d8c';
DELETE FROM questions WHERE id = 'cde0382a-206d-4d3a-9c4e-665d336fbd21';
DELETE FROM choixpossible WHERE id = '695a7d4f-e5d3-49db-b7bd-5a6d29c54314';
DELETE FROM choixpossible WHERE id = 'b2963501-b70d-4c5d-8a9b-2e7c37e7dec0';
DELETE FROM choixpossible WHERE id = '4c489d92-2866-4df0-81f1-d44484304d48';
DELETE FROM choixpossible WHERE id = '8497bb69-a0f2-4832-acb6-e8d93fb9f05c';
DELETE FROM choixpossible WHERE id = 'c27c3f3e-5f02-447f-a8eb-8ff45cb91b15';
DELETE FROM questions WHERE id = 'b16627fa-1a46-43f7-a179-5e72c6105aa6';
DELETE FROM choixpossible WHERE id = 'a7b035b0-a518-4299-a1b2-9323f064d386';
DELETE FROM choixpossible WHERE id = 'cb766cbe-f52d-4783-ae05-69ad8a198745';
DELETE FROM choixpossible WHERE id = '19a5ab00-ba40-4fa6-86a8-e0463e07a2c4';
DELETE FROM choixpossible WHERE id = '29878a94-282a-4765-8fb2-934a85fa86f0';
DELETE FROM choixpossible WHERE id = 'e9fa5448-1312-42e8-9ff9-347b69172f7e';
DELETE FROM choixpossible WHERE id = 'c7607f1c-4a9e-445e-a306-0c8a8b99f125';
DELETE FROM choixpossible WHERE id = '84d5887c-4739-45c9-8d79-cbe4f72536ba';
DELETE FROM choixpossible WHERE id = '855cec83-20d0-4025-b4b7-99a3815dcbad';
DELETE FROM choixpossible WHERE id = '0eba1b17-7603-4838-8bc3-8b16ee17c70a';
DELETE FROM choixpossible WHERE id = 'bd77e1ba-fd6c-4101-97d5-275e3fa7b4fb';
DELETE FROM choixpossible WHERE id = 'b8f35a89-0a33-46c4-8601-4136a686a274';
DELETE FROM questions WHERE id = '12f262e7-7a2e-4318-914b-825cddbab3e8';
DELETE FROM consultation_updates_v2 WHERE id = 'aa0180a2-fa10-4cdf-888b-bd58bb6f1709';
DELETE FROM consultation_update_sections WHERE id = '8d72595b-6def-4cc3-9c2b-32a90f8336f9';
DELETE FROM consultation_update_sections WHERE id = '09476a85-5115-4cca-a125-310b6e7b2f27';
DELETE FROM consultation_update_sections WHERE id = 'b3e99d8c-6146-4b54-ae05-3359553b1a42';
DELETE FROM consultation_updates_v2 WHERE id = '86a3356d-7dad-48be-8bff-368279f8be07';
DELETE FROM consultation_update_sections WHERE id = '07253fad-bf64-43e1-9585-9b1a7f0c6c4c';
DELETE FROM consultation_update_sections WHERE id = 'e9ba6b6f-e43f-474f-b89d-6f0faf0e2586';
DELETE FROM consultation_update_sections WHERE id = '5f75e508-1430-4944-b84e-2c75e264a539';
DELETE FROM consultation_updates_v2 WHERE id = '6f372d50-e921-417a-8ed0-684a6c32e820';
DELETE FROM consultation_update_sections WHERE id = '25da8e64-e09e-4cc3-8d8d-01f63233c0f2';
DELETE FROM consultation_update_sections WHERE id = 'bfcb98ef-e0e8-4a66-b9bc-78db28d75a21';
DELETE FROM consultation_update_sections WHERE id = '5f75e508-1430-4944-b84e-2c75e264a539';
DELETE FROM consultation_update_history WHERE id = '08bd2b70-d667-4d5f-b69d-e6056f2fe1a0';
DELETE FROM consultation_update_history WHERE id = '7aaaec7e-74af-4ae8-b028-3a74864a3dc7';

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
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382'
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

INSERT INTO consultation_updates_v2(id, consultation_id, is_visible_to_unanswered_users_only, update_label, update_date, share_text_template, has_questions_info, responses_info_picto, responses_info_description, responses_info_action_text,info_header_picto, info_header_description, has_participation_info, download_analysis_url, feedback_question_picto, feedback_question_title, feedback_question_description, footer_title, footer_description, goals) VALUES(
    'aa0180a2-fa10-4cdf-888b-bd58bb6f1709',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    1,
    null,
    '2023-04-01 23:59:59.999',
    'Comme moi, tu peux participer à la Consultation : {title}
{url}',
    1,
    null,
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
    null,
    '🗣<body>Consultation proposée par le <b>Capitaine P’tit Dej’</b></body>|🎯<body><b>Objectif</b> : améliorer la qualité des petits déjeuners apportés à l’équipe AGORA <noa11y>🤤</noa11y>.</body>'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, visibility_type, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '8d72595b-6def-4cc3-9c2b-32a90f8336f9',
    'aa0180a2-fa10-4cdf-888b-bd58bb6f1709',
    null,
    'title',
    1,
    2,
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

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, visibility_type, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
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

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, visibility_type, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
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

INSERT INTO consultation_updates_v2(id, consultation_id, is_visible_to_unanswered_users_only, update_label, update_date, share_text_template, has_questions_info, responses_info_picto, responses_info_description, responses_info_action_text,info_header_picto, info_header_description, has_participation_info, download_analysis_url, feedback_question_picto, feedback_question_title, feedback_question_description, footer_title, footer_description, goals) VALUES(
    '86a3356d-7dad-48be-8bff-368279f8be07',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    0,
    null,
    '2023-04-01 23:59:59.999',
    'Comme moi, tu peux participer à la Consultation : {title}
{url}',
    0,
    '🙌',
    '<body><b>Merci pour votre participation</b> à cette consultation !</body>',
    'Voir les premiers résultats',
    null,
    null,
    1,
    null,
    null,
    null,
    null,
    'Envie d’aller plus loin ?',
    '<body>Rendez-vous sur:<br/><ul><li><a href="https://www.mangerbouger.fr/">Manger Bouger.fr</a></li><li>...</li></ul></body>',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, visibility_type, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '07253fad-bf64-43e1-9585-9b1a7f0c6c4c',
    '86a3356d-7dad-48be-8bff-368279f8be07',
    null,
    'title',
    1,
    2,
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

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, visibility_type, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    'e9ba6b6f-e43f-474f-b89d-6f0faf0e2586',
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

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, visibility_type, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
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

INSERT INTO consultation_updates_v2(id, consultation_id, is_visible_to_unanswered_users_only, update_label, update_date, share_text_template, has_questions_info, responses_info_picto, responses_info_description, responses_info_action_text,info_header_picto, info_header_description, has_participation_info, download_analysis_url, feedback_question_picto, feedback_question_title, feedback_question_description, footer_title, footer_description, goals) VALUES(
    '6f372d50-e921-417a-8ed0-684a6c32e820',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    0,
    null,
    '2023-04-01 23:59:59.999',
    'Comme moi, tu peux participer à la Consultation : {title}
{url}',
    0,
    '🏁',
    '<body><b>Cette consultation est maintenant terminée.</b>
    Merci à tous !</body>',
    'Voir tous les résultats',
    null,
    null,
    1,
    null,
    null,
    null,
    null,
    'Envie d’aller plus loin ?',
    '<body>Rendez-vous sur:<br/><ul><li><a href="https://www.mangerbouger.fr/">Manger Bouger.fr</a></li><li>...</li></ul></body>',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, visibility_type, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    '25da8e64-e09e-4cc3-8d8d-01f63233c0f2',
    '6f372d50-e921-417a-8ed0-684a6c32e820',
    null,
    'title',
    1,
    2,
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

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, visibility_type, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
    'bfcb98ef-e0e8-4a66-b9bc-78db28d75a21',
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

INSERT INTO consultation_update_sections(id, consultation_update_id, parent_section_id, type, ordre, visibility_type, title, description, url, video_width, video_height, author_info_name, author_info_message, video_date, video_transcription) VALUES(
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
    'Fin de la consultation',
    'Consulter toutes les réponses'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_history(id, consultation_id, step_number, type, consultation_update_id, title, action_text) VALUES(
    '48b29a32-17d6-4a61-be39-0c62e33c4626',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    4,
    'update',
    null,
    'Réponse du gouvernement',
    null
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_update_history(id, consultation_id, step_number, type, consultation_update_id, title, action_text) VALUES(
    'dfef5438-5983-482f-8ee7-8a64c8de2eab',
    'f5fd9c1d-6583-494c-8b0f-78129d6a0382',
    5,
    'update',
    null,
    'Mise en oeuvre',
    null
) ON CONFLICT DO NOTHING;
