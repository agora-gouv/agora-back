INSERT INTO thematiques(id, label, picto, color) VALUES (
    '1f3dbdc6-cff7-4d6a-88b5-c5ec84c55d15',
    'Travail & emploi',
    '\ud83d\udcbc',
    '#FFCFDEFC'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto, color) VALUES (
    'bb051bf2-644b-47b6-9488-7759fa727dc0',
    'Transition écologique',
    '\ud83c\udf31',
    '#FFCFFCD9'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto, color) VALUES (
    'a4bb4b27-3271-4278-83c9-79ac3eee843a',
    'Santé',
    '\ud83e\ude7a',
    '#FFFCCFDD'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto, color) VALUES (
    'c97c3afd-1940-4b6d-950a-734b885ee5cb',
    'Economie',
    '\ud83d\udcc8',
    '#FFCFF6FC'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto, color) VALUES (
    '5b9180e6-3e43-4c63-bcb5-4eab621fc016',
    'Education',
    '\ud83c\udf93',
    '#FFFCE7CF'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto, color) VALUES (
    '8e200137-df3b-4bde-9981-b39a3d326da7',
    'International',
    '\ud83c\udf0f',
    '#FFE8CFFC'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto, color) VALUES (
    '0f644115-08f3-46ff-b776-51f19c65fdd1',
    'Transports',
    '\ud83d\ude8a',
    '#FFFCF7CF'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto, color) VALUES (
    'b276606e-f251-454e-9a73-9b70a6f30bfd',
    'Sécurité',
    '\ud83d\ude94',
    '#FFE1E7F3'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto, color) VALUES (
    '47897e51-8e94-4920-a26a-1b1e5e232e82',
    'Autre',
    '\ud83d\udce6',
    '#FFE6CCB3'
) ON CONFLICT DO NOTHING;

INSERT INTO consultations(id, title, abstract, end_date, cover_url, question_count, estimated_time, participant_count_goal, description, tips_description, thematique_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    'Développer le covoiturage',
    'Comment mutualiser au mieux les trajets pour l’environnement et le pouvoir d’achat des Français ?',
    '2024-01-23',
    'https://betagouv.github.io/agora-content/covoiturage.png',
    '5 à 10 questions',
    '5 minutes',
    100,
    '<body>La description avec textes <b>en gras</b> et potentiellement des <a href="https://google.fr">liens</a><br/><br/><ul><li>example1 <b>en gras</b></li><li>example2</li></ul></body>',
    '<body>Qui peut aussi être du texte <i>riche</i></body>',
    '0f644115-08f3-46ff-b776-51f19c65fdd1'
) ON CONFLICT (id) DO UPDATE SET cover_url = EXCLUDED.cover_url, description = EXCLUDED.description;

INSERT INTO consultation_updates(id, step, description, consultation_id) VALUES (
    '72682956-094b-423b-9086-9ec4f8ef2662',
    1,
    '<body>La description avec textes <b>en gras</b> et potentiellement des <a href="https://google.fr">liens</a><br/><br/><ul><li>example1 <b>en gras</b></li><li>example2</li></ul><br/>Dédicasse à Pénéloppe! Comme ça tu peux voir que ce texte vient bien du back 😉</body>',
    'c29255f2-10ca-4be5-aab1-801ea173337c'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea1733301',
    'quel est la fréquence d’utilisation de transport en commun?',
    1,
    'unique',
    null,
    null,
    'c29255f2-10ca-4be5-aab1-801ea173337c'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c28255f2-10ca-4be5-aab1-801ea1733301',
    'une à deux fois par semaine',
    1,
    'c29255f2-10ca-4be5-aab1-801ea1733301'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c28255f2-10ca-4be5-aab1-801ea1733302',
    'trois fois par semaine',
    2,
    'c29255f2-10ca-4be5-aab1-801ea1733301'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '78782e93-71b5-47e7-8f59-79c0ae32af48',
    'Souhaitez vous préciser davantage ?',
    2,
    'ouverte',
    null,
    null,
    'c29255f2-10ca-4be5-aab1-801ea173337c'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea1733302',
    'Comment vous motiver pour le covoiturage?',
    3,
    'unique',
    null,
    null,
    'c29255f2-10ca-4be5-aab1-801ea173337c'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c27255f2-10ca-4be5-aab1-801ea1733301',
    'en me proposant une prime',
    1,
    'c29255f2-10ca-4be5-aab1-801ea1733302'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c27255f2-10ca-4be5-aab1-801ea1733302',
    'en réduisant mes impots',
    2,
    'c29255f2-10ca-4be5-aab1-801ea1733302'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c27255f2-10ca-4be5-aab1-801ea1733303',
    'je ne veux pas le faire',
    3,
    'c29255f2-10ca-4be5-aab1-801ea1733302'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '18409cad-f31b-45cd-8285-a79c94aaf868',
    'Quel types de transports en commun utilisez-vous le plus souvent ?',
    6,
    'multiple',
    null,
    2,
    'c29255f2-10ca-4be5-aab1-801ea173337c'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9b3beb2c-6fd6-42d0-b8fb-6cea10447da2',
    'Train',
    1,
    '18409cad-f31b-45cd-8285-a79c94aaf868'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'eadaa609-2d81-4584-bcb7-fb432396a0c5',
    'Tram',
    2,
    '18409cad-f31b-45cd-8285-a79c94aaf868'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'b4dde6f7-fb75-48fd-9ec7-038adece59df',
    'Bus',
    3,
    '18409cad-f31b-45cd-8285-a79c94aaf868'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '89bd9080-0b27-417a-af13-bc2368535ddf',
    'Avez-vous des suggestions d’amélioration pour cette application ?',
    5,
    'ouverte',
    null,
    null,
    'c29255f2-10ca-4be5-aab1-801ea173337c'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '89bd9080-0b27-417a-af13-bc23685300df',
    'Ceci est un chapter',
    4,
    'chapter',
    'Ceci est la description du chapter',
    null,
    'c29255f2-10ca-4be5-aab1-801ea173337c'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    'f29c5d6f-9838-4c57-a7ec-0612145bb0c8',
    'Pour la retraite : comment est-ce qu’on aboutit au chiffre de 65 ans ?',
    'Le conseil d’orientation des retraites indique que les comptes sont à l’équilibre. A chaque nouveau président l’âge maximal change, qui choisit l’âge de 65 ans et pourquoi ? Je n’ai trouvé aucune justification concrète.',
    '2023-04-17',
    1,
    'Harry P.',
    '1f3dbdc6-cff7-4d6a-88b5-c5ec84c55d15'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    'c29255f2-10ca-4be5-aab1-801ea1733301',
    'c28255f2-10ca-4be5-aab1-801ea1733301',
    '',
    'b27bb876-d14c-4863-a23b-af6526b96968'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    '78782e93-71b5-47e7-8f59-79c0ae32af48',
    null,
    'Réponse avec champs libre',
    'b27bb876-d14c-4863-a23b-af6526b96968'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    'c29255f2-10ca-4be5-aab1-801ea1733302',
    'c27255f2-10ca-4be5-aab1-801ea1733301',
    '',
    'b27bb876-d14c-4863-a23b-af6526b96968'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    '18409cad-f31b-45cd-8285-a79c94aaf868',
    '9b3beb2c-6fd6-42d0-b8fb-6cea10447da2',
    '',
    'b27bb876-d14c-4863-a23b-af6526b96968'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    '18409cad-f31b-45cd-8285-a79c94aaf868',
    'b4dde6f7-fb75-48fd-9ec7-038adece59df',
    '',
    'b27bb876-d14c-4863-a23b-af6526b96968'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    '89bd9080-0b27-417a-af13-bc2368535ddf',
    null,
    'Merci bye bye !',
    'b27bb876-d14c-4863-a23b-af6526b96968'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    'c29255f2-10ca-4be5-aab1-801ea1733301',
    'c28255f2-10ca-4be5-aab1-801ea1733302',
    '',
    '77c27321-7c6f-41a4-afa3-052128fbbec0'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    '78782e93-71b5-47e7-8f59-79c0ae32af48',
    null,
    '2ème réponse avec champs libre',
    '77c27321-7c6f-41a4-afa3-052128fbbec0'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    'c29255f2-10ca-4be5-aab1-801ea1733302',
    'c27255f2-10ca-4be5-aab1-801ea1733303',
    '',
    '77c27321-7c6f-41a4-afa3-052128fbbec0'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    '18409cad-f31b-45cd-8285-a79c94aaf868',
    'eadaa609-2d81-4584-bcb7-fb432396a0c5',
    '',
    '77c27321-7c6f-41a4-afa3-052128fbbec0'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    '18409cad-f31b-45cd-8285-a79c94aaf868',
    'b4dde6f7-fb75-48fd-9ec7-038adece59df',
    '',
    '77c27321-7c6f-41a4-afa3-052128fbbec0'
) ON CONFLICT DO NOTHING;

INSERT INTO reponses_consultation (consultation_id, question_id, choice_id, response_text, participation_id) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    '89bd9080-0b27-417a-af13-bc2368535ddf',
    null,
    '@++',
    '77c27321-7c6f-41a4-afa3-052128fbbec0'
) ON CONFLICT DO NOTHING;

INSERT INTO supports_qag (user_id, qag_id) VALUES (
    '54f66df616565d84',
    'f29c5d6f-9838-4c57-a7ec-0612145bb0c8'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '889b41ad-321b-4338-8596-df745c546919',
    'Quand l’application AGORA sera-t-elle disponible au grand public ?',
    'Nous avons cru comprendre qu’il s’agit pour l’instant que d’une expérimentation.',
    '2023-04-1',
    1,
    'Henri J.',
    '47897e51-8e94-4920-a26a-1b1e5e232e82'
) ON CONFLICT DO NOTHING;

INSERT INTO responses_qag(id, author, author_description, response_date, video_url, transcription, qag_id) VALUES (
    'fe17ddb7-32d3-4ead-b3a0-3b6593addf47',
    'The Stormtrooper',
    'Ministre de rien du tout, porte parole du projet AGORA, ne sais pas viser juste',
    '2023-04-21',
    'https://betagouv.github.io/agora-content/QaG-Stormtrooper-Response.mp4',
    'Bonjour je suis monsieur Stormtrooper et je vais vous répondre à la question de Henry J. sur l’application AGORA. Blablabla blabla. Voilà merci !',
    '889b41ad-321b-4338-8596-df745c546919'
) ON CONFLICT DO NOTHING;
INSERT INTO feedbacks_qag (user_id, qag_id, is_helpful) VALUES (
    '54f66df616565d84',
    'f29c5d6f-9838-4c57-a7ec-0612145bb0c8',
    true
) ON CONFLICT DO NOTHING;