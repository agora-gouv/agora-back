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

INSERT INTO consultations(id, title, abstract, end_date, cover, question_count, estimated_time, participant_count_goal, description, tips_description, id_thematique) VALUES (
    'c29255f2-10ca-4be5-aab1-801ea173337c',
    'Développer le covoiturage',
    'Comment mutualiser au mieux les trajets pour l''environnement et le pouvoir d''achat des Français ?',
    '2024-01-23',
    '/images/covoiturage.svg',
    '5 à 10 questions',
    '5 minutes',
    30000,
    '<body>La description avec textes <b>en gras</b> et potentiellement des <a href=\"https://google.fr\">liens</a><br/><br/><ul><li>example1 <b>en gras</b></li><li>example2</li></ul></body>',
    '<body>Qui peut aussi être du texte <i>riche</i></body>',
    '0f644115-08f3-46ff-b776-51f19c65fdd1'
) ON CONFLICT DO NOTHING;