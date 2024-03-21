DELETE FROM qags WHERE id = 'f29c5d6f-9838-4c57-a7ec-0612145bb0c8';
DELETE FROM responses_qag WHERE id = '38990baf-b0ed-4db0-99a5-7ec01790720e';
DELETE FROM qags WHERE id = '996436ca-ee69-11ed-a05b-0242ac120003';
DELETE FROM qags WHERE id = 'f8776dd0-ee69-11ed-a05b-0242ac120003';
DELETE FROM qags WHERE id = '40c04882-ee6a-11ed-a05b-0242ac120003';
DELETE FROM qags WHERE id = '8171c50e-ee6a-11ed-a05b-0242ac120003';
DELETE FROM qags WHERE id = '1731a370-ee6b-11ed-a05b-0242ac120003';
DELETE FROM qags WHERE id = '5c5f5460-ee6b-11ed-a05b-0242ac120003';
DELETE FROM qags WHERE id = '96e8341c-ee6b-11ed-a05b-0242ac120003';
DELETE FROM qags WHERE id = 'd09f2788-ee6b-11ed-a05b-0242ac120003';
DELETE FROM qags WHERE id = '1cde43fe-ee6c-11ed-a05b-0242ac120003';
DELETE FROM qags WHERE id = '59028e08-ee6c-11ed-a05b-0242ac120003';
DELETE FROM qag_updates WHERE id = '996436ca-ee69-11ed-a05b-0242ac120003';
DELETE FROM qag_updates WHERE id = 'f8776dd0-ee69-11ed-a05b-0242ac120003';
DELETE FROM qag_updates WHERE id = '40c04882-ee6a-11ed-a05b-0242ac120003';
DELETE FROM qag_updates WHERE id = '8171c50e-ee6a-11ed-a05b-0242ac120003';
DELETE FROM qag_updates WHERE id = '1731a370-ee6b-11ed-a05b-0242ac120003';
DELETE FROM qag_updates WHERE id = '5c5f5460-ee6b-11ed-a05b-0242ac120003';
DELETE FROM qag_updates WHERE id = '96e8341c-ee6b-11ed-a05b-0242ac120003';
DELETE FROM qag_updates WHERE id = 'd09f2788-ee6b-11ed-a05b-0242ac120003';
DELETE FROM qag_updates WHERE id = '1cde43fe-ee6c-11ed-a05b-0242ac120003';
DELETE FROM qag_updates WHERE id = '59028e08-ee6c-11ed-a05b-0242ac120003';

INSERT INTO qags(id, title, description, post_date, status, username, thematique_id, user_id) VALUES (
    'f29c5d6f-9838-4c57-a7ec-0612145bb0c8',
    'Pourquoi avoir créé l’application Agora ?',
    'Quel est le but de l’application Agora ? A quel besoin des Français et des Françaises espérez-vous qu’elle réponde ? Que signifie son caractère expérimental ?',
    '2023-09-27',
    7,
    'Agora',
    '30671310-ee62-11ed-a05b-0242ac120003',
    '00000000-0000-0000-0000-000000000000'
) ON CONFLICT DO NOTHING;

INSERT INTO responses_qag(id, author, author_portrait_url, author_description, response_date, video_url, video_width, video_height, transcription, feedback_question, additional_info_title, additional_info_description, qag_id) VALUES (
    '38990baf-b0ed-4db0-99a5-7ec01790720e',
    'Olivier Véran',
    'https://content.agora.incubateur.net/portraits/QaG-OlivierVeran.png',
    'Ministre délégué auprès de la Première ministre, chargé du Renouveau démocratique, porte-parole du Gouvernement',
    '2023-06-05',
    'https://content.agora.incubateur.net/qag_responses/pourquoiCreerAgoraQagResponse.mp4',
    479,
    690,
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
    'Le ministre a-t-il répondu à votre question ?',
    null,
    null,
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
