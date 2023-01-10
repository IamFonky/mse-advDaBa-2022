# Laboratory 2 – Diving deeper with Neo4j

## Elèves

- Bédrunes Nicolas
- Monaco Pierre-Benjamin

## Déroulement du projet

Le but de ce laboratoire est de mettre en place une méthode permettant de charger un "gros" fichier (environ 18 Go) au format JSON, dans le système de gestion de base de données Neo4j, qui est basé sur les graphes.  

A partir du début de projet déjà fourni, nous avons commencé par faire des requêtes en Cypher sur la base de donnée depuis le programme en Java, afin de vérifier la bonne exécution de cette fonction.  
Cepandant, nous avons eu des difficultés à utiliser l'outil APOC qui permet de charger un fichier JSON dans la base de donnée. Nous avons donc développé un parser manuel, avant de finalement réussir à utiliser APOC.

La difficulté de ce projet est de traiter un fichier dont la taille dépasse largement la quantité de mémoire RAM mise à notre disposition. Nous avons pour cela réalisé un prétraitement qui génère des "petits" fichiers d'environ 2 Go qui tiennent donc dans la mémoire de 3 Go disponible. Nous pouvons ensuite les charger un par un dans la base de donnée.

## Résultats

Nous arrivons à charger 10 000 noeuds en 36 secondes avec 3Go de RAM.
