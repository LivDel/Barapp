# 🍸 Le Bar'app

Bienvenue sur le dépôt du projet **Le Bar'app**, une application moderne de gestion de bar intégrant une vue client pour commander et une vue "Barmaker" pour la gestion des commandes en temps réel.

---

## 🎯 Fonctionnalités Principales

- **📱 Vue Client (Mobile-First)** :
  - Consultation de la carte des cocktails avec filtrage par catégorie.
  - Gestion du panier et choix des tailles de boissons.
  - Suivi de la commande en temps réel avec pourcentage de progression (En attente, Ingrédients, Assemblage, Dressage, Prêt).
- **🍹 Vue Barmaker (Tableau de bord Barman)** :
  - Authentification sécurisée du Barman.
  - Swipe horizontal des commandes actives en direct.
  - Détail des étapes de préparation par cocktail (design Glassmorphism).
  - Panneau d'administration de la carte (gestion des cocktails, tailles, catégories).
- **🗄️ Initialisation Automatique** :
  - Base de données pré-peuplée avec des ingrédients, tailles, catégories, barmen et cocktails de démonstration au démarrage de l'application.

---

## 🛠️ Stack Technique

- **Frontend** : Vue.js 3 (Composition API), TypeScript, Vite, CSS Vanilla (Glassmorphism UI).
- **Backend** : Java Spring Boot 3, Spring Web, Spring Data JPA.
- **Base de données** : PostgreSQL.

---

## 🚀 Lancement Rapide (Localhost)

Ce guide détaille comment lancer l'application en environnement local de développement. *(L'intégration avec Docker sera ajoutée prochainement).*

### 1. Prérequis

Assurez-vous d'avoir installé les éléments suivants sur votre machine :
- **Node.js** (v16+ recommandé) et **npm**.
- **Java JDK** (v17+ recommandé).
- **Maven** (installé localement ou via l'IDE).
- **PostgreSQL** installé et démarré.

### 2. Base de données (PostgreSQL)

Créez une base de données nommée `barapp` sur votre serveur PostgreSQL local.
L'application s'attend par défaut aux identifiants suivants (modifiables dans `backend/src/main/resources/application.properties`) :
- **Host** : localhost
- **Port** : 5432
- **Utilisateur** : `postgres`
- **Mot de passe** : `postgres`

*L'application s'occupera toute seule de créer les tables et de générer les données (cocktails, barmen) grâce au `ddl-auto=create` et au composant `DataInitConfig`.*

### 3. Démarrer le Backend (Spring Boot)

Ouvrez un terminal dans le répertoire `backend` :
```bash
cd backend
mvn spring-boot:run
```
Le serveur Spring Boot démarrera sur le port **8080** par défaut : `http://localhost:8080`.

### 4. Démarrer le Frontend (Vue.js)

Ouvrez un second terminal dans le répertoire `frontend` :
```bash
cd frontend
npm install
npm run dev
```
Le serveur de développement Vite démarrera. Rendez-vous sur le lien affiché dans votre terminal (généralement `http://localhost:5173`).

---

## 🧭 Structure de navigation

- `http://localhost:5173/menu` : La carte pour les clients.
- `http://localhost:5173/barmaker/login` : Page de connexion pour le barman.
  - **Identifiant par défaut** : `barmaker1`
  - **Mot de passe par défaut** : `password123`

---

## 🎨 Note sur le Design

L'application a été développée avec une approche **Mobile-First** (priorité aux affichages sur smartphones) et intègre des concepts UI modernes comme le *Glassmorphism*, et les dégradés fluides pour une expérience premium.

---
*Développé pour sublimer l'expérience en bar.*
