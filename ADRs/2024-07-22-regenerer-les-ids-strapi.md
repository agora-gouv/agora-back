📅 Date : 22 juillet 2024

## Contexte

- Nous sommes en train de remplacer certaines tables de notre base de données par un CMS headless, Strapi. 
- Lors de notre modélisation sur Strapi, les contenus de consultations ne sont plus génériques et ils ont été divisés en 4 types différents : avant réponse, après réponse, autre et à venir.
- Il existe une route qui permet de récupérer le contenu d'une consultation via son id. "GET /consultation/{id}/"
- Cependant, Strapi n'utilise pas d'UUID donc une collision d'id est possible.
- Par exemple, je souhaite récupérer le contenu 8 de la consultation 12. Il est probable que cet id "8" soit utilisé par le contenu avant réponse et le contenu autre.

## Options envisagées 💡

### Utiliser un id forgé
✅ Avantages : rapide à faire

🚫 Inconvénients : on doit le modifier partout et on peut vite l'oublier, ce qui causerait des bugs

### Mettre une distance entre les ids Strapi
Lancer une requête SQL qui modifierait le compteur d'id Strapi, afin de mettre une distance entre les types.

✅ Avantages : rapide à faire, aucune modification de code

🚫 Inconvénients : on peut l'oublier et ne pas le faire sur un nouvel environnement

### séparer la récupération des contenus via des routes distinctes selon leur type
✅ Avantages : /

🚫 Inconvénients : routes moins génériques, compliqué à mettre en place, gros refactos 

## Décision 🏆
Mettre une distance entre les ids Strapi via des `ALTER SEQUENCE consultations_avant_reponse_id_seq RESTART WITH 6000;`

## Conséquences
On doit lancer 4 requêtes SQL par serveur.
