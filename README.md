# 🏉 XV de Rêve

**XV de Rêve** est une application web permettant de créer son équipe de rugby idéale à partir des joueurs ayant participé aux différentes Coupes du Monde, puis de la faire participer à un tournoi simulé.

Le joueur doit composer son XV poste par poste, choisir son buteur, puis affronter plusieurs équipes historiques jusqu'aux phases finales.

---

## 🎮 Fonctionnalités

### Draft

- Tirage aléatoire d'une équipe issue d'une Coupe du Monde.
- Sélection d'un joueur correspondant au poste demandé.
- Composition progressive d'un XV complet.
- Jusqu'à **3 respins** pendant la draft.
- Tri des joueurs par poste.
- Affichage des informations des joueurs et des équipes.
- Sélection d'un **buteur** une fois l'équipe terminée.

### Tournoi

Une fois le XV constitué, le joueur participe à un tournoi.

La simulation prend notamment en compte :

- la note globale de l'équipe ;
- la note de l'adversaire ;
- le niveau du buteur ;
- les essais ;
- les transformations ;
- les pénalités ;
- les différents marqueurs.

Les événements du match sont affichés progressivement afin de donner un aspect plus vivant à la simulation.

Plusieurs vitesses d'animation sont disponibles :

- `x1`
- `x2`
- `x4`
- résultat instantané

### Équipes historiques

Les joueurs proviennent de plusieurs éditions de la Coupe du Monde de rugby, notamment :

- 2011
- 2015
- 2019
- 2023

Les données sont stockées dans une base **PostgreSQL**.

---

# 🛠️ Technologies utilisées

## Frontend

- Angular
- TypeScript
- HTML
- CSS

## Backend

- Java 21
- Spring Boot
- REST API
- Maven

## Base de données

- PostgreSQL

## Infrastructure

- Docker
- Docker Compose

---

# 📁 Structure du projet

```text
XVdereve/
│
├── docker-compose.yml
│
├── database/
│   ├── init/
│   │   └── schema.sql
│   └── data/
│
├── xvdereve-back/
│   ├── src/
│   ├── csv/
│   │   ├── Player.csv
│   │   └── Team.csv
│   ├── pom.xml
│   ├── mvnw
│   └── Dockerfile
│
└── xvdereve-front/
    ├── src/
    ├── public/
    ├── package.json
    ├── angular.json
    └── Dockerfile
```

---

# 🚀 Lancer le projet

## Prérequis

Pour lancer le projet, il faut avoir installé :

- Git
- Docker
- Docker Compose

Vérifier que Docker fonctionne :

```bash
docker --version
docker compose version
```

---

## 1. Cloner le projet

```bash
git clone git@github.com:matteodenee/XVdereve.git
cd XVdereve
```

---

## 2. Lancer Docker

Il faut vérifier que **Docker Desktop** ou le daemon Docker est démarré avant de lancer le projet.

Puis, depuis la racine du projet :

```bash
docker compose up --build
```

Au premier lancement, la construction des images peut être plus longue car Docker doit télécharger les dépendances nécessaires.

Les lancements suivants sont normalement beaucoup plus rapides.

Pour démarrer sans reconstruire les images :

```bash
docker compose up
```

---

# 🌐 Accès à l'application

Une fois les conteneurs démarrés :

### Frontend Angular

```text
http://localhost:4200
```

### Backend Spring Boot

```text
http://localhost:8080
```

### PostgreSQL

```text
localhost:5432
```

---

# 🔌 API

L'API REST du jeu est disponible sous :

```text
/api/game
```

Principaux endpoints :

| Méthode | Endpoint             | Description                          |
| ------- | -------------------- | ------------------------------------ |
| `POST`  | `/api/game/start`    | Commencer une nouvelle partie        |
| `POST`  | `/api/game/respin`   | Relancer l'équipe proposée           |
| `POST`  | `/api/game/pick`     | Sélectionner un joueur               |
| `POST`  | `/api/game/kicker`   | Choisir le buteur                    |
| `POST`  | `/api/game/simulate` | Simuler un match                     |
| `GET`   | `/api/game/state`    | Récupérer l'état actuel de la partie |
| `GET`   | `/api/game/status`   | Vérifier le statut de la partie      |

---

# 🗄️ Base de données

La base PostgreSQL contient principalement deux tables.

## `team`

Contient les équipes ayant participé aux différentes éditions de la Coupe du Monde.

Principales informations :

- année ;
- pays ;
- description ;
- logo.

## `player`

Contient les joueurs associés aux équipes.

Principales informations :

- nom ;
- poste ;
- note globale ;
- capacité à tirer les pénalités et transformations ;
- équipe associée.

La relation entre les joueurs et les équipes est réalisée grâce à une clé étrangère.

---

# 🏉 Postes disponibles

Les joueurs sont répartis selon les postes suivants :

```text
Pilier
Talonneur
DeuxiemeLigne
TroisiemeLigneAile
TroisiemeLigneCentre
DemiDeMelee
DemiOuverture
Centre
Ailier
Arriere
```

La composition finale respecte l'organisation classique d'un XV de rugby.

---

# 🎲 Simulation des matchs

La simulation repose sur une comparaison entre le niveau de l'équipe du joueur et celui de l'adversaire.

Une cote de match est notamment calculée à partir de la formule :

```text
cote = 50 + (noteEquipe - noteAdversaire) × 0.7
```

Cette cote influence ensuite :

- le nombre d'occasions d'essai ;
- le nombre de pénalités ;
- les probabilités de réussite ;
- le résultat final.

Le niveau du buteur influence également la réussite des transformations et des pénalités.

---

# 🐳 Commandes Docker utiles

### Lancer le projet

```bash
docker compose up
```

### Reconstruire les images

```bash
docker compose up --build
```

### Arrêter le projet

```bash
docker compose down
```

### Voir les conteneurs actifs

```bash
docker ps
```

### Voir les logs

```bash
docker compose logs
```

### Voir les logs en direct

```bash
docker compose logs -f
```

---

# 🔧 Lancement manuel

Il est également possible de lancer le frontend et le backend séparément.

## Backend

```bash
cd xvdereve-back
chmod +x mvnw
./mvnw spring-boot:run
```

Le backend est ensuite disponible sur :

```text
http://localhost:8080
```

## Frontend

Dans un autre terminal :

```bash
cd xvdereve-front
npm install
npm start
```

ou :

```bash
ng serve
```

Le frontend est ensuite disponible sur :

```text
http://localhost:4200
```

---

# ⚠️ Problèmes fréquents

## Port 8080 déjà utilisé

Vérifier quel programme utilise le port :

```bash
sudo ss -ltnp '( sport = :8080 )'
```

Puis arrêter le conteneur ou le programme concerné.

---

## Docker n'est pas démarré

Une erreur de ce type :

```text
failed to connect to the docker API
```

signifie généralement que Docker Desktop ou le daemon Docker n'est pas lancé.

---

## Port 4200 inaccessible

Vérifier que le conteneur frontend est actif :

```bash
docker ps
```

Puis consulter les logs :

```bash
docker compose logs frontend
```

---

# 🔄 Réinitialiser la base de données

Les données PostgreSQL sont persistantes grâce à un volume Docker.

Ainsi, arrêter puis relancer les conteneurs ne supprime pas la base.

Pour reconstruire complètement la base de données, il faut supprimer son volume ou son dossier de données avant de relancer les conteneurs.

⚠️ Cette opération supprime les données présentes dans la base.

---

# 🎯 Objectif du projet

L'objectif de **XV de Rêve** est de proposer une expérience interactive autour de l'histoire de la Coupe du Monde de rugby en combinant :

- composition d'équipe ;
- données de joueurs historiques ;
- stratégie ;
- hasard ;
- simulation sportive.

---

# 👤 Auteur

**Matteo Denée**

Étudiant ingénieur en informatique à **Polytech Montpellier**.

Projet personnel autour du développement logiciel, de la data et du rugby.

---

## 📌 Repository

```text
git@github.com:matteodenee/XVdereve.git
```
