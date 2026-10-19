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

- Docker et Docker Compose
- Nginx (service du frontend Angular compilé)
- Tailscale Funnel (publication HTTPS du jeu sur Internet)
- Ubuntu 24.04 LTS (machine qui héberge actuellement le jeu)

---

# 📁 Structure du projet

```text
XVdereve/
├── docker-compose.yml
├── .gitignore
├── .env                         # local, non versionné
├── xvdereve-back/
│   ├── src/
│   ├── database/
│   │   └── init/               # initialisation PostgreSQL
│   ├── csv/
│   ├── pom.xml
│   ├── mvnw
│   └── Dockerfile
└── xvdereve-front/
    ├── src/
    ├── public/
    ├── package.json
    ├── angular.json
    ├── nginx.conf
    └── Dockerfile
```

Le dossier `node_modules/`, les sauvegardes et le fichier `.env` ne doivent pas être versionnés.

---

# 🚀 Installation et lancement avec Docker

## Prérequis

- Git
- Docker Engine (ou Docker Desktop)
- Docker Compose

```bash
docker --version
docker compose version
```

## 1. Récupérer le code

```bash
git clone git@github.com:matteodenee/XVdereve.git
cd XVdereve
```

Le clonage en SSH nécessite une clé GitHub configurée. On peut aussi utiliser l'URL HTTPS du dépôt.

## 2. Configurer PostgreSQL

La configuration Docker attend les variables suivantes dans un fichier `.env` **local**, à la racine du projet :

```dotenv
POSTGRES_DB=xvdereve
POSTGRES_USER=xvdereve
POSTGRES_PASSWORD=<mot-de-passe-fort-a-definir>
```

Ne jamais commiter `.env` ni copier un mot de passe réel dans le README. Les variables doivent être utilisées dans les services `postgres` et `backend` de `docker-compose.yml`. Sur une base **déjà initialisée**, modifier `POSTGRES_PASSWORD` dans `.env` ne modifie pas le mot de passe enregistré dans PostgreSQL : il faut également le changer dans la base.

> **État du déploiement :** les paramètres sont externalisés dans `.env` uniquement si les changements de configuration correspondants ont été appliqués. Vérifier `docker-compose.yml` avant de lancer une nouvelle installation.

## 3. Construire et démarrer

```bash
docker compose config --quiet
docker compose build frontend backend
docker compose up -d
```

`-d` démarre les services en arrière-plan. Le premier build peut être plus long. Lors des prochains démarrages, si le code n'a pas changé :

```bash
docker compose up -d
```

Vérifier le fonctionnement :

```bash
docker compose ps
docker compose logs --tail=50 backend
```

## Architecture de production

```text
Navigateur ── HTTPS ── Tailscale Funnel ── localhost:4200
                                               │
                                       Nginx + Angular
                                               │ /api
                                         Spring Boot
                                               │
                                          PostgreSQL
```

- Angular est **compilé en mode production** et servi par Nginx (pas par `ng serve`).
- Nginx transmet `/api/` au service Docker `backend:8080`.
- Spring Boot se connecte à `postgres:5432` via le réseau Docker.
- En production, le port `4200` est publié sur `127.0.0.1` seulement ; `8080` et `5432` ne sont **pas exposés à l'hôte**.

---

# 🌐 Accès au jeu

### Accès local (sur le PC serveur)

**http://localhost:4200**

### Accès public HTTPS

**[Jouer à XV de Rêve](https://matteodne-vivobook-asuslaptop-x412da-x412da.tail8b29b4.ts.net)**

L'application est publiée avec **Tailscale Funnel**. Les visiteurs n'ont pas besoin de compte Tailscale ni de VPN. L'adresse est associée au nom Tailscale de la machine : elle peut changer si l'appareil est supprimé, renommé ou si la configuration du réseau est modifiée.

Le service reste accessible uniquement si le PC est **allumé, connecté à Internet**, que les conteneurs fonctionnent et que Funnel est connecté. Funnel est une solution gratuite pour ce petit projet personnel, avec des limites de débit et sans garantie d'hébergement professionnel.

### Vérifier le tunnel

```bash
tailscale status
tailscale funnel status
tailscale netcheck
```

Pour publier le service sur une machine Tailscale déjà authentifiée (avec autorisation Funnel) :

```bash
sudo tailscale funnel --bg 4200
```

Tester le lien public depuis un autre réseau (4G/5G, par exemple). Un nouveau lien peut nécessiter un délai de propagation DNS.

### Démarrage automatique

```bash
sudo systemctl enable --now docker
sudo systemctl enable --now tailscaled
```

Les services Docker utilisent `restart: unless-stopped`, et la configuration Funnel en arrière-plan est persistante. Après un redémarrage, vérifier tout de même `docker compose ps` et `tailscale funnel status`.

Sur le PC Ubuntu, **désactiver la suspension automatique sur secteur** si le jeu doit rester accessible. L'écran peut être éteint ; le PC ne doit pas être mis en veille. Garder une bonne ventilation.

### NordVPN : attention au conflit réseau

Sur le serveur actuel, **l'application NordVPN Linux connectée bloque les relais Tailscale et rend Funnel inaccessible**, même si `Funnel on` reste affiché. Pour maintenir le jeu en ligne, laisser le VPN système déconnecté :

```bash
nordvpn disconnect
```

Il est possible d'utiliser l'extension NordVPN de Firefox pour la navigation dans ce navigateur uniquement, sans affecter tout le trafic du PC. Ne pas désactiver durablement le pare-feu NordVPN pour contourner ce problème.

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

# 🐳 Commandes utiles pour l'exploitation

À exécuter depuis la racine du dépôt :

| Action | Commande |
| --- | --- |
| Démarrer en arrière-plan | `docker compose up -d` |
| Voir les services | `docker compose ps` |
| Suivre les logs | `docker compose logs -f` |
| Voir les derniers logs backend | `docker compose logs --tail=80 backend` |
| Reconstruire le frontend uniquement | `docker compose up -d --build frontend` |
| Reconstruire le backend uniquement | `docker compose up -d --build backend` |
| Arrêter les services | `docker compose down` |
| Vérifier la configuration | `docker compose config --quiet` |

**Ne pas utiliser `docker compose down -v`** sur la machine serveur : cela supprimerait les volumes associés, notamment les données PostgreSQL.

## Mettre à jour le serveur depuis GitHub

Sur le PC serveur :

```bash
cd ~/XVdereve
git status
git pull --ff-only origin main
docker compose up -d --build
```

Vérifier que le répertoire de travail ne contient pas de modifications locales non sauvegardées avant le `pull`. Pour une simple modification d'Angular, on peut ne reconstruire que `frontend` ; pour une modification Java, que `backend`.

Une reconstruction ne supprime normalement pas le volume PostgreSQL. Il peut y avoir une courte indisponibilité pendant le redémarrage des services.

## Sauvegarder PostgreSQL

```bash
mkdir -p ~/XVdereve-backups
docker exec xvdereve-postgres \
  pg_dump -U xvdereve -d xvdereve -Fc \
  > ~/XVdereve-backups/xvdereve.dump
```

La sauvegarde est placée **hors du dépôt Git**. Vérifier qu'elle est non vide et prévoir périodiquement une copie sur un autre support. La présence d'un volume Docker ne remplace pas les sauvegardes.

## Lancement manuel pour le développement

Le serveur Docker de production est indépendant des commandes de développement ci-dessous.

Backend (avec PostgreSQL accessible dans l'environnement local et des variables de connexion adaptées) :

```bash
cd xvdereve-back
chmod +x mvnw
./mvnw spring-boot:run
```

Frontend, dans un autre terminal :

```bash
cd xvdereve-front
npm ci
npm start
```

Le serveur Angular de développement utilise `localhost:4200`. Attention aux conflits de port si Nginx Docker publie déjà ce port.

## Dépannage

### L'accueil ne charge pas

```bash
curl -I --max-time 5 http://127.0.0.1:4200
docker compose ps
docker compose logs --tail=50 frontend
```

### La page s'affiche mais la partie ne démarre pas

```bash
docker compose logs --tail=100 backend
```

Contrôler la connexion PostgreSQL et les appels `/api/` dans les outils de développement du navigateur.

### Erreur Hibernate de validation du schéma

Sur la machine de déploiement initiale, les colonnes `player.overall` et `team.year` ont été converties de `SMALLINT` en `INTEGER` pour correspondre aux entités Java.

**Important :** cette correction manuelle de la base existante n'assure pas à elle seule qu'une base neuve sera compatible. Le schéma SQL versionné doit être harmonisé ou une migration SQL doit être créée avant un déploiement sur une nouvelle machine. Conserver `spring.jpa.hibernate.ddl-auto=validate` pour éviter les modifications automatiques imprévues du schéma.

### Funnel inaccessible alors que `Funnel on` est affiché

```bash
tailscale netcheck
nordvpn status
```

Si NordVPN Linux est connecté, il peut bloquer les relais DERP de Tailscale. Le déconnecter sur cette installation puis retester depuis un appareil hors du réseau local.

### Docker non disponible

```bash
systemctl status docker --no-pager
```

### Base PostgreSQL et réinitialisation

Les données sont persistées dans le volume Docker `xvdereve_postgres_data` sur le serveur actuel. `docker compose down` ne supprime pas ce volume. **Ne jamais supprimer le volume pour résoudre une erreur sans sauvegarde et diagnostic préalable.**

---

# 🔒 Sécurité du déploiement public

- L'API `/api/` est accessible via le lien public : ne pas y laisser d'opérations d'administration ou de données sensibles sans protection.
- Garder PostgreSQL et le port interne Spring Boot non publiés ; seul Nginx est joignable localement via `127.0.0.1:4200`.
- Ne pas versionner `.env`, les dumps SQL, les jetons d'authentification ou les clés Tailscale.
- Utiliser un mot de passe PostgreSQL fort et distinct ; si une base existe déjà, changer le mot de passe **dans PostgreSQL également**.
- Garder Ubuntu, les images Docker et Tailscale à jour ; prévoir des sauvegardes régulières.
- Le jeu reste un hébergement personnel, pas une infrastructure avec disponibilité garantie.

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
