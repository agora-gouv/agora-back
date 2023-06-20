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

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    'f29c5d6f-9838-4c57-a7ec-0612145bb0c8',
    'Pourquoi avoir créé l’application Agora ?',
    'Quel est le but de l’application Agora ? A quel besoin des Français et des Françaises espérez-vous qu’elle réponde ? Que signifie son caractère expérimental ?',
    '2023-06-01',
    7,
    'Agora',
    '30671310-ee62-11ed-a05b-0242ac120003',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id, status = EXCLUDED.status;

INSERT INTO responses_qag(id, author, author_portrait_url, author_description, response_date, video_url, transcription, qag_id) VALUES (
    '38990baf-b0ed-4db0-99a5-7ec01790720e',
    'Olivier Véran',
    'https://betagouv.github.io/agora-content/QaG-OlivierVeran.png',
    'Ministre délégué auprès de la Première ministre, chargé du Renouveau démocratique, porte-parole du Gouvernement',
    '2023-06-05',
    'https://betagouv.github.io/agora-content/AgoraQagResponse.mp4',
    'Bonjour,\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
    'f29c5d6f-9838-4c57-a7ec-0612145bb0c8'
) ON CONFLICT (id) DO UPDATE SET video_url = EXCLUDED.video_url;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '996436ca-ee69-11ed-a05b-0242ac120003',
    'On parle beaucoup de l’intelligence artificielle… vous comptez faire quoi pour les emplois qu’elle risque de remplacer ?',
    'ChatGPT est arrivé avec fureur et on voit déjà combien un certain nombre de tâches et de métier pourraient être rapidement remplacés par l’intelligence artificielle… Faut-il interdire ChatGPT? Faut-il ré-orienter les travailleurs concernés ?',
    '2023-06-02',
    1,
    'Nina',
    '1f3dbdc6-cff7-4d6a-88b5-c5ec84c55d15',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    'f8776dd0-ee69-11ed-a05b-0242ac120003',
    'Pourquoi ne taxe-t-on pas le kérosène des avions ? La France doit militer pour !',
    'Je trouve ça absolument lunaire que le kérosène des avions ne soit pas taxés, alors que les carburants des autres modes de transports le sont. Il s’agit d’une mesure à prendre au niveau international : la France défend-elle cette position de pur bon sens et quand peut-on espérer avoir des résultats ?',
    '2023-06-01',
    1,
    'Elliott',
    'bb051bf2-644b-47b6-9488-7759fa727dc0',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '40c04882-ee6a-11ed-a05b-0242ac120003',
    'Peut-on refuser d’être pris en charge par un soignant non-vacciné ? Personnellement, ça me dérange.',
    'Au pays de Pasteur, je trouve ça inadmissible que les soignants non-vaccinés soient ré-intégrés dans le corps hospitalier. Est-ce possible de refuser qu’un tel soignant me soigne ? Désolée mais je ne fais pas confiance à quelqu’un qui ne croit pas à la science.',
    '2023-06-01',
    1,
    'Laure',
    'a4bb4b27-3271-4278-83c9-79ac3eee843a',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '8171c50e-ee6a-11ed-a05b-0242ac120003',
    'Quel va être l’impact de la baisse de la note de la France par l’agence de notation financière Fitch ?',
    'L’agence de notation financière Fitch a baissé la note de la France, passant de AA à AA-. Qu’est-ce que ça veut dire concrètement ? Est-ce un signal pour l’avenir ou cela a-t-il un impact financier direct ?',
    '2023-06-02',
    1,
    'Patrick',
    'c97c3afd-1940-4b6d-950a-734b885ee5cb',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '1731a370-ee6b-11ed-a05b-0242ac120003',
    'Ma question, c’est pourquoi on n’arrive pas, dans ce pays, à recruter davantage de professeurs ?!',
    'Il y a bien sûr la question de la rémunération, mais au-delà de ça, c’est aussi la position sociale et la reconnaissance même du professeur qui semble avoir “baissé” ces 20 dernières années. Comment remédier à cela ?',
    '2023-06-02',
    1,
    'Agny',
    '5b9180e6-3e43-4c63-bcb5-4eab621fc016',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '5c5f5460-ee6b-11ed-a05b-0242ac120003',
    'Aurait-on pu faire quelque chose pour éviter la guerre en Ukraine ? Et les prochaines ? Je m’interroge',
    'La guerre en Ukraine semble partie pour durer, avec les conséquences dramatiques que l’on sait. Aurait-on pu éviter cette guerre ? Peut-on faire quelque chose pour éviter qu’il y en ait d’autres ?',
    '2023-06-02',
    1,
    'Béatrice',
    '8e200137-df3b-4bde-9981-b39a3d326da7',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '96e8341c-ee6b-11ed-a05b-0242ac120003',
    'Pourquoi ne pas tester un tarif unique de transport en commun pas trop cher partout en France comme en Allemagne ?',
    'L’Allemagne a lancé en mai DeutschlandTicket à 49 euros par mois.qui permet avec un seul billet d’aller dans n’importe quel transport en commun local et régional. Ne serait-ce pas un bon coup de pouce pour le pouvoir d’achat et la promotion des transports en commun ?',
    '2023-06-01',
    1,
    'Danny',
    '0f644115-08f3-46ff-b776-51f19c65fdd1',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    'd09f2788-ee6b-11ed-a05b-0242ac120003',
    'La France devrait autoriser la reconnaissance faciale par vidéo-surveillance pour lutter contre les délits du quotidien : qu’est-ce qu’on attend ?',
    'Y-a-t-il eu des expérimentations sur ces dispositifs ? Est-ce que c’est efficace ? Est-ce que les JO vont être l’occasion de tester des choses sur ce sujet ?',
    '2023-06-02',
    1,
    'Josette',
    'b276606e-f251-454e-9a73-9b70a6f30bfd',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '1cde43fe-ee6c-11ed-a05b-0242ac120003',
    'Est-ce qu’il ne serait pas temps d’avoir enfin (!) un vrai débat sur la légalisation du cannabis, comme ils le font dans plein de pays ?',
    'On consomme beaucoup de cannabis en France, même si c’est illégal. Ne devrait-on pas avoir un débat sur une potentielle légalisation pour que cette réalité cesse de nourrir des trafics illégaux ? Arrêtons l’hypocrisie ! (Et ce serait sûrement bon pour les finances de l’Etat …)',
    '2023-06-02',
    1,
    'Tyron',
    '47897e51-8e94-4920-a26a-1b1e5e232e82',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    '59028e08-ee6c-11ed-a05b-0242ac120003',
    'Quel est le niveau de la menace terroriste sur la France aujourd’hui ?',
    'On se doute qu’il y a des attentats déjoués dont nous ne sommes pas au courant et c’est bien normal. Mais pouvez-vous nous dire par rapport aux années précédentes et des événements dans le monde actuel à quel niveau de menace nous situons-nous ?',
    '2023-06-01',
    1,
    'Kevin',
    'b276606e-f251-454e-9a73-9b70a6f30bfd',
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45'
) ON CONFLICT (id) DO UPDATE SET user_id = EXCLUDED.user_id;

INSERT INTO consultations(id, title, end_date, cover_url, question_count, estimated_time, participant_count_goal, description, tips_description, thematique_id) VALUES (
    '6d85522a-ee71-11ed-a05b-0242ac120003',
    'Covoiturage, roulons ensemble',
    '2023-07-04',
    'https://betagouv.github.io/agora-content/covoiturage.png',
    '12 questions',
    '5 minutes',
    10000,
    '<body>Le Gouvernement a lancé un plan national pour faciliter le covoiturage au quotidien : son objet est de tripler le nombre de trajets en covoiturage du quotidien d’ici 2027 pour atteindre les 3 millions de trajet réalisés par jour.<br/><br/>Le covoiturage est un enjeu majeur pour :<br/><br/><ul><li><b>L’amélioration de la qualité de l’air et la baisse des gaz à effet de serre</b>. Le partage d’une voiture divise par 2 les émissions de son trajet. Si l’objectif est atteint, 4.5 millions de tonnes de CO2 par an peuvent être économisées (environ 1% des émissions françaises).</li><li><b>Le pouvoir d’achat</b>. Un covoiturage quotidien pour se rendre sur son lieu de travail à 30 km permet une économie de près de 2000 euros chaque année.</li><li><b>Se déplacer plus librement.</b> Le covoiturage, c’est un moyen de  se déplacer plus facilement là où il n’y a pas de transports en commun mais aussi pour ceux qui n’ont pas de voiture ou ne peuvent pas conduire.</li></ul><br/><br/><a href="https://www.ecologie.gouv.fr/covoiturage">Sources </a>(https://www.ecologie.gouv.fr/covoiturage)</body>',
    '<body>🗣 Consultation proposée par le <b>Ministère des Transports</b><br/><br/>🎯<b> Objectif</b> : évaluer et améliorer le plan national covoiturage <br/><br/>🚀<b>Axe gouvernemental</b> : Planifier et accélérer la transition écologique</body>',
    '0f644115-08f3-46ff-b776-51f19c65fdd1'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title, end_date = EXCLUDED.end_date, question_count = EXCLUDED.question_count, participant_count_goal = EXCLUDED.participant_count_goal, description = EXCLUDED.description, tips_description = EXCLUDED.tips_description;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'e271ed7a-ef05-11ed-a05b-0242ac120003',
    'Avez-vous une voiture ?',
    1,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f5dd076e-ef05-11ed-a05b-0242ac120003',
    'Oui',
    1,
    'e271ed7a-ef05-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '0fd15904-ef06-11ed-a05b-0242ac120003',
    'Non',
    2,
    'e271ed7a-ef05-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '0fd15944-ef06-11ed-a05b-0242ac120003',
    'Je ne souhaite pas répondre',
    3,
    'e271ed7a-ef05-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '48d3c502-ef06-11ed-a05b-0242ac120003',
    'Dans quel cadre utilisez-vous principalement votre voiture ?',
    2,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '84f7f70e-ef08-11ed-a05b-0242ac120003',
    'Déplacement quotidien domicile-travail',
    1,
    '48d3c502-ef06-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '84f7f70e-ef08-11ed-a05b-0242ac120010',
    'Déplacement quotidien hors domicile-travail (ex : école / courses)',
    2,
    '48d3c502-ef06-11ed-a05b-0242ac120003'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '84f7f70e-ef08-11ed-a05b-0242ac120011',
    'Déplacement pour motif professionnel (hors du lieu habituel de travail)',
    3,
    '48d3c502-ef06-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '84f7f70e-ef08-11ed-a05b-0242ac120012',
    'Déplacement occasionnel (loisir et vacances)',
    4,
    '48d3c502-ef06-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '84f7f70e-ef08-11ed-a05b-0242ac120013',
    'Je ne suis pas concerné(e)',
    5,
    '48d3c502-ef06-11ed-a05b-0242ac120003'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'e52c5868-ef08-11ed-a05b-0242ac120003',
    'Avez-vous déjà fait du covoiturage ? Que ce soit en tant que conducteur ou passager.',
    3,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '1b52a816-ef09-11ed-a05b-0242ac120003',
    'Oui',
    1,
    'e52c5868-ef08-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '1b52a816-ef09-11ed-a05b-0242ac120002',
    'Non',
    2,
    'e52c5868-ef08-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '1b52a816-ef09-12ed-a05b-0242ac120002',
    'Je ne souhaite pas répondre',
    3,
    'e52c5868-ef08-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '867b767a-0905-11ee-be56-0242ac120002',
    'Êtes-vous prêt à covoiturer en tant que …',
    4,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9c3eba80-0905-11ee-be56-0242ac120002',
    'Conducteur',
    1,
    '867b767a-0905-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'a3e959f2-0905-11ee-be56-0242ac120002',
    'Passager',
    2,
    '867b767a-0905-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ab7d336e-0905-11ee-be56-0242ac120002',
    'Les deux',
    3,
    '867b767a-0905-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ab7d136e-0905-11ee-be56-0242ac120002',
    'Aucun',
    4,
    '867b767a-0905-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac7d336e-0905-11ee-be56-0242ac120002',
    'Je ne sais pas',
    5,
    '867b767a-0905-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120015',
    'Quelle est votre principale difficulté pour recourir au covoiturage ? (conducteur et/ou passager)',
    5,
    'open',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT (id) DO UPDATE SET type = EXCLUDED.type;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '4c85adfe-0906-11ee-be56-0242ac120002',
    'A quelle fréquence pourriez-vous recourir au covoiturage ?',
    6,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5f30b336-0906-11ee-be56-0242ac120002',
    'Tous les jours',
    1,
    '4c85adfe-0906-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5f30b336-0906-11ee-be56-0242ac120001',
    'Plusieurs fois par semaine',
    2,
    '4c85adfe-0906-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5f30b336-0906-11ee-be56-0242ac120003',
    'Plusieurs fois par mois',
    3,
    '4c85adfe-0906-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5f30b336-0906-10ee-be56-0242ac120003',
    'De manière occasionnelle',
    4,
    '4c85adfe-0906-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5f30b336-0906-18ee-be56-0242ac120003',
    'Je ne suis pas concerné(e)',
    5,
    '4c85adfe-0906-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'cea510c2-0906-11ee-be56-0242ac120002',
    'Pour quel usage principalement ?',
    7,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '6ed88358-0907-11ee-be56-0242ac120002',
    'Déplacement quotidien domicile-travail',
    1,
    'cea510c2-0906-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '6ed88358-0907-11ee-be56-0242ac120010',
    'Déplacement quotidien école / courses',
    2,
    'cea510c2-0906-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '6ed88358-0907-11ee-be56-0242ac120005',
    'Déplacement pour motif professionnel (hors du lieu habituel de travail)',
    3,
    'cea510c2-0906-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '6ed88358-0907-11ee-be56-0242ac120033',
    'Déplacement occasionnel (loisir et vacances)',
    4,
    'cea510c2-0906-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '6ed88358-0907-11ee-be56-0242ac120011',
    'Aucun',
    5,
    'cea510c2-0906-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '6ed00358-0907-11ee-be56-0242ac120011',
    'Je ne sais pas',
    6,
    'cea510c2-0906-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '91c3411c-ef0a-11ed-a05b-0242ac120003',
    'Parmi les mesures proposées, quelle est la ou les deux mesures qui vous convaincrai(en)t le plus pour recourir au covoiturage ?',
    8,
    'multiple',
    null,
    2,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120003',
    'Des places de stationnement au départ et à l’arrivée',
    1,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120010',
    'Des voies sur les routes ou les autoroutes réservées au covoiturage',
    2,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120011',
    'Une incitation financière pour le conducteur',
    3,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120012',
    'Une incitation financière pour le passager',
    4,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120013',
    'Une plateforme unique de mise en relation sur mon territoire',
    5,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24c428-ef0a-11ed-a05b-0242ac120013',
    'Des arrêts d’autostop organisé le long d’un axe routier (ligne de covoiturage)',
    6,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac127713',
    'Un accompagnement et de l’information sur le fonctionnement et les aides au covoiturage',
    7,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-88ed-a05b-0242ac120013',
    'L’intégration du covoiturage dans une offre plus large de transports (horaires et points de contacts proches des transports en commun)',
    8,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-88ed-a05b-0002ac120013',
    'Aucun',
    9,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-88ed-a10b-0242ac120013',
    'Je ne sais pas',
    10,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'a3ae519c-ef09-11ed-a05b-0242ac120003',
    'Nous allons vous présenter des solutions qui existent pour favoriser le covoiturage et recueillir votre avis.',
    9,
    'chapter',
    '<body>👉Les <b>infrastructures</b> pour favoriser le covoiturage se développent partout en France : voies réservées sur routes et autoroutes, lignes de covoiturage (arrêts d’auto-stop organisé le long d’un axe routier) ou encore aires de covoiturage clairement identifiées pour prendre ou déposer des passagers.</body>',
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '9197bab0-0909-11ee-be56-0242ac120002',
    'Connaissez-vous des infrastructures dédiées au covoiturage proche de vous ?',
    10,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9f724178-0909-11ee-be56-0242ac120002',
    'Oui',
    1,
    '9197bab0-0909-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9f724178-0909-11ee-be56-0242ac120000',
    'Non',
    2,
    '9197bab0-0909-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9f724178-0909-11ee-be56-0242ac120011',
    'Je ne sais pas',
    3,
    '9197bab0-0909-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'f2aa628a-0909-11ee-be56-0242ac120002',
    'Nous allons vous présenter des solutions qui existent pour favoriser le covoiturage et recueillir votre avis.',
    11,
    'chapter',
    '<body>👉 Les <b>voies dédiées au covoiturage sur routes et autoroutes</b> pour limiter la pollution consistent à réserver ces voies aux seuls véhicules avec 2 personnes minimum à bord.</body>',
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '580bd3c0-090a-11ee-be56-0242ac120002',
    'Seriez-vous prêt à accepter ce traitement différencié ?',
    12,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '66bab292-090a-11ee-be56-0242ac121111',
    'Oui',
    1,
    '580bd3c0-090a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '66bab292-090a-11ee-be56-0242ac120002',
    'Non',
    2,
    '580bd3c0-090a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '66bab292-090a-11ee-be56-0242ac122222',
    'Je ne sais pas',
    3,
    '580bd3c0-090a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '8bdfcbde-090a-11ee-be56-0242ac120002',
    'Nous allons vous présenter des solutions qui existent pour favoriser le covoiturage et recueillir votre avis.',
    13,
    'chapter',
    '<body>👉Des <b>incitations financières</b> à la réalisation de trajets en covoiturage existent : une prime gouvernementale offre 100€ aux nouveaux conducteurs qui effectuent 10 premiers trajets en covoiturage depuis le 1er janvier 2023, les employeurs peuvent verser une aide mobilité jusqu’à 800€ par an aux salariés qui covoiturent pour se rendre au travail, certaines mairies ou régions permettent aux passagers de covoiturer gratuitement sur le territoire et/ou subventionnent les conducteurs.</body>',
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'd6cba992-090a-11ee-be56-0242ac120002',
    'Connaissez-vous ces incitations ?',
    14,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'e615de2c-090a-11ee-be56-0242ac120002',
    'Oui',
    1,
    'd6cba992-090a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'e615de2c-090a-11ee-be56-0242ac120003',
    'Non',
    2,
    'd6cba992-090a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '1f0d4940-090b-11ee-be56-0242ac120002',
    'Avez-vous déjà bénéficié d’incitations financières au covoiturage ?',
    15,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5453e924-090b-11ee-be56-0242ac120002',
    'Oui',
    1,
    '1f0d4940-090b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5453e924-090b-11ee-be56-0242ac120001',
    'Non',
    2,
    '1f0d4940-090b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5453e924-090b-11ee-be56-0242ac120010',
    'Je ne sais pas',
    3,
    '1f0d4940-090b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120020',
    'Avez-vous d’autres idées pour encourager le covoiturage en France ?',
    16,
    'open',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT (id) DO UPDATE SET type = EXCLUDED.type;

INSERT INTO consultation_updates(id, step, description, consultation_id) VALUES (
    '72682956-094b-423b-9086-9ec4f8ef2662',
    1,
    '<body>👉 Le Ministère des transports recevra les résultats et en tirera les enseignements pour la suite et les actions qui découleront de vos réponses <b>à l’occasion des 6 mois du plan covoiturage du gouvernement</b>.<br/><br/>Il s’agira notamment de :<br/><ul><li>Faire un <b>premier bilan</b> des engagements pris grâce au plan,</li><li>Mettre en place de <b>nouvelles actions</b> pour encourager le covoiturage</li></ul><br/>—<br/><br/><b>🚗 Envie d’aller plus loin ?</b><br/>Rendez-vous ici (<a href="https://www.ecologie.gouv.fr/covoiturage">https://www.ecologie.gouv.fr/covoiturage</a>) pour voir comment vous lancer et en savoir plus sur le covoiturage et ses enjeux.</body>',
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT (id) DO UPDATE SET description = EXCLUDED.description;

INSERT INTO consultations(id, title, end_date, cover_url, question_count, estimated_time, participant_count_goal, description, tips_description, thematique_id) VALUES (
    '98a8ba56-0923-11ee-be56-0242ac120002',
    'Participation citoyenne : une appli, vos idées',
    '2023-07-01',
    'https://betagouv.github.io/agora-content/education.png',
    '12 questions',
    '10 minutes',
    10000,
    '<body>En France, comme dans de nombreuses démocraties, l’abstention augmente, élection après élection. De nombreux citoyens ne font pas confiance aux responsables politiques, aux élus ou encore à l’Etat pour améliorer leur quotidien.<br/><br/>Pour tenter de rétablir ce lien de confiance, le Gouvernement compte, depuis juillet 2022, un ministre délégué auprès de la Première ministre chargé du <b>renouveau démocratique</b>, Olivier Véran.<br/><br/>Son rôle consiste notamment à <b>développer les démarches de participation citoyenne</b>, pour permettre à chaque citoyen de mieux contribuer à la décision politique.<br/><br/>Cette appli mobile, que vous venez de télécharger, s’inscrit dans cet objectif. Elle est complémentaire de démarches dites « délibératives » en présentiel, comme par exemple, la Convention citoyenne sur la fin de vie ou la concertation nationale sur le mix énergétique.<br/><br/>Avec cette consultation, donnez-nous votre avis pour nous aider à aller plus loin !</body>',
    '<body>🗣 Consultation proposée par le <b>Ministre du Renouveau Démocratique</b><br/><br/>🎯<b> Objectif</b> : améliorer et co-construire cette application avec les citoyens ; développer la participation citoyenne et faire reculer l’abstention à partir de vos idées <br/><br/>🚀<b>Axe gouvernemental</b> : Renforcer l’ordre républicain et encourager l’engagement</body>',
    '30671310-ee62-11ed-a05b-0242ac120003'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title, end_date = EXCLUDED.end_date, question_count = EXCLUDED.question_count, participant_count_goal = EXCLUDED.participant_count_goal, description = EXCLUDED.description, tips_description = EXCLUDED.tips_description;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'e65beea0-0926-11ee-be56-0242ac120002',
    'Suivez-vous l’actualité politique ?',
    1,
    'unique',
    null,
    null,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '092557b4-0927-11ee-be56-0242ac120002',
    'Au quotidien',
    1,
    'e65beea0-0926-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '092557b4-0927-11ee-be56-0242ac120001',
    'De temps en temps',
    2,
    'e65beea0-0926-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '092557b4-0927-11ee-be56-0242ac120003',
    'Uniquement avant une élection',
    3,
    'e65beea0-0926-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
     '092557b4-0927-11ee-be56-0242ac120010',
     'Jamais',
     4,
     'e65beea0-0926-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'abe8c6b0-0928-11ee-be56-0242ac120002',
    'Avez-vous l’impression que les décisions politiques ont un impact sur votre quotidien ?',
    2,
    'unique',
    null,
    null,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c2ad3e1c-0928-11ee-be56-0242ac120002',
    'Oui, plutôt en bien',
    1,
    'abe8c6b0-0928-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c2ad3e1c-0928-11ee-be56-0242ac120011',
    'Oui, plutôt en mal',
    2,
    'abe8c6b0-0928-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c2ad3e1c-0928-11ee-be56-0242ac120012',
    'Non',
    3,
    'abe8c6b0-0928-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
     'c2ad3e1c-0928-11ee-be56-0242ac121002',
     'Je ne sais pas',
     4,
     'abe8c6b0-0928-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '2c154084-0929-11ee-be56-0242ac120002',
    'Pour vous, le renouveau démocratique passe d’abord par… ?',
    3,
    'multiple',
    null,
    3,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
     '43f979ae-0929-11ee-be56-0242ac120002',
     'Un changement de pratiques politiques et/ou de profils des élus',
     1,
     '2c154084-0929-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
     '43f979ae-0929-11ee-be56-0242ac120102',
     'Davantage de participation des citoyens aux décisions politiques',
     2,
     '2c154084-0929-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
     '43f979ae-0929-11ee-be56-0242ac121002',
     'Une réforme de nos institutions',
     3,
     '2c154084-0929-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
     '43f979ae-0929-11ee-be56-0242ac121102',
     'Autre',
     4,
     '2c154084-0929-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '7763aeda-092a-11ee-be56-0242ac120002',
    'Avez-vous des idées ou propositions pour renouveler la démocratie en France ?',
    4,
    'open',
    null,
    null,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET type = EXCLUDED.type;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'dab3dc58-092a-11ee-be56-0242ac120002',
    'Avez-vous déjà participé à une consultation citoyenne ? (en ligne ou en présentiel)',
    5,
    'unique',
    null,
    null,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '0b7b992a-092b-11ee-be56-0242ac120002',
    'Oui',
    1,
    'dab3dc58-092a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '0b7b992a-092b-11ee-be56-0242ac120001',
    'Non',
    2,
    'dab3dc58-092a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '0b7b992a-092b-11ee-be56-0242ac120202',
    'Je ne sais pas ce que c’est',
    3,
    'dab3dc58-092a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '40b7d25c-092b-11ee-be56-0242ac120002',
    'Pour vous, recueillir plus souvent l’avis des citoyens pendant la préparation d’une réforme est :',
    6,
    'unique',
    null,
    null,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5a0071ec-092b-11ee-be56-0242ac120002',
    'Inutile',
    1,
    '40b7d25c-092b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5a0071ec-092b-11ee-be56-0242ac120001',
    'Utile, mais non prioritaire',
    2,
    '40b7d25c-092b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5a0071ec-092b-11ee-be56-0242ac120000',
    'Utile et prioritaire',
    3,
    '40b7d25c-092b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5a0071ec-092b-11ee-be56-0242ac110000',
    'Je ne sais pas',
    4,
    '40b7d25c-092b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'd4df6396-092b-11ee-be56-0242ac120002',
    'Pensez-vous que cela ait un impact sur la décision politique ?',
    7,
    'unique',
    null,
    null,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ea68bafa-092b-11ee-be56-0242ac120002',
    'Le plus souvent',
    1,
    'd4df6396-092b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ea68bafa-092b-11ee-be56-0242ac120001',
    'Parfois',
    2,
    'd4df6396-092b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ea68bafa-092b-11ee-be56-0242ac120000',
    'Jamais',
    3,
    'd4df6396-092b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ea68bafa-092b-11ee-be56-0242ac120102',
    'Je ne sais pas',
    4,
    'd4df6396-092b-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'b9e813c0-092c-11ee-be56-0242ac120002',
    'Pouvoir s’exprimer sur les grands projets et réformes du Gouvernement via une application dédiée comme celle que vous venez de télécharger ; est-ce une bonne idée selon vous ?',
    8,
    'unique',
    null,
    null,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'd97f36a0-092c-11ee-be56-0242ac120002',
    'Oui',
    1,
    'b9e813c0-092c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'd97f36a0-092c-11ee-be56-0242ac120001',
    'Non',
    2,
    'b9e813c0-092c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'd97f36a0-092c-11ee-be56-0242ac120003',
    'Je ne sais pas',
    3,
    'b9e813c0-092c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '4343c204-092d-11ee-be56-0242ac120002',
    'Sur quels sujets aimeriez-vous être consultés en priorité ?',
    9,
    'multiple',
    null,
    3,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d60-0932-11ee-be56-0242ac120002',
    'Agriculture & alimentation',
    1,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d60-0932-11ee-be56-0242ac121002',
    'Autonomie',
    2,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d60-0932-11ee-be56-0242ac120302',
    'Culture',
    3,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d60-0932-11ee-be56-0242ac120402',
    'Démocratie',
    4,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d60-0932-11ee-be56-0242ac120502',
    'Economie',
    5,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d60-0832-11ee-be56-0242ac120002',
    'Education & jeunesse',
    6,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d60-0732-11ee-be56-0242ac120002',
    'Egalité',
    7,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d60-0232-11ee-be56-0242ac120002',
    'Energie',
    8,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d60-0032-11ee-be56-0242ac120002',
    'Enfance',
    9,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d61-0932-11ee-be56-0242ac120002',
    'Etudes sup. & recherche',
    10,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d62-0932-11ee-be56-0242ac120002',
    'Europe & international',
    11,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d63-0932-11ee-be56-0242ac120002',
    'Handicap',
    12,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d65-0932-11ee-be56-0242ac120002',
    'Justice',
    13,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d68-0932-11ee-be56-0242ac120002',
    'Logement',
    14,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8000-0932-11ee-be56-0242ac120002',
    'Outre-mer',
    15,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '1f3a8d60-0932-11ee-be56-0242ac120002',
    'Santé',
    16,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '0f3a8d60-0932-11ee-be56-0242ac120002',
    'Sécurité & défense',
    17,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3f3a8d60-0932-11ee-be56-0242ac120002',
    'Services publics',
    18,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f1a8d60-0932-11ee-be56-0242ac120002',
    'Solidarités',
    19,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f2a8d60-0932-11ee-be56-0242ac120002',
    'Sport',
    20,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f8a8d60-0932-11ee-be56-0242ac120002',
    'Transition écologique',
    21,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f9a8d60-0932-11ee-be56-0242ac120002',
    'Transports',
    22,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2f3a8d60-0992-11ee-be56-0242ac120002',
    'Travail',
    23,
    '4343c204-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'b7c7eccc-092d-11ee-be56-0242ac120002',
    'Sur cette application, le Gouvernement prend l’engagement de répondre chaque semaine à une question posée par les citoyens (celle qui obtient le plus de votes) ; est-ce une bonne idée ?',
    10,
    'unique',
    null,
    null,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f130db54-092d-11ee-be56-0242ac120002',
    'Oui',
    1,
    'b7c7eccc-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f130db54-092d-11ee-be56-0242ac120001',
    'Non',
    2,
    'b7c7eccc-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f130db54-092d-11ee-be56-0242ac120202',
    'Je ne sais pas',
    3,
    'b7c7eccc-092d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '5563aeda-092a-11ee-be56-0242ac120002',
    'Qu’attendez-vous en priorité des réponses que feront les ministres à ces questions ?',
    11,
    'open',
    null,
    null,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET type = EXCLUDED.type;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '6363aeda-092a-11ee-be56-0242ac120002',
    'Pourquoi avez-vous téléchargé cette appli et à quoi, selon vous, doit-elle principalement servir ? C’est la dernière question de cette consultation : partagez-nous toutes vos idées !',
    12,
    'open',
    null,
    null,
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title,  type = EXCLUDED.type;

INSERT INTO consultation_updates(id, step, description, consultation_id) VALUES (
    '68682956-094b-423b-9086-9ec4f8ef2662',
    1,
    '<body>👉 Le Ministre du Renouveau démocratique, après lecture et analyse de l’ensemble de vos contributions, reviendra vers vous, directement sur cette appli, pour vous partager les mesures que le Gouvernement entend mettre en œuvre pour :<br/><br/><ul><li><b>Améliorer cette application</b></li><br/><br/><li><b>Continuer à développer la participation citoyenne</b></li><br/><br/>—<br/><br/>💡 <b>Envie d’aller plus loin ?</b><br/><br/>Rendez-vous <a href="https://www.participation-citoyenne.gouv.fr/">ici</a> <b> (participation-citoyenne.gouv.fr)</b> pour en savoir plus sur les dispositifs de participation citoyenne mis en place de façon volontaire par l’Etat.</br></br>Et pour nous partager vos retours, critiques et idées pour améliorer l’appli, rendez-vous sur votre page profil.</body>',
    '98a8ba56-0923-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET description = EXCLUDED.description;

INSERT INTO consultations(id, title, end_date, cover_url, question_count, estimated_time, participant_count_goal, description, tips_description, thematique_id) VALUES (
    'c342e83e-0b5a-11ee-be56-0242ac120002',
    'Vivre mieux en 2050 : comment s’adapter au changement climatique ?',
    '2023-09-15',
    'https://betagouv.github.io/agora-content/climate.jpeg',
    '11 questions',
    '10 minutes',
    10000,
    '<body>Comment s’assurer de vivre mieux, ensemble, dans la France de 2050 ? Atténuation et adaptation sont les deux volets indispensables et complémentaires de la politique climatique.<br/><ul><li><b>Atténuation </b>: agir sur les causes du changement climatique pour en limiter l’ampleur, comme réduire les émissions de gaz à effet de serre ou augmenter les puits de gaz à effet de serre (sols, forêts, produits bois, zones humides…).</li><br/><br/><li><b>Adaptation </b>: anticiper et limiter les conséquences du changement climatique sur les populations, les activités humaines et la nature, en intervenant, par exemple, sur l’urbanisation.</li></ul><br/><br/>L’<b>Accord de Paris</b> fixe l’objectif de rester bien <b>en-dessous de 2 degrés de réchauffement mondial</b> par rapport à l’ère pré-industrielle et de poursuivre les efforts pour ne pas dépasser 1.5 degrés. Cependant nous ne sommes collectivement pas sur la bonne trajectoire : les émissions mondiales de gaz à effet de serre continuent d’augmenter, certes bien moins vite que dans le passé, alors qu’il faudrait qu’elles baissent rapidement et fortement pour freiner le changement climatique.<br/><br/>Si l’atteinte des objectifs de l’Accord de Paris reste notre priorité et notre combat, les engagements pris par les Etats lors des COP climat et les politiques en place nous conduisent <b>vers un réchauffement mondial de l’ordre de 3°C, correspondant à +4°C en France.</b><br/><br/>Nous devons nous préparer, concrètement, aux effets inévitables du changement climatique sur notre territoire et sur nos vies. Cette consultation vise à fournir un premier aperçu des conséquences du réchauffement climatique et des choix d’adaptation qu’il induit.</body>',
    '<body>🗣 Consultation proposée par le <b>Ministère de la Transition écologique et de la Cohésion des territoires</b><br/><br/>🎯<b> Objectif</b> : adapter la France aux conséquences du réchauffement climatique <br/><br/>🚀<b>Axe gouvernemental</b> : Planifier et accélérer la transition écologique</body>',
    'bb051bf2-644b-47b6-9488-7759fa727dc0'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title, end_date = EXCLUDED.end_date, question_count = EXCLUDED.question_count, participant_count_goal = EXCLUDED.participant_count_goal, description = EXCLUDED.description, tips_description = EXCLUDED.tips_description;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'ccc9eb30-0b5c-11ee-be56-0242ac120002',
    'Avez-vous le sentiment que votre vie quotidienne a déjà changé à cause du réchauffement climatique ?',
    1,
    'unique',
    null,
    null,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '4358eb48-0b5d-11ee-be56-0242ac120002',
    'Oui, beaucoup',
    1,
    'ccc9eb30-0b5c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '4358eb48-0b5d-11ee-be56-0242ac120001',
    'Oui, un peu',
    2,
    'ccc9eb30-0b5c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '4358eb48-0b5d-11ee-be56-0242ac120003',
    'Non, pas vraiment',
    3,
    'ccc9eb30-0b5c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '4358eb48-0b5d-11ee-be56-0242ac120004',
    'Pas du tout',
    4,
    'ccc9eb30-0b5c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'df7524a6-0b5d-11ee-be56-0242ac120002',
    'A quelles conséquences du réchauffement climatique avez-vous déjà été confronté ?',
    2,
    'multiple',
    null,
    8,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET max_choices = EXCLUDED.max_choices;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ffc3fcfa-0b5d-11ee-be56-0242ac120002',
    'Canicule',
    1,
    'df7524a6-0b5d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ffc3fcfa-0b5d-11ee-be56-0242ac120001',
    'Restriction d’eau',
    2,
    'df7524a6-0b5d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ffc3fcfa-0b5d-11ee-be56-0242ac120030',
    'Sécheresse des sols',
    3,
    'df7524a6-0b5d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ffc3fcfa-0b5d-11ee-be56-0242ac120006',
    'Incendie',
    4,
    'df7524a6-0b5d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ffc3fcfa-0b5d-11ee-be56-0242ac120010',
    'Inondation',
    5,
    'df7524a6-0b5d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ffc3fcfa-0b5d-11ee-be56-0242ac120020',
    'Montée du niveau de la mer',
    6,
    'df7524a6-0b5d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ffc3fcfa-0b5d-11ee-be56-0242ac120022',
    'Recul de l’enneigement',
    7,
    'df7524a6-0b5d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ffc3fcfa-0b5d-11ee-be56-0242ac120032',
    'Autre',
    8,
    'df7524a6-0b5d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ffc3fcfa-0b5d-eeee-be56-0242ac120032',
    'Aucune',
    9,
    'df7524a6-0b5d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '97a30002-0b5e-11ee-be56-0242ac120002',
    'Selon vous, que faudrait-il changer, en priorité, dans votre quartier pour limiter les conséquences du réchauffement climatique dans votre quotidien ?',
    3,
    'open',
    null,
    null,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET type = EXCLUDED.type;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '22e736a4-0b5e-11ee-be56-0242ac122222',
    'En cas d’évènement climatique extrême, et si les usages des moyens de transports devaient être hiérarchisés, êtes-vous prêt à réduire votre liberté de déplacement afin de privilégier l’approvisionnement des magasins et des hôpitaux ?',
    4,
    'unique',
    null,
    null,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'a9cf0d70-0b5e-11ee-be56-0242ac120002',
    'Oui',
    1,
    '22e736a4-0b5e-11ee-be56-0242ac122222'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, ordre = EXCLUDED.ordre, question_id = EXCLUDED.question_id;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'a9cf0d70-0b5e-11ee-be56-0242ac120022',
    'Non',
    2,
    '22e736a4-0b5e-11ee-be56-0242ac122222'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, ordre = EXCLUDED.ordre, question_id = EXCLUDED.question_id;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'a9cf0d70-0b5e-11ee-be56-0242ac120222',
    'Je ne sais pas',
    3,
    '22e736a4-0b5e-11ee-be56-0242ac122222'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, ordre = EXCLUDED.ordre, question_id = EXCLUDED.question_id;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '8cc590cc-0b5f-11ee-be56-0242ac120002',
    'Selon vous, il faudrait :',
    5,
    'unique',
    null,
    null,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9aecb9dc-0b5f-11ee-be56-0242ac120002',
    'Accepter que certains trajets soient plus longs ou ralentis',
    1,
    '8cc590cc-0b5f-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9aecb9dc-0b5f-11ee-be56-0242ac120022',
    'Maintenir un service de transport maximal en pariant sur l’innovation technologique, quitte à consentir à un coût du transport plus élevé',
    2,
    '8cc590cc-0b5f-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9aecb9dc-0b5f-11ee-be56-0242ac120222',
    'Je ne sais pas',
    3,
    '8cc590cc-0b5f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'f2e736a4-0b5e-11ee-be56-0242ac120002',
    'Pensez-vous que, à l’avenir, les conditions climatiques (chaleur, risque d’inondation, etc.) seront un élément déterminant pour choisir votre lieu de vie ?',
    6,
    'unique',
    null,
    null,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '129af8aa-0b5f-11ee-be56-0242ac120002',
    'Oui',
    1,
    'f2e736a4-0b5e-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '129af8aa-0b5f-11ee-be56-0242ac120022',
    'Non',
    2,
    'f2e736a4-0b5e-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '129af8aa-0b5f-11ee-be56-0242ac122222',
    'Je ne sais pas',
    3,
    'f2e736a4-0b5e-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET ordre = EXCLUDED.ordre;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'cac40a48-0b5f-11ee-be56-0242ac120002',
    'Selon vous, il faudrait plutôt :',
    7,
    'unique',
    null,
    null,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title, ordre = EXCLUDED.ordre;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'd8672e00-0b5f-11ee-be56-0242ac120002',
    'Protéger les logements situés en bord du littoral contre la hausse du niveau de la mer',
    1,
    'cac40a48-0b5f-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'd8672e00-0b5f-11ee-be56-0242ac120022',
    'Accepter que certains logements soient détruits au fur et à mesure de la hausse du niveau de la mer, le cas échéant en prévoyant des solutions de logement alternative',
    2,
    'cac40a48-0b5f-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'd8672e00-0b5f-11ee-be56-0242ac120222',
    'Je ne sais pas',
    3,
    'cac40a48-0b5f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '15973d9c-0b60-11ee-be56-0242ac120002',
    'Selon vous, il faudrait plutôt :',
    8,
    'multiple',
    null,
    3,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title, ordre = EXCLUDED.ordre, type = EXCLUDED.type, max_choices = EXCLUDED.max_choices;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2ba34d4c-0b60-11ee-be56-0242ac120002',
    'Privilégier le télétravail quand c’est possible',
    1,
    '15973d9c-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2ba34d4c-0b60-11ee-be56-0242ac120022',
    'Adapter l’environnement de travail pour tous quel que soit le prix',
    2,
    '15973d9c-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2ba34d4c-0b60-11ee-be56-0242ac120222',
    'Adapter l’environnement de travail uniquement pour ceux qui ne peuvent pas télétravailler',
    3,
    '15973d9c-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2ba34d4c-0b60-11ee-be56-0242ac122222',
    'Les deux : télétravail et adaptation, peu importe le prix',
    4,
    '15973d9c-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '129af8aa-0b5f-11ee-be56-0242ac120222',
    'Je ne sais pas',
    5,
    '15973d9c-0b60-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET label = EXCLUDED.label, ordre = EXCLUDED.ordre, question_id = EXCLUDED.question_id;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '871f4716-0b60-11ee-be56-0242ac120002',
    'Seriez-vous prêt à travailler systématiquement en horaires décalés (commencer très tôt le matin ou finir très tard le soir, en évitant les pics de chaleur de la mi-journée) en cas de vagues de chaleur ?',
    9,
    'unique',
    null,
    null,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title, ordre = EXCLUDED.ordre;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9e880816-0b60-11ee-be56-0242ac120002',
    'Oui, tout à fait',
    1,
    '871f4716-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9e880816-0b60-11ee-be56-0242ac120022',
    'Oui, plutôt',
    2,
    '871f4716-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9e880816-0b60-11ee-be56-0242ac120012',
    'Non',
    3,
    '871f4716-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '9e880816-0b60-11ee-be56-0242ac120202',
    'Je ne sais pas',
    4,
    '871f4716-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'db22171c-0b60-11ee-be56-0242ac120002',
    'Faut-il adapter le rythme des vacances scolaires aux évolutions du climat ?',
    10,
    'unique',
    null,
    null,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET ordre = EXCLUDED.ordre;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '1532370c-0b61-11ee-be56-0242ac120002',
    'Oui, tout à fait',
    1,
    'db22171c-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '1632370c-0b61-11ee-be56-0242ac120002',
    'Oui, plutôt',
    2,
    'db22171c-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2532370c-0b61-11ee-be56-0242ac120002',
    'Non',
    3,
    'db22171c-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '5532370c-0b61-11ee-be56-0242ac120002',
    'Je ne sais pas',
    4,
    'db22171c-0b60-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '4289e362-0b61-11ee-be56-0242ac120002',
    'Seriez-vous d’accord pour que la forêt que nous connaissons, qui souffre du réchauffement climatique, change de visage avec des essences d’arbre plus adaptées au changement climatique ?',
    11,
    'unique',
    null,
    null,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET ordre = EXCLUDED.ordre;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '8bdf40ac-0b61-11ee-be56-0242ac120002',
    'Oui, tout à fait',
    1,
    '4289e362-0b61-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '8bdf40ac-0b61-11ee-be56-0242ac120022',
    'Oui, plutôt',
    2,
    '4289e362-0b61-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '8bdf40ac-0b61-11ee-be56-0242ac120222',
    'Non',
    3,
    '4289e362-0b61-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '8bdf40ac-0b61-11ee-be56-0242ac122222',
    'Je ne sais pas',
    4,
    '4289e362-0b61-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'c0cf651c-0b61-11ee-be56-0242ac120002',
    'Pensez-vous que certaines infrastructures et activités critiques doivent être préparées à un scénario encore plus pessimiste que celui des 4 degrés de réchauffement climatique en France ?',
    12,
    'unique',
    null,
    null,
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET ordre = EXCLUDED.ordre, type = EXCLUDED.type;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'de636562-0e89-11ee-be56-0242ac120002',
    'Oui',
    1,
    'c0cf651c-0b61-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'de636562-0e89-11ee-be56-0242ac120222',
    'Non',
    2,
    'c0cf651c-0b61-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'de636562-0e89-11ee-be56-0242ac122202',
    'Je ne sais pas',
    3,
    'c0cf651c-0b61-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates(id, step, description, consultation_id) VALUES (
    'e9547964-0b61-11ee-be56-0242ac120002',
    1,
    '<body>👉 Les réponses à cette consultation seront présentées au ministre de la Transition écologique et de la Cohésion des territoires et serviront à alimenter les <b>travaux de préparation des politiques d’adaptation au changement climatique.</b><br/><br/>D’ici la fin de l’année 2023, le <b> Plan national d’adaptation au changement climatique </b> (PNACC) doit être révisé et ses mesures renforcées afin de prendre en compte les nouveaux effets du réchauffement climatique.</body>',
    'c342e83e-0b5a-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET description = EXCLUDED.description;

INSERT INTO consultations(id, title, end_date, cover_url, question_count, estimated_time, participant_count_goal, description, tips_description, thematique_id) VALUES (
    'ee3dd5c2-0c49-11ee-be56-0242ac120002',
    'Comment agir durablement en faveur de la sobriété énergétique ?',
    '2023-09-30',
    'https://betagouv.github.io/agora-content/energie.jpg',
    '10 questions',
    '7 minutes',
    10000,
    '<body>En octobre dernier, un <a href= "https://www.ecologie.gouv.fr/sites/default/files/dp-plan-sobriete.pdf">premier plan de sobriété</a> a été présenté par la Première ministre, Elisabeth Borne, et la ministre de la Transition énergétique, Agnès Pannier-Runacher. Ce plan a permis à la France de <b> réduire sa consommation d’énergie de plus de 10%.</b> <br/><br/>Le 20 juin, l’<b>acte 2 du plan de sobriété</b> a été présenté avec en priorité des mesures pour l’été, sur le numérique et sur le carburant. En octobre 2023, un bilan de la première année d’application du plan de sobriété devra être dressé et des mesures devront être prises afin d’ancrer la sobriété dans la durée.<br/><br/>Car la sobriété énergétique, ce n’était pas seulement pour passer l’hiver dernier, c’est une <b>mesure climatique de long-terme </b>: si nous voulons atteindre la neutralité carbone, nous devrons réduire notre consommation d’énergie de 40% d’ici 2050. L’objectif est donc d’<b>associer les citoyens en faveur de la sobriété dans la durée.</b></body>',
    '<body>🗣 Consultation proposée par le <b>Ministère de la Transition énergétique</b><br/><br/>🎯<b> Objectif</b> : trouver des solutions pour agir durablement en faveur de la transition énergétique <br/><br/>🚀<b>Axe gouvernemental</b> : Planifier et accélérer la transition écologique et énergétique</body>',
    '5e6bed94-015e-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title, end_date = EXCLUDED.end_date, question_count = EXCLUDED.question_count, participant_count_goal = EXCLUDED.participant_count_goal, description = EXCLUDED.description, tips_description = EXCLUDED.tips_description;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '34d997e4-0c4c-11ee-be56-0242ac120002',
    'Pour vous, la “sobriété énergétique”, c’est …',
    1,
    'open',
    null,
    null,
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET type = EXCLUDED.type;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '641d7656-0c4c-11ee-be56-0242ac120002',
    'Si je m’engage pour la sobriété énergétique, c’est en priorité pour :',
    2,
    'unique',
    null,
    null,
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '89e36102-0c4c-11ee-be56-0242ac120002',
    'Le climat',
    1,
    '641d7656-0c4c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '89e36102-0c4c-11ee-be56-0242ac120022',
    'Mon pouvoir d’achat',
    2,
    '641d7656-0c4c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '89e36102-0c4c-11ee-be56-0242ac120202',
    'Mon confort de vie',
    3,
    '641d7656-0c4c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '89e36102-0c4c-11ee-be56-0242ac120222',
    'Autre / Je ne souhaite pas m’engager',
    4,
    '641d7656-0c4c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'd7201d84-0c4c-11ee-be56-0242ac120002',
    'Réduire sa consommation d’énergie, dans une démarche de sobriété, c’est :',
    3,
    'unique',
    null,
    null,
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2fab57de-0c4d-11ee-be56-0242ac120002',
    'Une opportunité',
    1,
    'd7201d84-0c4c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2fab57de-0c4d-11ee-be56-0242ac120022',
    'Une contrainte',
    2,
    'd7201d84-0c4c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2fab57de-0c4d-11ee-be56-0242ac120202',
    'Les deux',
    3,
    'd7201d84-0c4c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '2fab57de-0c4d-11ee-be56-0242ac120222',
    'Je ne sais pas',
    4,
    'd7201d84-0c4c-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '97950b6a-0c4d-11ee-be56-0242ac120002',
    'Pour baisser la consommation de mon logement, en priorité, je suis prêt à :',
    4,
    'multiple',
    null,
    3,
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c78d732a-0c4d-11ee-be56-0242ac120002',
    'Respecter les consignes de température (19° de chauffage ; 26° de climatisation)',
    1,
    '97950b6a-0c4d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c78d732a-0c4d-11ee-be56-0242ac120022',
    'Entretenir ma chaudière (baisse de l’ordre de 10 à 15%)',
    2,
    '97950b6a-0c4d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c78d732a-0c4d-11ee-be56-0242ac120202',
    'M’équiper de thermostats (avec un gain potentiel de 5 à 10%)',
    3,
    '97950b6a-0c4d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c78d732a-0c4d-11ee-be56-0242ac122002',
    'Donner consigne à mon syndic ou à mon fournisseur d’énergie de piloter, pour moi, ma chaudière selon les indications que je lui donne',
    4,
    '97950b6a-0c4d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'c78d732a-0c4d-11ee-be56-0242ac122202',
    'Ces solutions ne me conviennent pas ou ne sont pas accessibles',
    5,
    '97950b6a-0c4d-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '5072cd84-0c4e-11ee-be56-0242ac120002',
    'Pour baisser ma facture de carburant, je suis prêt, en priorité, à :',
    5,
    'multiple',
    null,
    3,
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '68d63c80-0c4e-11ee-be56-0242ac120002',
    'Lever le pied à bord de mon véhicule, par exemple en roulant à 110 km/h sur autoroute (économies de 20% sur le carburant et les émissions de CO2)',
    1,
    '5072cd84-0c4e-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '68d63c80-0c4e-11ee-be56-0242ac120202',
    'Pratiquer le covoiturage via une application ou avec mes proches',
    2,
    '5072cd84-0c4e-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '68d63c80-0c4e-11ee-be56-0242ac122002',
    'Adapter ma conduite pour limiter ma consommation (par exemple : si je conduis un véhicule à boîte de vitesse manuelle, j’évite les surrégimes occasionnant une surconsommation de carburant pouvant aller jusqu’à 20 %)',
    3,
    '5072cd84-0c4e-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '68d63c80-0c4e-11ee-be56-0242ac120022',
    'Passer au vélo ou à la marche pour les petits trajets',
    4,
    '5072cd84-0c4e-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '68d63c80-0c4e-11ee-be56-0242ac122202',
    'Choisir les transports en commun dès lors qu’ils correspondent à mes horaires et à mon trajet',
    5,
    '5072cd84-0c4e-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '68d63c80-0c4e-11ee-be56-0242ac122222',
    'Ces solutions ne me conviennent pas ou ne sont pas accessibles',
    6,
    '5072cd84-0c4e-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '098d1f68-0c4f-11ee-be56-0242ac120002',
    'Pour réduire mes usages numériques, en priorité, je suis prêt à :',
    6,
    'multiple',
    null,
    3,
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3b50381e-0c4f-11ee-be56-0242ac120002',
    'Eteindre mes appareils en veille',
    1,
    '098d1f68-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3b50381e-0c4f-11ee-be56-0242ac120022',
    'Acheter des appareils reconditionnés',
    2,
    '098d1f68-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3b50381e-0c4f-11ee-be56-0242ac122202',
    'Faire attention à mes petits usages les plus énergivores comme le non-archivage des mails',
    3,
    '098d1f68-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3b50381e-0c4f-11ee-be56-0242ac122002',
    'Diminuer ma consommation de vidéos en streaming (réduire la qualité ou télécharger la vidéo consomme moins d’énergie)',
    4,
    '098d1f68-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3b50381e-0c4f-11ee-be56-0242ac122222',
    'Ces solutions ne me conviennent pas',
    5,
    '098d1f68-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'a60aaf9a-0c4f-11ee-be56-0242ac120002',
    'Quels sont les principaux obstacles qui vous empêchent de passer à un véhicule moins polluant ?',
    7,
    'multiple',
    null,
    3,
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'da2256b6-0c4f-11ee-be56-0242ac120002',
    'Je ne connais pas les aides financières auxquelles j’ai le droit',
    1,
    'a60aaf9a-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'da2256b6-0c4f-11ee-be56-0242ac120202',
    'Je connais les aides financières mais elles ne me permettent pas d’acquérir le véhicule que je souhaite',
    2,
    'a60aaf9a-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'da2256b6-0c4f-11ee-be56-0242ac120022',
    'Je ne sais pas quel véhicule choisir, les réglementations changent tout le temps',
    3,
    'a60aaf9a-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'da2256b6-0c4f-11ee-be56-0242ac120222',
    'Même avec les aides, je n’aurais pas le prêt de mon banquier dont j’ai besoin pour avancer l’argent',
    4,
    'a60aaf9a-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'da2256b6-0c4f-11ee-be56-0242ac122002',
    'L’absence d’infrastructures, comme les bornes de recharge, près de chez moi',
    5,
    'a60aaf9a-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'da2256b6-0c4f-11ee-be56-0242ac122202',
    'Je ne souhaite pas passer à un véhicule moins polluant',
    6,
    'a60aaf9a-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'da2256b6-004f-111e-be56-0242ac122202',
    'Je ne suis pas concerné(e)',
    7,
    'a60aaf9a-0c4f-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'd7201d84-0c4c-11ee-be56-0242ac122222',
    'Selon moi, la mesure la plus efficace pour réduire ma consommation d’énergie dans la durée, c’est :',
    8,
    'unique',
    null,
    null,
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'a6c83168-0c50-11ee-be56-0242ac120002',
    'Réduire l’usage de véhicules polluants',
    1,
    'd7201d84-0c4c-11ee-be56-0242ac122222'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'a6c83168-0c50-11ee-be56-0242ac120022',
    'Rénover et isoler les logements',
    2,
    'd7201d84-0c4c-11ee-be56-0242ac122222'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'a6c83168-0c50-11ee-be56-0242ac120222',
    'Limiter la pollution lumineuse (éclairage, publicité…)',
    3,
    'd7201d84-0c4c-11ee-be56-0242ac122222'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'a6c83168-0c50-11ee-be56-0242ac122002',
    'Allonger la durée de vie des appareils numériques',
    4,
    'd7201d84-0c4c-11ee-be56-0242ac122222'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'a6c83168-0c50-11ee-be56-0300ac122002',
    'Je ne sais pas',
    5,
    'd7201d84-0c4c-11ee-be56-0242ac122222'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'fb56a610-0c50-11ee-be56-0242ac120002',
    'La méthode qui me paraît la plus adaptée pour réduire ma consommation d’énergie, c’est :',
    9,
    'multiple',
    null,
    2,
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '21f260b6-0c51-11ee-be56-0242ac120002',
    'L’incitation : plus de primes pour les comportements vertueux',
    1,
    'fb56a610-0c50-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '21f260b6-0c51-11ee-be56-0242ac120022',
    'La contrainte : plus de sanctions pour les comportements les moins vertueux',
    2,
    'fb56a610-0c50-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '21f260b6-0c51-11ee-be56-0242ac120202',
    'Les règles : plus de normes pour encadrer les usages',
    3,
    'fb56a610-0c50-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '21f260b6-0c51-11ee-be56-0242ac122002',
    'L’accompagnement : plus de pédagogie et de sensibilisation',
    4,
    'fb56a610-0c50-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '21f260b6-0c51-1111-be56-0242ac122222',
    'Je ne sais pas',
    5,
    'fb56a610-0c50-11ee-be56-0242ac120002'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '8488ee98-0c51-11ee-be56-0242ac120002',
    'Pour poursuivre les efforts qui ont permis à la France de réduire de 12% sa consommation d’énergie, quelle nouvelle mesure recommanderiez-vous ?',
    10,
    'open',
    null,
    null,
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET type = EXCLUDED.type;

INSERT INTO consultation_updates(id, step, description, consultation_id) VALUES (
    '22682956-094b-423b-9086-9ec4f8ef2222',
    1,
    '<body>👉 La ministre de la Transition énergétique, Agnès Pannier-Runacher, recevra les résultats et viendra annoncer les enseignements pour la suite et les actions qui découleront de vos réponses <b>début octobre</b>, dans le cadre du bilan des 1 an du premier plan de sobriété.<br/><br/>—<br/><br/><b>🤓 Envie d’aller plus loin ?</b><br/>Vous pouvez notamment :<br/><ul><li>Explorer les <a href="https://www.ecologie.gouv.fr/sites/default/files/dp-plan-sobriete.pdf">Plans de sobriété</a></li><li><a href="https://www.monecowatt.fr/application-mobile-ecowatt/">Télécharger l’application EcoWatt de RTE</a> pour savoir à quel moment réduire votre consommation et éviter les coupures</li><li><a href="https://librairie.ademe.fr/actualites/10-gestes-pour-economiser-lenergie-n8">Découvrir les 10 gestes de sobriété recommandés par l’ADEME</a></li></ul></body>',
    'ee3dd5c2-0c49-11ee-be56-0242ac120002'
) ON CONFLICT (id) DO UPDATE SET description = EXCLUDED.description;
