📅 Date : 22 juillet 2024

## Contexte

- Nous sommes en train de remplacer certaines tables de notre base de données par un CMS headless, Strapi. 
- Lors de notre modélisation sur Strapi, les contenus de consultations ne sont plus génériques et ils ont été divisés en 4 types différents : avant réponse, après réponse, autre et à venir.
- Il existe une route qui permet de récupérer un contenu d'une consultation via son id "GET /consultation/{id}/contenu/{id}".
- Cependant, Strapi n'utilise pas d'UUID mais un incrément de nombre entiers commençant à 1. 
- Entre les 4 types de contenus différents, une collision d'id est possible.
- Par exemple, je souhaite récupérer le contenu 8 de la consultation 12. Il est fort probable que cet id "8" soit utilisé par le contenu avant réponse et le contenu autre. En appelant la route, on pourra donc récupérer l'un des deux contenu, qui ne sera pas forcément celui attendu.

## Options envisagées 💡

### Utiliser un id forgé
Modifier les ids de Strapi dans l'API en les préfixant de leur type. Par exemple "avant-8" pour l'id 8 d'un contenu "avant réponse".

✅ Avantages : rapide à faire

🚫 Inconvénients : on doit le forger et déforger les id partout et on peut vite l'oublier, ce qui causerait des bugs

### Mettre une distance entre les ids Strapi
Lancer une requête SQL qui modifierait le compteur d'id Strapi, afin de mettre une distance entre les types.
Par exemple faire commencer les ids "avant réponse" à 6000 et les ids "après réponse" à 12000. Avec une distance de 6000, et sachant que l'on n'a qu'une vingtaine de contenu par an, il est très peu probable qu'une collision se produise.

✅ Avantages : rapide à faire, aucune modification de code

🚫 Inconvénients : on peut l'oublier et ne pas le faire sur un nouvel environnement

### Séparer la récupération des contenus via des routes distinctes selon leur type
On pourrait créer une route par contenu, par exemple "GET /consultation/{id}/contenu-avant/{id}", "GET /consultation/{id}/contenu-apres/{id}"...
Ca dupliquerait le code 4 fois et on devrait aussi modifier l'application mobile qui appelerait une URL différente selon le type de contenu.

✅ Avantages : /

🚫 Inconvénients : routes moins génériques, compliqué à mettre en place, gros refactos 

## Décision 🏆
Mettre une distance entre les ids Strapi via des `ALTER SEQUENCE consultations_avant_reponse_id_seq RESTART WITH 6000;`

## Conséquences
On doit lancer 4 requêtes SQL par serveur. 
Les ids ne commencent plus par 1 pour les contenus.
