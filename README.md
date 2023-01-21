# Laboratory 2 – Diving deeper with Neo4j

## Elèves

- Bédrunes Nicolas
- Monaco Pierre-Benjamin

## Installation
1. Cloner le repo `git clone git@github.com:IamFonky/mse-advDaBa-2022.git`
2. Accéder au repo cloné `cd mse-advDaBa-2022`
3. Placer le fichier dblpv13.json à la racine du projet
4. Lancer les deux containers `./build.sh`

## Déroulement du projet

Le but de ce laboratoire est de mettre en place une méthode permettant de charger un "gros" fichier (environ 18 Go) au format JSON, dans le système de gestion de base de données Neo4j, qui est basé sur les graphes.  

A partir du début de projet déjà fourni, nous avons commencé par faire des requêtes en Cypher sur la base de donnée depuis le programme en Java, afin de vérifier la bonne exécution de cette fonction.  
Cepandant, nous avons eu des difficultés à utiliser l'outil APOC qui permet de charger un fichier JSON dans la base de donnée. Nous avons donc développé un parser manuel, avant de finalement réussir à utiliser APOC.

La difficulté de ce projet est de traiter un fichier dont la taille dépasse largement la quantité de mémoire RAM mise à notre disposition. Nous avons pour cela réalisé un prétraitement qui génère des "petits" fichiers d'environ 2 Go qui tiennent donc dans la mémoire de 4 Go disponible. Nous pouvons ensuite les charger un par un dans la base de donnée.

## Résultats

Voici les résultats que nous avons obtenus :

| Nodes           | Temps [h:m:s] |
|----------------:|:-------------:|
| 1 000           |  00:00:22     |
| 2 000           |  00:00:30     |
| 3 000           |  00:00:55     |
| 4 000           |  00:01:24     |
| 5 000           |  00:02:29     |
| 6 000           |  00:03:10     |
| 7 000           |  00:03:25     |
| 8 000           |  00:05:28     |
| 10 000          |  00:05:30     |

