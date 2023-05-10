INSERT INTO thematiques(id, label, picto) VALUES (
    '1f3dbdc6-cff7-4d6a-88b5-c5ec84c55d15',
    'Travail & emploi',
    '\ud83d\udcbc'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto) VALUES (
    'bb051bf2-644b-47b6-9488-7759fa727dc0',
    'Transition écologique',
    '\ud83c\udf31'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto) VALUES (
    'a4bb4b27-3271-4278-83c9-79ac3eee843a',
    'Santé',
    '\ud83e\ude7a'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto) VALUES (
    'c97c3afd-1940-4b6d-950a-734b885ee5cb',
    'Economie',
    '\ud83d\udcc8'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto) VALUES (
    '5b9180e6-3e43-4c63-bcb5-4eab621fc016',
    'Education',
    '\ud83c\udf93'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto) VALUES (
    '8e200137-df3b-4bde-9981-b39a3d326da7',
    'International',
    '\ud83c\udf0f'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto) VALUES (
    '0f644115-08f3-46ff-b776-51f19c65fdd1',
    'Transports',
    '\ud83d\ude8a'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto) VALUES (
    'b276606e-f251-454e-9a73-9b70a6f30bfd',
    'Sécurité',
    '\ud83d\ude94'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto) VALUES (
    '30671310-ee62-11ed-a05b-0242ac120003',
    'Démocratie',
    '\uD83D\uDDF3'
) ON CONFLICT DO NOTHING;

INSERT INTO thematiques(id, label, picto) VALUES (
    '47897e51-8e94-4920-a26a-1b1e5e232e82',
    'Autre',
    '\ud83d\udce6'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    'f29c5d6f-9838-4c57-a7ec-0612145bb0c8',
    'Pourquoi avoir créé l’application Agora ?',
    'Quel est le but de l’application Agora ? A quel besoin des Français et des Françaises espérez-vous qu’elle réponde ? Que signifie son caractère expérimental ?',
    '2023-06-01',
    1,
    'Agora',
    '30671310-ee62-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO responses_qag(id, author, author_portrait_url, author_description, response_date, video_url, transcription, qag_id) VALUES (
    '38990baf-b0ed-4db0-99a5-7ec01790720e',
    'Olivier Véran',
    'https://betagouv.github.io/agora-content/QaG-OlivierVeran.png',
    'Ministre délégué auprès de la Première ministre, chargé du Renouveau démocratique, porte-parole du Gouvernement',
    '2023-06-05',
    'https://betagouv.github.io/agora-content/qag_response_creation_agora.mov',
    'Bonjour,\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
    'f29c5d6f-9838-4c57-a7ec-0612145bb0c8'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '1e9d2830-ee68-11ed-a05b-0242ac120003',
    'Je suis à la tête d’une TPE et je suis très inquiète pour l’avenir. Que fait l’Etat pour nous aider ?',
    'Les prévisions de croissance ne sont pas bonnes. La concurrence internationale est particulièrement rude et mes charges explosent. Je me sens particulièrement seule et inquiète. Que se passera-t-il pour mes employés si je mets la clef sous la porte ??',
    '2023-06-02',
    1,
    'Julie',
    'c97c3afd-1940-4b6d-950a-734b885ee5cb'
) ON CONFLICT DO NOTHING;

INSERT INTO responses_qag(id, author, author_portrait_url, author_description, response_date, video_url, transcription, qag_id) VALUES (
    '67c904ea-d6bb-4825-bd98-cd8ec831b4d3',
    'Olivia Grégoire',
    'https://betagouv.github.io/agora-content/QaG-OliviaGregoire.png',
    'Ministre déléguée auprès du ministre de l’Économie, des Finances et de la Souveraineté industrielle et numérique, chargée des Petites et moyennes entreprises, du Commerce, de l’Artisanat et du Tourisme',
    '2023-06-05',
    'https://betagouv.github.io/agora-content/qag_response_avenir_tpe.mov',
    'Bonjour,\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
    '1e9d2830-ee68-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '817b5c32-ee69-11ed-a05b-0242ac120003',
    'Pourquoi ne peut-on pas choisir là où vont précisément nos impôts ? ',
    'Il est normal de payer ses impôts pour financer notre système. Néanmoins, il me semble qu’il y aurait un consentement plus grand si vous pouvions chacun choisir les postes de dépense auquel nous aimerions contribuer. Cela donnerait plus de sens et moins l’impression de financer “une machine globale”. Qu’en pensez-vous ?',
    '2023-06-01',
    1,
    'Jean',
    '30671310-ee62-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO responses_qag(id, author, author_portrait_url, author_description, response_date, video_url, transcription, qag_id) VALUES (
    '7c4525bb-3557-473d-83da-3bcd940a5a9c',
    'Gabriel Attal',
    'https://betagouv.github.io/agora-content/QaG-GabrielAttal.png',
    'Ministre délégué auprès du ministre de l’Économie, des Finances et de la Souveraineté industrielle et numérique, chargé des Comptes publics',
    '2023-06-05',
    'https://betagouv.github.io/agora-content/qag_response_impots_choix_depenses.mov',
    'Bonjour,\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
    '817b5c32-ee69-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '996436ca-ee69-11ed-a05b-0242ac120003',
    'On parle beaucoup de l’intelligence artificielle… vous comptez faire quoi pour les emplois qu’elle risque de remplacer ?',
    'ChatGPT est arrivé avec fureur et on voit déjà combien un certain nombre de tâches et de métier pourraient être rapidement remplacés par l’intelligence artificielle… Faut-il interdire ChatGPT? Faut-il ré-orienter les travailleurs concernés ?',
    '2023-06-02',
    1,
    'Nina',
    '1f3dbdc6-cff7-4d6a-88b5-c5ec84c55d15'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    'f8776dd0-ee69-11ed-a05b-0242ac120003',
    'Pourquoi ne taxe-t-on pas le kérosène des avions ? La France doit militer pour !',
    'Je trouve ça absolument lunaire que le kérosène des avions ne soit pas taxés, alors que les carburants des autres modes de transports le sont. Il s’agit d’une mesure à prendre au niveau international : la France défend-elle cette position de pur bon sens et quand peut-on espérer avoir des résultats ?',
    '2023-06-01',
    1,
    'Elliott',
    'bb051bf2-644b-47b6-9488-7759fa727dc0'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '40c04882-ee6a-11ed-a05b-0242ac120003',
    'Peut-on refuser d’être pris en charge par un soignant non-vacciné ? Personnellement, ça me dérange.',
    'Au pays de Pasteur, je trouve ça inadmissible que les soignants non-vaccinés soient ré-intégrés dans le corps hospitalier. Est-ce possible de refuser qu’un tel soignant me soigne ? Désolée mais je ne fais pas confiance à quelqu’un qui ne croit pas à la science.',
    '2023-06-01',
    1,
    'Laure',
    'a4bb4b27-3271-4278-83c9-79ac3eee843a'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '8171c50e-ee6a-11ed-a05b-0242ac120003',
    'Quel va être l’impact de la baisse de la note de la France par l’agence de notation financière Fitch ?',
    'L’agence de notation financière Fitch a baissé la note de la France, passant de AA à AA-. Qu’est-ce que ça veut dire concrètement ? Est-ce un signal pour l’avenir ou cela a-t-il un impact financier direct ?',
    '2023-06-02',
    1,
    'Patrick',
    'c97c3afd-1940-4b6d-950a-734b885ee5cb'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '1731a370-ee6b-11ed-a05b-0242ac120003',
    'Ma question, c’est pourquoi on n’arrive pas, dans ce pays, à recruter davantage de professeurs ?!',
    'Il y a bien sûr la question de la rémunération, mais au-delà de ça, c’est aussi la position sociale et la reconnaissance même du professeur qui semble avoir “baissé” ces 20 dernières années. Comment remédier à cela ?',
    '2023-06-02',
    1,
    'Agny',
    '5b9180e6-3e43-4c63-bcb5-4eab621fc016'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '5c5f5460-ee6b-11ed-a05b-0242ac120003',
    'Aurait-on pu faire quelque chose pour éviter la guerre en Ukraine ? Et les prochaines ? Je m’interroge',
    'La guerre en Ukraine semble partie pour durer, avec les conséquences dramatiques que l’on sait. Aurait-on pu éviter cette guerre ? Peut-on faire quelque chose pour éviter qu’il y en ait d’autres ?',
    '2023-06-02',
    1,
    'Béatrice',
    '8e200137-df3b-4bde-9981-b39a3d326da7'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '96e8341c-ee6b-11ed-a05b-0242ac120003',
    'Pourquoi ne pas tester un tarif unique de transport en commun pas trop cher partout en France comme en Allemagne ?',
    'L’Allemagne a lancé en mai DeutschlandTicket à 49 euros par mois.qui permet avec un seul billet d’aller dans n’importe quel transport en commun local et régional. Ne serait-ce pas un bon coup de pouce pour le pouvoir d’achat et la promotion des transports en commun ?',
    '2023-06-01',
    1,
    'Danny',
    '0f644115-08f3-46ff-b776-51f19c65fdd1'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    'd09f2788-ee6b-11ed-a05b-0242ac120003',
    'La France devrait autoriser la reconnaissance faciale par vidéo-surveillance pour lutter contre les délits du quotidien : qu’est-ce qu’on attend ?',
    'Y-a-t-il eu des expérimentations sur ces dispositifs ? Est-ce que c’est efficace ? Est-ce que les JO vont être l’occasion de tester des choses sur ce sujet ?',
    '2023-06-02',
    1,
    'Josette',
    'b276606e-f251-454e-9a73-9b70a6f30bfd'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '1cde43fe-ee6c-11ed-a05b-0242ac120003',
    'Est-ce qu’il ne serait pas temps d’avoir enfin (!) un vrai débat sur la légalisation du cannabis, comme ils le font dans plein de pays ?',
    'On consomme beaucoup de cannabis en France, même si c’est illégal. Ne devrait-on pas avoir un débat sur une potentielle légalisation pour que cette réalité cesse de nourrir des trafics illégaux ? Arrêtons l’hypocrisie ! (Et ce serait sûrement bon pour les finances de l’Etat …)',
    '2023-06-02',
    1,
    'Tyron',
    '47897e51-8e94-4920-a26a-1b1e5e232e82'
) ON CONFLICT DO NOTHING;

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id) VALUES (
    '59028e08-ee6c-11ed-a05b-0242ac120003',
    'Quel est le niveau de la menace terroriste sur la France aujourd’hui ?',
    'On se doute qu’il y a des attentats déjoués dont nous ne sommes pas au courant et c’est bien normal. Mais pouvez-vous nous dire par rapport aux années précédentes et des événements dans le monde actuel à quel niveau de menace nous situons-nous ?',
    '2023-06-01',
    1,
    'Kevin',
    'b276606e-f251-454e-9a73-9b70a6f30bfd'
) ON CONFLICT DO NOTHING;

INSERT INTO consultations(id, title, end_date, cover_url, question_count, estimated_time, participant_count_goal, description, tips_description, thematique_id) VALUES (
    '6d85522a-ee71-11ed-a05b-0242ac120003',
    'Développer le covoiturage au quotidien',
    '2023-06-21',
    'https://betagouv.github.io/agora-content/covoiturage.png',
    '7 questions',
    '5 minutes',
    100,
    '<body>Le Gouvernement a lancé un plan national pour faciliter le covoiturage au quotidien : son objet est de tripler le nombre de trajets en covoiturage du quotidien d’ici 2027 pour atteindre les 3 millions de trajet. <br/>Le covoiturage est un enjeu majeur pour :<br/><ul><li><b>Le pouvoir d’achat</b>. Un covoiturage quotidien pour se rendre sur son lieu de travail à 30 km permet une économie de près de 2000 euros chaque année.</li><li><b>L’amélioration de la qualité de l’air et la baisse des gaz à effet de serre</b>. Le partage d’une voiture divise par 2 les émissions de son trajet. Si l’objectif est atteint, 4.5 millions de tonnes de CO2 par an peuvent être économisées (environ 1% des émissions françaises).</li><li><b>Se déplacer plus librement.</b> Le covoiture, c’est un moyen de  se déplacer plus facilement là où il n’y a pas de transports en commun mais aussi pour ceux qui n’ont pas de voiture ou ne peuvent pas conduire.</li></ul><br/>Sources (<a href="https://www.ecologie.gouv.fr/covoiturage">https://www.ecologie.gouv.fr/covoiturage</a>)</body>',
    '<body>🗣 Consultation proposée par le <b>Ministère des Transports</b><br/>🎯<b> Objectif</b> : évaluer et améliorer le plan national covoiturage</body>',
    '0f644115-08f3-46ff-b776-51f19c65fdd1'
) ON CONFLICT (id) DO UPDATE SET tips_description = EXCLUDED.tips_description;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'e271ed7a-ef05-11ed-a05b-0242ac120003',
    'Pour vous, le covoiturage c’est un sujet …',
    1,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f5dd076e-ef05-11ed-a05b-0242ac120003',
    'Pas très important',
    1,
    'e271ed7a-ef05-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '0fd15904-ef06-11ed-a05b-0242ac120003',
    'Important',
    2,
    'e271ed7a-ef05-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '17c62b58-ef06-11ed-a05b-0242ac120003',
    'Très important',
    3,
    'e271ed7a-ef05-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '48d3c502-ef06-11ed-a05b-0242ac120003',
    'Comment vous rendez-vous généralement sur votre lieu de travail ?',
    2,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '84f7f70e-ef08-11ed-a05b-0242ac120003',
    'En voiture, seul(e)',
    1,
    '48d3c502-ef06-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '84f7f70e-ef08-11ed-a05b-0242ac120010',
    'En covoiturage',
    2,
    '48d3c502-ef06-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '84f7f70e-ef08-11ed-a05b-0242ac120011',
    'En transports en commun',
    3,
    '48d3c502-ef06-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '84f7f70e-ef08-11ed-a05b-0242ac120012',
    'En vélo ou à pied',
    4,
    '48d3c502-ef06-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '84f7f70e-ef08-11ed-a05b-0242ac120013',
    'Je ne suis pas concerné',
    5,
    '48d3c502-ef06-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'e52c5868-ef08-11ed-a05b-0242ac120003',
    'Avez-vous repéré des incitations à vous lancer dans le covoiturage ces derniers mois, dans la presse, à la radio, sur des affiches ou autre ?',
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
    'Je crois mais je n’en suis pas sûr(e)',
    2,
    'e52c5868-ef08-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '1b52a816-ef09-11ed-a05b-0242ac120001',
    'Non',
    3,
    'e52c5868-ef08-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'a3ae519c-ef09-11ed-a05b-0242ac120003',
    'Une campagne de communication a eu lieu à partir de mars.',
    4,
    'chapter',
    '<body>Le plan de covoiturage comporte 3 parties :<br/><ol><li>Une <b>prime de 100 euros</b> pour ceux qui se lancent dans le covoiturage (25 euros au premier trajet et 75 euros au bout du 10ème trajet dans les 3 mois)</li><li>Le <b>forfait mobilités durables</b>, un dispositif financier de soutien aux salariés du secteur privé (jusqu’à 800 euros par an) et agents de services publics (jusqu’à 300 euros par an) pour leurs déplacements domicile-travail</li><li>Un <b>soutien aux aides locales.</b> De nombreuses collectivités proposent des incitations financières et l’Etat double la mise pour les encourager.</li></ol></body>',
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '91c3411c-ef0a-11ed-a05b-0242ac120003',
    'Avez-vous bénéficié d’une des ces aides ?',
    5,
    'multiple',
    null,
    3,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120003',
    'Prime de 100 euros reversée par les plateformes de covoiturage',
    1,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120010',
    'Forfait mobilités durables avec votre employeur',
    2,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120011',
    'Incitation financière de votre collectivité',
    3,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120012',
    'Non et ça ne m’intéresse pas',
    4,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120013',
    'Non, mais je vais me renseigner',
    5,
    '91c3411c-ef0a-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120015',
    'Avez-vous des idées ou des recommandations pour améliorer ces différentes aides financières ?',
    6,
    'ouverte',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '91c3411c-ef0a-11ed-a05b-0242ac120100',
    'En dehors des aides financières,  qu’est-ce qui marcherait le mieux selon vous pour encourager les personnes à faire du covoiturage ?',
    7,
    'multiple',
    null,
    2,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac121000',
    'Avoir une place réservée et gratuite de parking',
    1,
    '91c3411c-ef0a-11ed-a05b-0242ac120100'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac121001',
    'Pouvoir utiliser des voies réservées pour aller plus vite',
    2,
    '91c3411c-ef0a-11ed-a05b-0242ac120100'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac121002',
    'Avoir une meilleure interconnexion avec les autres transports (bus, train, etc.)',
    3,
    '91c3411c-ef0a-11ed-a05b-0242ac120100'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac121003',
    'Rien',
    4,
    '91c3411c-ef0a-11ed-a05b-0242ac120100'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'ac24b428-ef0a-11ed-a05b-0242ac120020',
    'Avez-vous d’autres idées pour encourager le covoiturage en France et améliorer le plan national lancé ?',
    8,
    'ouverte',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates(id, step, description, consultation_id) VALUES (
    '72682956-094b-423b-9086-9ec4f8ef2662',
    1,
    '<body>Le Ministre des transports recevra les résultats et viendra annoncer les enseignements pour la suite et les actions qui découleront de vos réponses le <b>22 juin à 10h</b>.<br/>Il s’agira notamment de :<br/><ul><li>Faire un <b>premier bilan</b> des engagements pris grâce au plan,</li><li>Mettre en place de <b>nouvelles actions</b> pour encourager le covoiturage</li></ul><br/>—<br/><br/><b>\ud83d\ude97Envie d’aller plus loin ?</b><br/>Rendez-vous ici <a href="https://www.ecologie.gouv.fr/covoiturage">(https://www.ecologie.gouv.fr/covoiturage)</a> pour voir comment vous lancer et en savoir plus sur le covoiturage et ses enjeux.</body>',
    '6d85522a-ee71-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO consultations(id, title, end_date, cover_url, question_count, estimated_time, participant_count_goal, description, tips_description, thematique_id) VALUES (
    '6d85522a-ee71-11ed-a05b-0242ac120010',
    'Généraliser le Service National Universel (SNU)',
    '2023-06-22',
    'https://betagouv.github.io/agora-content/education.png',
    '7 questions',
    '5 minutes',
    100,
    '<body>Promesse de campagne du président de la République, le Service national universel (SNU) a été mis en place à partir de 2019. Son but est de créer du collectif en favorisant le sentiment d’unité nationale, de transmettre le goût de l’engagement et de la République et d’impliquer la jeunesse dans la vie du pays.<br/>Il s’agit concrètement d’un parcours à destination des jeunes de 15 à 17 ans en plusieurs étapes :<br/><ol><li>Un <b>séjour de cohésion de 15 jours</b> avec une centaine de jeunes de toute la France dans un autre département que le sien,</li><li>Une <b>mission d’intérêt général</b> à effectuer sur une année près de chez soi,</li><li>En option, la poursuite par une <b>mission de Service civique</b> ou du bénévolat jusqu’à ses 25 ans.</li></ol><br/>Pensez-vous que le SNU est un dispositif utile ? Comment le rendre plus efficace pour créer de la cohésion nationale auprès des jeunes ?<br/><br/>Sources (<a href="https://www.snu.gouv.fr/](https://www.snu.gouv.fr/">https://www.snu.gouv.fr/](https://www.snu.gouv.fr/</a>)</body>',
    '<body>🗣 Consultation proposée par le <b>Secrétariat d’Etat chargé de la Jeunesse et du Service national universel</b><br/>🎯<b> Objectif</b> : évaluer la perception du SNU par les citoyens et le faire évoluer</body>',
    '5b9180e6-3e43-4c63-bcb5-4eab621fc016'
) ON CONFLICT (id) DO UPDATE SET tips_description = EXCLUDED.tips_description;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'c98bb6f6-ef0f-11ed-a05b-0242ac120003',
    'Pour vous, le Service national universel c’est un sujet …',
    1,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f8b5a978-ef0f-11ed-a05b-0242ac120003',
    'Pas très important',
    1,
    'c98bb6f6-ef0f-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f8b5a978-ef0f-11ed-a05b-0242ac120001',
    'Important',
    2,
    'c98bb6f6-ef0f-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f8b5a978-ef0f-11ed-a05b-0242ac120002',
    'Très important',
    3,
    'c98bb6f6-ef0f-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'c98bb6f6-ef0f-11ed-a05b-0242ac120100',
    'Connaissez-vous quelqu’un ayant participé au SNU ?',
    2,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f8b5a978-ef0f-11ed-a05b-0242ac121111',
    'J’ai moi-même participé',
    1,
    'c98bb6f6-ef0f-11ed-a05b-0242ac120100'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f8b5a978-ef0f-11ed-a05b-0242ac121011',
    'Je connais une personne ayant participé',
    2,
    'c98bb6f6-ef0f-11ed-a05b-0242ac120100'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f8b5a978-ef0f-11ed-a05b-0242ac121101',
    'Je connais plusieurs personnes ayant participé',
    3,
    'c98bb6f6-ef0f-11ed-a05b-0242ac120100'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'f8b5a978-ef0f-11ed-a05b-0242ac121110',
    'Je ne connais personne ayant participé',
    4,
    'c98bb6f6-ef0f-11ed-a05b-0242ac120100'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'a3ae519c-ef09-11ed-a05b-0242ac120113',
    'Zoom sur le séjour de cohésion',
    3,
    'chapter',
    '<body>La phase 1 du SNU est le séjour de cohésion.<br/><br/>Pendant 2 semaines, les journées sont rythmées :<br/><br/><ul><li>La journée commence par le lever des couleurs, <b>rituel républicain</b> pendant lequel on lève le drapeau français et on chante l’hymne national, la Marseillaise.</li><li>Ont ensuite lieu des <b>activités diverses</b>, en plein air ou en salle, toujours collectives et participatives : sport et activités physiques, activités culturelles, chantiers participatifs, visites, etc.</li><li>L’hébergement se fait par <b>maisonnée</b> composée d’une douzaine de jeunes et encadrée par un tuteur. Filles et garçons sont séparés. Tous participent aux tâches du centre d’hébergement (repas, nettoyage, gestion des déchets ménagers, organisations des activités, etc.)</li><li>Le séjour de cohésion se termine par une <b>cérémonie de clôture</b>, en présence des autorités locales.</li></ul></body>',
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'dfb1647e-ef11-11ed-a05b-0242ac120003',
    'Qu’est-ce qui vous semble le plus important dans le séjour de cohésion ?',
    4,
    'multiple',
    null,
    2,
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '05ed3320-ef12-11ed-a05b-0242ac120003',
    'Sortir de son milieu habituel',
    1,
    'dfb1647e-ef11-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '05ed3320-ef12-11ed-a05b-0242ac120000',
    'Rencontrer d’autres jeunes',
    2,
    'dfb1647e-ef11-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '05ed3320-ef12-11ed-a05b-0242ac120001',
    'L’apprentissage des rites républicains',
    3,
    'dfb1647e-ef11-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '05ed3320-ef12-11ed-a05b-0242ac120002',
    'La découverte de différentes activités',
    4,
    'dfb1647e-ef11-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '05ed3320-ef12-11ed-a05b-0242ac120100',
    'Avez-vous des idées ou des recommandations pour améliorer le séjour de cohésion ?',
    5,
    'ouverte',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'a9a2974e-ef12-11ed-a05b-0242ac120003',
    'Zoom sur la mission d’intérêt général.',
    6,
    'chapter',
    '<body>La phase 2 du SNU est la mission d’intérêt général.<br/><br/><ul><li>Le jeune choisit la <b>cause</b> qu’il souhaite soutenir : environnement et développement durable, solidarité, santé, sport, éducation, sécurité, défense et mémoire, culture ou citoyenneté.</li><li>Il choisit une structure et lui apporte son aide pendant <b>12 jours minimum ou 84 heures dans l’année</b>, de façon ponctuelle ou régulière.</li><li>Cela peut être au sein d’une <b>association</b>, d’un <b>service public</b>, d’un établissement de santé ou encore d’une entreprise solidaire d’utilité sociale agréée.</li></ul></body>',
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '05ed3320-ef12-11ed-a05b-0242ac120102',
    'Avez-vous des idées ou des recommandations pour améliorer la mission d’intérêt général ?',
    7,
    'ouverte',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'e271ed7a-ef05-11ed-a05b-0142ac120003',
    'Si vous aviez entre 15 et 17 ans, aimeriez-vous participer au SNU ?',
    8,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '01ed3320-ef10-11ed-a05b-0242ac120002',
    'Non, pas du tout',
    1,
    'e271ed7a-ef05-11ed-a05b-0142ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '01ed3320-ef11-11ed-a05b-0242ac120002',
    'Plutôt pas',
    2,
    'e271ed7a-ef05-11ed-a05b-0142ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '01ed3320-ef13-11ed-a05b-0242ac120002',
    'Plutôt oui',
    3,
    'e271ed7a-ef05-11ed-a05b-0142ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '01ed3320-ef02-11ed-a05b-0242ac120002',
    'Oui vraiment',
    4,
    'e271ed7a-ef05-11ed-a05b-0142ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'e271ed7a-ef05-10ed-a05b-0142ac120003',
    'Selon vous, le SNU devrait …',
    9,
    'unique',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '01ed3320-ef12-10ed-a05b-0242ac120002',
    'être supprimé',
    1,
    'e271ed7a-ef05-10ed-a05b-0142ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '01ed3320-ef12-12ed-a05b-0242ac120002',
    'être étendu en restant sur la base du volontariat',
    2,
    'e271ed7a-ef05-10ed-a05b-0142ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '01ed3320-ef12-13ed-a05b-0242ac120002',
    'être rendu obligatoire',
    3,
    'e271ed7a-ef05-10ed-a05b-0142ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '05ed3320-ef12-11ed-a05b-0242ac110102',
    'Souhaitez-vous ajouter un commentaire ou une remarque sur le service national universel ?',
    10,
    'ouverte',
    null,
    null,
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates(id, step, description, consultation_id) VALUES (
    '72682956-094b-423b-9086-9ec4f8ef2612',
    1,
    '<body>\ud83d\udc49 La Secrétaire d’Etat en charge de la jeunesse et du Service national universel recevra les résultats et viendra annoncer les enseignements pour la suite et les actions qui découleront de vos réponses le <b>23 juin à 10h</b>.<br/><br/>Il s’agira notamment de :<br/><br/><ul><li>Faire un <b>bilan</b> de ce que les Français perçoivent du SNU,</li><li>Proposer des pistes d’amélioration pour le SNU.</li></ul>—<br/><br/><b>\uD83E\uDD13 Envie d’aller plus loin ?</b><br/><br/>Rendez-vous ici (<a href="https://www.snu.gouv.fr/">https://www.snu.gouv.fr/</a>) pour en savoir plus et proposer des missions d’intérêt général si vous avez une structure qui peut en accueillir.</body>',
    '6d85522a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO consultations(id, title, end_date, cover_url, question_count, estimated_time, participant_count_goal, description, tips_description, thematique_id) VALUES (
    '6d82222a-ee71-11ed-a05b-0242ac120010',
    'Renforcer le numérique au service des patients',
    '2023-06-23',
    'https://betagouv.github.io/agora-content/sante.png',
    '7 questions',
    '5 minutes',
    100,
    '<body>Le Gouvernement souhaite améliorer le parcours des patients par le numérique et la télésanté.<br/>Le numérique en santé est un enjeu majeur pour :<br/><ul><li><b>Faciliter la vie.</b> Permettre aux Français de gérer leur santé plus facilement et plus efficacement grâce à des outils numériques innovants.</li><li><b>Améliorer l’accès aux soins</b>. Réduire les déplacements inutiles des patients en leur offrant des options de consultation à distance, désengorger les établissements de santé et offrir une alternative aux citoyens habitants dans des déserts médicaux.</li><li><b>Améliorer la qualité des soins</b>. Permettre aux professionnels de santé de mieux communiquer et de partager des informations sur les patients conduirait à une prise en charge plus rapide et plus efficace des problèmes de santé.</li></ul></br>Quelle est votre perception de l’utilisation du numérique en santé ? Comment pourrions nous vous faciliter la vie en la matière ?</body>',
    '<body>🗣 Consultation proposée par le <b>Ministère de la santé</b><br/>🎯<b> Objectif</b> : évaluer la perception et trouver des idées d’amélioration de l’utilisation du numérique en matière de santé</body>',
    'a4bb4b27-3271-4278-83c9-79ac3eee843a'
) ON CONFLICT (id) DO UPDATE SET tips_description = EXCLUDED.tips_description;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '95167ec6-ef17-11ed-a05b-0242ac120003',
    'Comment prenez-vous généralement vos rendez-vous médicaux ?',
    1,
    'unique',
    null,
    null,
    '6d82222a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'b7c81768-ef17-11ed-a05b-0242ac120003',
    'En ligne',
    1,
    '95167ec6-ef17-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'b7c81768-ef17-11ed-a05b-0242ac120001',
    'Par téléphone',
    2,
    '95167ec6-ef17-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'b7c81768-ef17-11ed-a05b-0242ac120002',
    'Via un proche',
    3,
    '95167ec6-ef17-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'b7c81768-ef17-11ed-a05b-0242ac120010',
    'Autre',
    4,
    '95167ec6-ef17-11ed-a05b-0242ac120003'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '95167ec6-ef17-11ed-a05b-0242ac120010',
    'Avez-vous déjà effectué une consultation en ligne avec un médecin généraliste ou un spécialiste ?',
    2,
    'unique',
    null,
    null,
    '6d82222a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'b7c81768-ef17-11ed-a05b-0242ac121010',
    'Oui, et je recommande',
    1,
    '95167ec6-ef17-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'b7c81768-ef17-11ed-a05b-0242ac111010',
    'Oui, mais je ne recommande pas',
    2,
    '95167ec6-ef17-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'b7c81768-ef17-11ed-a05b-0242ac011010',
    'Non, je n’ai pas encore eu l’occasion',
    3,
    '95167ec6-ef17-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    'b7c81768-ef17-11ed-a05b-0242ac121011',
    'Non, je ne le souhaite pas',
    4,
    '95167ec6-ef17-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '01ed3320-ef12-11ed-a05b-0242ac120100',
    'D’après vous, comment les consultations en ligne pourraient-elles améliorées ?',
    3,
    'ouverte',
    null,
    null,
    '6d82222a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'a3ae519c-ef09-11ed-a05b-0242ac125503',
    'Connaissez-vous Mon Espace santé ?',
    4,
    'chapter',
    '<body><ul><li>Mon Espace Santé est un service qui vous permet de <b>stocker et partager vos documents et données de santé</b> en toute confidentialité.  Vous y retrouvez le contenu de votre Dossier Médical Partagé (DMP) si vous en possédez un.</li><li>Grâce à la <b>messagerie sécurisée</b>, vos professionnels de santé peuvent vous envoyer des informations et des documents en toute confidentialité.</li><li>Vous avez à disposition un <b>catalogue de services numériques de confiance</b> dans le domaine de la santé, du bien-être ou du maintien de l''autonomie.</li></ul></body>',
    null,
    '6d82222a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '95167ec6-ef17-11ed-a15b-0242ac120010',
    'Utilisez-vous “Mon Espace Santé” ?',
    5,
    'unique',
    null,
    null,
    '6d82222a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3932330a-ef19-11ed-a05b-0242ac120003',
    'Oui, j’utilise principalement le site internet',
    1,
    '95167ec6-ef17-11ed-a15b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3932330a-ef19-11ed-a05b-0242ac120000',
    'Oui, j’utilise principalement l’application mobile',
    2,
    '95167ec6-ef17-11ed-a15b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3932330a-ef19-11ed-a05b-0242ac120001',
    'Non, je ne connais pas',
    3,
    '95167ec6-ef17-11ed-a15b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3932330a-ef19-11ed-a05b-0242ac120002',
    'Non, j’y suis opposé',
    4,
    '95167ec6-ef17-11ed-a15b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'a40823b0-ef19-11ed-a05b-0242ac120003',
    'Avez-vous des recommandations pour rendre “Mon Espace Santé” plus utile et plus agréable à utiliser ?',
    6,
    'ouverte',
    null,
    null,
    '6d82222a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    '95167ec6-ef17-11ed-a15b-0252ac120010',
    'Globalement, pensez-vous que le numérique en santé est plutôt …',
    7,
    'unique',
    null,
    null,
    '6d82222a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3932220a-ef19-11ed-a05b-0242ac120002',
    'Un levier à utiliser',
    1,
    '95167ec6-ef17-11ed-a15b-0252ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO choixpossible(id, label, ordre, question_id) VALUES (
    '3932220a-ef19-11ed-a05b-0242ac120102',
    'Une menace dont il faut se prévenir',
    2,
    '95167ec6-ef17-11ed-a15b-0252ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO questions(id, title, ordre, type, description, max_choices, consultation_id) VALUES (
    'a40823b0-ef19-11ed-a05b-0242ac120100',
    'Avez-vous une idée ou une remarque sur la question du numérique et de la santé dont vous aimeriez nous faire part ?',
    8,
    'ouverte',
    null,
    null,
    '6d82222a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO consultation_updates(id, step, description, consultation_id) VALUES (
    '72682956-094b-423b-9086-9ec4f8ef2002',
    1,
    '<body>\ud83d\udc49 Le Ministre de la Santé recevra les résultats et viendra annoncer les enseignements pour la suite et les actions qui découleront de vos réponses le <b>26 juin à 10h</b>.<br/>Il s’agira notamment de :<br/><br/><ul><li>Faire un <b>premier bilan</b> de la perception du numérique en santé,</li><li>Faire émerger de <b>nouvelles pistes d’action</b>.</li></ul>—<br/><br/><b>\ud83e\ude7a Envie d’aller plus loin ?</b><br/>Rendez-vous ici (<a href="https://www.monespacesante.fr/">https://www.monespacesante.fr/</a>) pour activer Mon Espace Santé.</body>',
    '6d82222a-ee71-11ed-a05b-0242ac120010'
) ON CONFLICT DO NOTHING;

INSERT INTO agora_users(id, device_id, password, fcm_token, created_date, authorization_level, is_banned) VALUES (
    'bacc967d-cb6c-4b43-b64d-71fbcf1f0a45',
    'deviceId-de-test1',
    '',
    'fake-fcm-token1',
    '2023-04-02',
    '0',
    '0'
) ON CONFLICT DO NOTHING;

INSERT INTO agora_users(id, device_id, password, fcm_token, created_date, authorization_level, is_banned) VALUES (
    '2a4b03d6-c575-4a65-bf49-f52ecd54a413',
    'deviceId-de-test2',
    '',
    'fake-fcm-token2',
    '2023-04-01',
    '0',
    '0'
) ON CONFLICT DO NOTHING;
