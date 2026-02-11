# PROJET: (YughiCards) Magasin de carte avec AI chat bot 

### 26 janvier: Réunion d’équipe 
- Discussion sur le projet 
- Trouver une idée de projet 
- Recherche de AI à implémenté 

---

### 29 janvier: Projet choisi 
- Magasin de carte 
- springboot pour fait les requêtes HTTP vers l’API 
- API YGOProDeck 
- react pour linterface web 
- springAI pour AI chatbox 
- AI framework Ollama avec le model d’IA appeler llama3.1:8b 

---

### 2 février: Générer/Crée un projet vide avec les dépendances nécessaire pour le backend 

- **Regarder** une partie du vidéo sure Sprint AI “Bootiful Spring AI by Mark Pollack, Christian Tzolov, Josh Long, James Ward” pour avoir une idée d’où commencer. 

- **Utiliser** Sprint initializr pour générer notre projet avec certains des dépendances vues dans le vidéo plus autre dépendances (pour éviter boiler-plate et modifier pour notre stack) 

    **Les dépendances installées sont**: 

        spring-boot-starter-data-jpa 
        jakarta.persistence-api 
        spring-data-jpa 
        spring-boot-starter-jdbc 
        spring-boot-starter-webmvc 
        spring-ai-advisors-vector-store 
        spring-ai-starter-model-chat-memory-repository-jdbc 
        spring-ai-starter-model-ollama 
        spring-ai-starter-vector-store-pgvector 
        spring-boot-devtools 
        postgresql 
        lombok 
        spring-boot-starter-jdbc-test 
        spring-boot-starter-webmvc-test 
        spring-boot-docker-compose 

- **Installation** de l’outile Ollama pour installer le model d’IA appeler llama3.1:8b, pour assurer que ça roule sur nos machines et qu’on peut l’utiliser dans notre plus tard. 

- **Installation** postgres avec l’utilisateur nommé yugi pour assurer que postgres funcitone sur nos machines. 

- **Liens**: 
    - [Spring Initializr](https://start.spring.io/)
    - [Vidé sur Spring Ai](https://www.youtube.com/watch?v=Sw3PlFXfWj4&t=1039s)
    - [Lien pour Postgres](https://www.postgresql.org/download/)
    - [Lien pour ollama](https://www.postgresql.org/download/) 

---

### 3 février : (6pm - 7pm) Initialisation du projet React et analyse des designs de sites vendant des cartes 

- **Installation** React et ReactDOM : pour pouvoir utiliser React dans le projet et créer des composants interactifs. 

- **Installation** Vite et du plugin React : pour bénéficier d’un environnement de développement rapide et moderne. 

- **Analyse** des designs de sites concurrents : 

- **Étudier** la palette de couleurs utilisée pour comprendre les tendances visuelles et identifier les choix attractifs pour les utilisateurs. 

- **Observer** les patterns et mises en page : grilles, cartes produit, menus, sections d’information, et organisation générale des pages. 

- **Noter** l’ergonomie et l’expérience utilisateur : navigation, filtres, affichage des cartes, interactions avec les éléments, temps de chargement. 

- **Identifier** les bonnes pratiques : boutons visibles, hiérarchie de l’information, design responsive adapté à différents écrans. 

- **Relever** les éléments inspirants pour le futur design de notre site : typographie, effets au survol (hover), animations simples, structure et organisation des pages. 

- **Sites consultés** :
    - [Gamekeeper Verdun](https://www.gamekeeperverdun.com/)  
    - [Face to Face Games](https://facetofacegames.com/fr) 
    - [Card Brawlers](https://cardbrawlers.com/fr?srsltid=AfmBOopfUu7CuW3hBwMltFG0_iT5hB14mGQeYENyE0FRZ5ZcwXiVw4Zw) 
    - [Carta Magica](https://www.cartamagica.com/) 
    - [Expedition Jeux](https://www.expeditionjeux.com/) 

---

### 4 février : (7pm - 9pm) Implémentation des pages, configuration de la navigation et navbar 

- **Création** de la page Home

- **Définition** de la route par défaut : Configuration du point d'entrée de l'application pour que la page Home s'affiche automatiquement à l'ouverture du site 

- **Installer** tailwind: 

        npm install -D tailwindcss@3 

        npx tailwindcss init (fichier tailwind.config.js) 
        
        npx tailwindcss init –p (fichier postcss.config.js) 
        
- **Implémentation de la Navbar** : 

    - **Recherche** : Chercher de l’Inspiration des navbar des sites de magasins de cartes consultés le 3 février. 

    - **Nom du magasin** : affichage du nom du site cliquable (Yughi-Cards) menant à la page d'accueil. 

    - **Éléments de navigation** : liens vers les pages Home, Vendre, À propos, Contact pour faciliter la navigation. 

    - **Icône panier** : ajout d’une icône pour accéder au panier. 

    - **Icône connexion** : ajout d’une icône pour accéder à la page de connexion. 

    - **Sélecteur de langue** : possibilité de basculer entre les langues disponibles (fr et en) via un menu déroulant contrôlé par l’état React. 

    - **Couleur et style** 

    - **Langue** : Ajouté le français et l’anglais pour les éléments de la navbar 
 
---

### 4 février : (8am – 1pm) Backend - Initialisation projet Spring Boot  

- **Configuration** / Debuggé intelliJ pour que ça connecte à la base de données PostgreSQL (user : yugi) 

- **Configuration** / Debuggé compose.yalm : pour avoir un environnement de développement cohérents, éphémères et isolés pendant le développement. 

- **Analysé** l’API ([ygoprodeck](https://ygoprodeck.com/api-guide/)) pour avoir une idée comment structuré les données pour les cartes de notre magasin. 

- **Remue-méningesdes** pour trouver les acteurs de notre application: 

    - **ApplicaitonUser**
        - représente l’utilisateur (Nom, Numéro cellulaire et Courriel). 

    - **ShopingCart**
        - représente le carte panier de l’utilisateur et contiendra les cartes dont il prévoit acheter. 

    - **Yughiocard** 
        - (MonsterCard, SpellCard, TrapCard), les cartes à vendre dans le magasin. 

- **Implémentation** des classes models(les acteurs mentionné) avec des classes services et classes repository qui les sont associé. 

---

### 5 février : (6pm - 9pm) Implémentation, configuration et navigation du footer + page AboutUs 

- Footer 

    - **Recherche** : Inspiration tirée des footers de sites de magasins de cartes consultés le 3 février. 

    - **Nom du magasin** : affichage du nom du site cliquable (Yughi-Cards) menant à la page d'accueil. 

    - **Éléments de navigation** : liens vers les pages Home, Vendre, À propos et Contact pour faciliter la navigation. 

    - **Icônes des réseaux sociaux** : icônes Facebook, Instagram et X (anciennement Twitter) pour donner l’esthétique d’un vrai magasin de cartes. 

    - **Informations concernant le magasin** : numéro de téléphone, adresse et heures d’ouverture. 

    - **Droits** : mention des droits d’auteur (« Tous droits réservés » / « All rights reserved ») 

    - **Design** : Structuration d’une interface claire et bien organisée pour le footer 

 

    - **Page aboutUs** 

        - **Implémentation** de la page AboutUs pour présenter l’identité, l’histoire et les valeurs du magasin Yughi-Cards. 

        - **Internationalisation (i18n)** : utilisation du fichier de traductions afin d’adapter automatiquement le contenu de la page selon la langue sélectionnée (français / anglais). 

            - **Structure de la page** : 

                - Section d’en-tête avec le titre et un séparateur visuel. 

                - Section « Notre histoire » décrivant l’origine et la mission du magasin. 

                - Section « Ce qui nous rend uniques » mettant en valeur l’authenticité des cartes, les prix, la qualité du service et la diversité de l’inventaire à l’aide d’icônes et d’une disposition en grille    

                - Section « Notre engagement » présentant les valeurs et l’engagement envers les clients. 

                - Design : mise en page moderne avec des cartes (cards), des ombres, une hiérarchie claire des titres et une palette de couleurs cohérente avec l’identité visuelle du site. 

--- 

### 11 févrié: (4pm - 6pm) FE - Implémentation de la page contactUs
   - **Recherche** : Inspiration tirée des pages contactUs des magasins de cartes consultés le 3 février.
     
   - **En-tête** : Mise en place du titre principal, d’un séparateur visuel et d’un sous-titre pour introduire la section contact.
     
   - **Introduction** : Ajout d’une section expliquant comment prendre contact avec le magasin, avec une mise en page claire et lisible.

   - **Informations de contact** :
        - Numéro de téléphone : Afficher le numéro de téléphone.
    
        - Email : Cliquable, ouvre Gmail dans un nouvel onglet.
    
        - Adresse : Cliquable et ouvre Google Maps dans un nouvel onglet avec l'adresse.
    
        - Design : Chaque information est dans une card avec une icône colorée.
    
        - Heures d’ouverture : Affichage des horaires de semaine et du weekend.
    
        - Réseaux sociaux : Ajout des icônes Facebook, Instagram et X (Twitter) avec animations au survol (changement de couleur).
    
        - Carte Google Maps : Intégration d’un iframe avec l’adresse du magasin(cegep andré-laurendeau).
    
        - Message d’encouragement : Section finale colorée (rouge) avec texte invitant les visiteurs à venir et indiquant la disponibilité de l’équipe pour répondre aux questions.
    
        - Internationalisation (i18n) : Toutes les sections utilisent les traductions pour adapter le contenu automatiquement selon la langue sélectionnée (français / anglais).
 

 
















   
 

 
