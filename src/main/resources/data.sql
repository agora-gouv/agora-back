INSERT INTO thematiques(id, label, picto) VALUES (
    '1f3dbdc6-cff7-4d6a-88b5-c5ec84c55d15',
    'Travail',
    '\ud83d\udcbc'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    'bb051bf2-644b-47b6-9488-7759fa727dc0',
    'Transition écologique',
    '\ud83c\udf31'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    'a4bb4b27-3271-4278-83c9-79ac3eee843a',
    'Santé',
    '\ud83c\udfe5'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    'c97c3afd-1940-4b6d-950a-734b885ee5cb',
    'Economie',
    '\ud83d\udcc8'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '5b9180e6-3e43-4c63-bcb5-4eab621fc016',
    'Education & jeunesse',
    '\ud83c\udf93'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '8e200137-df3b-4bde-9981-b39a3d326da7',
    'Europe & international',
    '\ud83c\udf0f'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '0f644115-08f3-46ff-b776-51f19c65fdd1',
    'Transports',
    '\ud83d\ude8a'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    'b276606e-f251-454e-9a73-9b70a6f30bfd',
    'Sécurité & défense',
    '\ud83d\udee1'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '30671310-ee62-11ed-a05b-0242ac120003',
    'Démocratie',
    '\uD83D\uDDF3'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '47897e51-8e94-4920-a26a-1b1e5e232e82',
    'Autre',
    '\ud83d\udce6'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '5cdb4732-0153-11ee-be56-0242ac120002',
    'Agriculture & alimentation',
    '\ud83c\udf3e'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '01c4789a-015e-11ee-be56-0242ac120002',
    'Culture',
    '\ud83c\udfad'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '0ca6f2f6-015e-11ee-be56-0242ac120002',
    'Etudes sup. & recherche',
    '\ud83e\uddec'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '175ab0b6-015e-11ee-be56-0242ac120002',
    'Outre-mer',
    '\ud83c\udf0d'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '2186bc60-015e-11ee-be56-0242ac120002',
    'Justice',
    '\u2696\ufe0f'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '2d1c72fe-015e-11ee-be56-0242ac120002',
    'Solidarités',
    '\ud83e\udd1d'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '3953a966-015e-11ee-be56-0242ac120002',
    'Autonomie',
    '\ud83d\udc75'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '41dcc98c-015e-11ee-be56-0242ac120002',
    'Handicap',
    '\ud83e\uddd1\u200d\ud83e\uddbd'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '4c379646-015e-11ee-be56-0242ac120002',
    'Sport',
    '\ud83c\udfc0'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '5531afc0-015e-11ee-be56-0242ac120002',
    'Services publics',
    '\ud83c\udfdb'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '5e6bed94-015e-11ee-be56-0242ac120002',
    'Energie',
    '\u26a1'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '73fa6438-015e-11ee-be56-0242ac120002',
    'Egalité',
    '\ud83d\udc65'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '801e3eb0-015e-11ee-be56-0242ac120002',
    'Enfance',
    '\ud83d\udc76'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO thematiques(id, label, picto) VALUES (
    '8a4e95e2-015e-11ee-be56-0242ac120002',
    'Logement',
    '\ud83c\udfe1'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, picto = EXCLUDED.picto;

INSERT INTO agora_users(id, password, fcm_token, created_date, authorization_level, is_banned) VALUES (
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45',
    '',
    '',
    '2023-04-01',
    '0',
    '0'
) ON CONFLICT DO NOTHING;