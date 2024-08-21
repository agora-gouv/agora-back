📅 Date : 29 juillet 2024

## Contexte

Même chose que pour l'ADR [2024-07-22-regenerer-les-ids-de-contenu-strapi.md](2024-07-22-regenerer-les-ids-de-contenu-strapi.md). 

## Options envisagées 💡

Même chose que pour l'ADR [2024-07-22-regenerer-les-ids-de-contenu-strapi.md](2024-07-22-regenerer-les-ids-de-contenu-strapi.md).

## Décision 🏆
Mettre une distance entre les ids Strapi via des `ALTER SEQUENCE consultations_avant_reponse_id_seq RESTART WITH 6000;` (cf le fichier 2024-07-29-id-questions.sql du repo agora-cms-strapi)

## Conséquences
On doit lancer 5 requêtes SQL par serveur. 
Les ids ne commencent plus par 1 pour les questions.
Si on crée un nouveau type de question, on devra abonder son id de départ. 
