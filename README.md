# 🎯 Bouleto

**Bouleto** est une application mobile Android fun et conviviale conçue pour comptabiliser les "moments de solitude" de vos amis ou de votre famille.

## 📝 Description du concept

Bouleto permet à un groupe d'amis ou à une famille de noter et d'enregistrer des **points boulets**.

**Qu'est-ce qu'un point boulet ?**
Les points boulets sont attribués lorsqu'une personne fait une bêtise, un oubli maladroit ou une action amusante qui désespère son entourage.
*   *Exemple 1 :* Perdre ses lunettes en faisant de la moto.
*   *Exemple 2 :* Mettre son t-shirt à l'envers.

L'objectif est de garder une trace de ces anecdotes mémorables et de déterminer, avec humour, qui est le plus grand "boulet" du groupe !

## ✨ Fonctionnalités principales

L'application s'articule autour de trois axes majeurs : la gestion de groupes, le tableau des scores et la géolocalisation des anecdotes.

### 1. Gestion des Groupes 👥
*   **Multi-groupes :** Créez différents groupes (ex: "Famille", "Colocs", "Potes du lycée") via un menu latéral (Burger Menu).
*   **Membres :** Ajoutez les membres participant à la compétition dans chaque groupe.

### 2. Tableau des Scores (Accueil) 🏆
*   **Classement en temps réel :** Visualisez instantanément le total des points de chaque membre du groupe sélectionné.
*   **Ajout de points :** Un système de popup rapide permet d'ajouter des points à un membre.
    *   Sélection du membre fautif.
    *   Définition du nombre de points (le score).
    *   Ajout d'une description de la bêtise.
    *   **Enregistrement automatique de la position GPS** (Latitude/Longitude) de l'événement.

### 3. Carte Interactive (Maps) 📍
*   **Visualisation :** Retrouvez tous les lieux où les "actions boulets" se sont déroulées sur une carte interactive.
*   **Souvenirs :** Chaque pin sur la carte permet de se remémorer l'anecdote associée au lieu.
*   **Filtres :** (À venir) Distinction des groupes par couleurs de marqueurs.

### 4. Paramètres ⚙️
*   Configuration de l'application et gestion des préférences utilisateur.

## 📱 Interface Utilisateur & Navigation

L'application est construite avec **Jetpack Compose** pour une interface moderne et fluide.
*   **Barre de navigation (Bottom Bar) :** Accès rapide aux 3 écrans principaux (Accueil, Carte, Paramètres).
*   **Menu Latéral (Drawer) :** Pour changer de groupe ou en créer un nouveau facilement.

## 🛠️ Stack Technique

Ce projet est développé en **Kotlin** natif pour Android.

*   **Langage :** Kotlin
*   **UI Toolkit :** Jetpack Compose (Material Design 3)
*   **Architecture :** MVVM (Model-View-ViewModel)
*   **Base de données locale :** Room Database (SQLite)
*   **Sérialisation JSON :** Moshi
*   **Navigation :** Navigation Compose & Gestion d'états
*   **Géolocalisation :** Intégration Google Maps / Coordonnées GPS

## 🎨 Maquettes et Design

Le design de l'application a été pensé pour être simple et efficace. Vous pouvez consulter les maquettes interactives ici :

👉 **[Voir la maquette Figma](https://www.figma.com/make/H7v3vDfTMRsYF7SHvStGf7/Bouleto-App-Mockup?node-id=0-1&p=f&t=fF9BwRvJbcVZ81IC-0&fullscreen=1)**

---

