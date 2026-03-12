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

- **Regarder** une partie du vidéo sure Sprint AI **“[Bootiful Spring AI by Mark Pollack, Christian Tzolov, Josh Long, James Ward](https://www.youtube.com/watch?v=Sw3PlFXfWj4)”** pour avoir une idée d’où commencer. 

- **Remue-méninges** pour choisir les outiles et le model d'IA à utiliser pour le chatbox :
  - **Les critères pour notre IA** :
    - On cherche un outile d'IA qui nous permet de rouler des llm sur nos machines locales. 
    - Un model qui peut résonner et aider nos clients à trouver et les suggérer les bons produits selon leurs besoins. 
    - Le model doit aussi être cappable de répondre aux questions des clients qui sont bassé sur nos produits.
  - On a **choisi Ollama** : parce que c'est un outil d'IA open source et gratuit qui nous permet d'exécuter des llm sur nos machines locales.
    - **Compare** quelques models de LLM les plus récents qui sont disponibles sur [le site d'Ollama](https://ollama.com/library?sort=popular) : 
        - **llama3.1**: 
        > cette model est optimizer pour avoir des compétences en culture générale, en pilotage, en mathématiques, en utilisation d'outils et en traduction multilingue.
        
        > [O] ne nécessitent pas trops d'espace pour un model suffisant.
        
        > [O] est assez puissant pour notre projet.  
    
              - versions:
              - llama3.1: latest 4.9GB
              - llama3.1: (8b params) 4.9GB
              - llama3.1: (70b params) 43GB
              - llama3.1  (405b params) 243GB
        
        - **deepseek-r1**:
        > chaque version de cette model est un model distilé d'un plus grand model
          ce qui risque d'avoir une diminution de la précision, baisse des capacités de raisonnement et augmentation des taux d'hallucinations.
        
        > [X] nécessitent trop d'espace pour un model suffisant. 
        
        > [X] Et risque diminution de la précision, baisse des capacités de raisonnement et augmentation des taux d'hallucinations. 
    
              versions:
              (distilled version)
              - deepseek-r1: latest 5.2GB
              - deepseek-r1: (1.5b params) 1.1GB
              - deepseek-r1: (7b params) 4.7GB
              - deepseek-r1: (8b params) 5.2GB
              - deepseek-r1: (14b params) 9.0GB
              - deepseek-r1: (32b params) 20GB
              - deepseek-r1: (70b params) 43GB
              (non distilled version)
              - deepseek-r1: (671b params) 404GB
        - **llama3.2**:
        > Cette model est optimisé pour les cas d'utilisation de dialogue multilingue, y compris les tâches de récupération et de résumé automatisées.
        
        > [O] nécessitent peu d'éspace. 
        
        > [X] n'est pas assez puissant pour notre projet.
      
                versions:
                - llama3.2: latest 2GB
                - llama3.2: (1b params) 1.3GB
                - llama3.2: (3b params) 2GB

- **On choisit llma3.1 : 8b** : Parce qu'il nous semble de répondre aux besoins de notre projet et qu'il est assez puissant pour notre projet.

- **Utiliser** Sprint initializr pour générer notre projet avec certains des dépendances vues dans le vidéo et avec autre dépendances (pour éviter boiler-plate et modifier pour notre stack) (ex: lombok)

    **Les dépendances installées sont **: 

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

- ****
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

     - npm install -D tailwindcss@3 

     - npx tailwindcss init (fichier tailwind.config.js) 
        
     - npx tailwindcss init –p (fichier postcss.config.js) 
        
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

### 11 février: (4pm - 6pm) FE - Implémentation de la page contactUs
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
---

### 11 (8am - 2pm) - 12 (8am - 10pm) février : BE implémentation de l'API YGOProDeck et création de notre propre API (End-points)
- Connexion à l’API YGOProDeck

    -  **Reanalyser** l'API pour "brain storm" comment l'utiliser dans notre projet.
  
    -  **Essayer** d'implémenter un service (ApiService) qui appelle les différents endpoint de l'API de cartes.
  
        - **Crée** des methods qu'appelle les end point de l'API.
       
        - **Crée** des DTOs et des repository pour chaques types de cartes (MonsterCard, SpellCard, TrapCard).
   
    - **Réalisation** que l'implémentation des model de cartes aurait créé beaucoup de boiler-plate code :
  
        - **Recherche** alternative pour les classes de cartes.
       
        - **Lue** des blogues sur composition vs inheritance et j'ai choisi composition.
       
        - **Supprimer** les classes (MonsterCard, SpellCard, TrapCard).
       
        - **Création** des classes properties (CardProperties, MonsterCardProperties, SpellCardProperties, TrapCardProperties).
       
        - **Refactor** les DTOs, 
            - Supprimer les DTOs (DTOMonsterCard, DTOSpellCard, DTOTrapCard).
       
            - Décider d'utiliser un seul type de DTO (DTOYughioCard) et Repository (YughioCardRepository) pour tous les types de cartes.
          
            - Implementer composition des CardedProperties dans le DTO(DTOYughioCard).
          
        - les sties consulté: ([composition-in-java](https://www.geeksforgeeks.org/java/composition-in-java/)) ([composition-aggregation-association](https://www.baeldung.com/java-composition-aggregation-association))

    - **Réalisation** qu'appeler l'api dans le backend et le rediriger direct ver le frontend/controller avec un service est redundant 
        >((à chaque requet du backend)) API -> backend -> controller -> frontend) **(nx de fois) + retard du côté reponse de l'API**.
  
        - **Refactor** le service ApiService: 
      
             >(à l'instanciation de l'application) (API où fichier.Json (sauvegarde dans le)-> backend -> repository) **(1x)**
             >
             >(après à chaque requet) (repository -> backend -> controller) (Peu de retard)
      
             - **(lors de la première exécution de l'application)** : Implémentation fonctionnalité appel API pour acquérir les informations sur tout les cartes et les sauvegarder une fois dans le backend / repository
             - **(lors de la première exécution de l'application, Mais API n'est pas encore en ligne)** :
                  - **Télécharger** le fichier JSON de l'api qui contienne tout les données des cartes dans les fichiers du backend.
                  - **Implémentation** fonctionnalité appel API pour acquérir les informations du fichier JSON et les sauvegarde une fois dans le backend / repository
  
    - **Création** du service YughioCardService:
         
         -  Utilise le repo (YughioCardRepository) 
  
         -  Implémentation des méthodes qui me semble utile pour notre application.
      
    - **Création** du controller CardApiController:
  
         - Utilise YughioCardService
      
         - Apell les methods du service YughioCardService
            
         - Les end-points: 
         > 1. get card by id
         > 2. get card by name
         > 3. get all cards (can chose page and quantity of cards per page)
         > 4. get/ search for cards by name (can chose page and quantity of cards per page)
         > 5. get/ search for cards by frameType (can chose page and quantity of cards per page)
         > 6. get/ search for cards by type (can chose page and quantity of cards per page)


### 12 février: (3pm - 5pm) FE - Implémentation de la page d'accueil(partie 1) + petite mise à jour au navbar

- Navbar
   - **Choix de la langue** : Ajout de la photo du drapeau correspondant à la langue sélectionnée.

- Page d'acceuil
   - **Recherche** : Inspiration tirée des pages d'acceuil des magasins de cartes consultés le 3 février.
      
   - **Hero section** : Ajout d’une image de type Yu-Gi-Oh pour la section principale.
        - Titre principal, sous-titre et description: Texte et boutton animés avec fadeIn.
            
   - **Card section** : Section composée de cards présentant notre boutique, avec animations au survol des cards.
     
   - **i18n** : Toutes les sections utilisent les traductions pour adapter le contenu automatiquement selon la langue sélectionnée (français / anglais).
---
### 18 février: (2h30 - 4h30)) FE - Implémentation de la page VendezNous + composant pour aller en haut de la page lorsqu'on change de page

-Page vendezNous:

   - **Objectif de la page** : Expliquer en détail le processus permettant aux clients de vendre leurs cartes Yu-Gi-Oh! de manière simple.
     
   - **Processus de vente** : Présentation des étapes que doit suivre le client, depuis l’apport des cartes au magasin, jusqu’au paiement immédiat ou crédit boutique (avec bonus éventuel de 30 %).
     
   - **Critères d’achat** : Indication des cartes acceptées, selon la condition de la carte, leur rareté, leur popularité et la possibilité de vendre des collections complètes ou partielles.
     
   - **Pourquoi nous choisir** : Mise en avant des avantages du service, tels que prix équitables, processus rapide, expertise de l’équipe et fiabilité des transactions
     
   - **Call to action(CTA)** : Bouton invitant le client à contacter le magasin ou se rendre sur place pour vendre ses cartes.
     
   - **i18n** : Toutes les sections utilisent les fichiers de traduction pour afficher le contenu automatiquement en français ou en anglais, selon la langue sélectionnée par l’utilisateur.

**Composant ScrollToTop** : Composant pour revenir en haut de la page lors du changement de page.

---
### 22 février: (10am - 2pm)) BE - Implémentation de la fonctionnalité d'inscription pour les utilisateurs :

   - **Recherche** : 
     - Utilisation de code de notre projet "OS2.0/ProjetOse_public" qui avait aussi le besoin de la fonctionnalité d'inscription d'utilisateur.
     - Regarder l'utilisation des endpoints et les méthodes des services.

   - **Remue-méninges** de ce qu'on aurait besoin :
     - Type d'utilisateur (client et admin).
     - Champs d'inscription pour un utilisateur client (username, nom, nom de famille, courriel, mot de passe).
     - Validation des champs d'inscription :
       - Champs obligatoires et non vides :
         - Username.
         - Nom.
         - Nom de famille.
         - Courriel.
         - Mot de passe. 
       - Champs de courriel valide :
         - Doit contenir @.
         - Doit contenir un point.
       - Mot de passe valide.
         - Doit contenir au moins 1 caractère unique.
         - Doit contenir au moins 1 majuscule.
         - Doit contenir au moins 1 chiffre. 

   - **Reimplementation** des bouts de code essentiel pour l'inscription d'utilisateur selon nos besoins.
     - Implémentation des class pour les couches : **Model, Repository, Service, Controller, Security Jwt**
     - **Refactor** du code implémenté pour fonctionner avec notre codebase.
   - **Resources**:
     - Projet Ose (ProjetOse_public): ([GITHUB](https://github.com/ZohnHadley/ProjetOse_public))

---
### 23 février: (10am - 11:40am)) BE - Implémentation de la fonctionnalité de connexion pour les utilisateurs :

- **Recherche** :
    - Utilisation de code de notre projet "OS2.0/ProjetOse_public" qui avait aussi le besoin de la fonctionnalité de connexion d'utilisateur.
    - Regarder l'utilisation des endpoints et les méthodes des services.

- **Remue-méninges** de ce qu'on aurait besoin :
    - Type d'utilisateur (client et admin).
    - Champs d'inscription pour un utilisateur client (username, nom, nom de famille, courriel, mot de passe).
    - Validation des champs d'inscription :
        - Champs obligatoires et non vides : 
            - Courriel.
            - Mot de passe.
        - Champs de courriel valide :
            - Doit contenir @.
            - Doit contenir un point. 

- **Reimplementation** des bouts de code essentiel pour l'inscription d'utilisateur selon nos besoins.
    - Implémentation des class pour les couches : **Model, Repository, Service, Controller, Security Jwt**
    - **Refactor** du code implémenté pour fonctionner avec notre codebase.
- **Resources**:
    - Projet Ose (ProjetOse_public): ([GITHUB](https://github.com/ZohnHadley/ProjetOse_public))
        
---
### 25 février: (4pm - 10pm)) FE - Implémentation de la page pour afficher les cartes + moteur de recherche dans la page shop :
   **FE service**: Implémentation du service frontend permettant de communiquer avec les endpoints du backend.
   
   **store**: Appeler le service
   
   **Afficher les cartes**: Afficher les cartes(monster,spell and trap) sur la page avec les éléments suivant:
      - Nom
      - Type de carte
      - image
      - Nombre en stock/rupture de stock
      - Nom du paquet
      - Le code du paquet
      - Rareté de la carte
      - Condition de la carte
      - Prix de la carte
      
   **Boutton ajouté au panier**: Un boutton qui permet d'ajouté la carte au panier.
   
   **pagination** : Permet de changer de page pour voir le reste des cartes.

   **moteur de recherche**: Permet à l’utilisateur de trouver rapidement une carte spécifique sans parcourir toute la pagination.

---
### 26 février: (4pm - 5:40pm)) FE - Implémentation du filtrage:
   
**Filtrage** : Bouttons et dropdown qui permettent de filtrer l'inventaire des cartes
   -filtrage par:
      - Tout
      - Monstre
      - Magie
      - Piège
      - En stock
      - Trier par prix ascendant et descendant

---
### 27 février: (3:20pm - 5:30pm)) FE - Implémentation des détails complets d'une carte lorsqu'on le clique:
   **Affichages des informations(onClick)**: Lorsqu'on clique sur une carte dans l'inventaire, on affiche les détails complets:
         Monster, spell and trap:
            -Photo
            -Nom de la carte
            -Rareté
            -Son effet
            -prix
            -les éditions
         Monster specifiquement:
            -Attribut
            -Type
            -Niveau
            -Ses stats(atk et def) 

---
### 2 Mars: (4pm - 10pm)) BE - Implémentation du pannier d'achat du client :

- **Remue-méninges** de ce qu'on aurait besoin pour le pannier d'achat:
  - Type d'utilisateur (client).
  - Type d'item dans le panier (carte).
  - Qui à access et comment il est accessible.
  - **fonctionnalités**:
    - Ajouter un item au panier.
    - Supprimer un item du panier.
    
- **Lue l'article de** [Denis Mutunga](https://medium.com/@denis.mutunga/back-end-architecture-for-the-cart-system-7c222bb99ef3) pour avoir une idée de comment implémenter le pannier d'achat.

---
### 4 Mars: (4:40pm - 5:15pm)) FE - Implémentation du pannier d'achat du client :
  **ShoppingCart service FE** : 
    - Création des fonctions pour communiquer avec le backend (getByUserId, getByEmail, addCard, removeCard).
    - Gestion des erreurs et des cas où le panier est vide ou inexistant.
    
  **ShoppingCart store(Zustand)** : 
    - Gestion du state global du panier (cart, loading, error).
    - Fonctions pour récupérer le panier, ajouter ou retirer une carte (mise à jour optimiste).

Fonctions calculées pour le total et le nombre de cartes.
  **ShoppingCart main page** :
   - Affichage dynamique de la liste des cartes dans le panier.
   - Gestion des états : chargement, erreur réseau, panier vide.
   - Sidebar de résumé de commande avec sous-total, livraison et total.

Boutons interactifs pour retirer des cartes et passer la commande.
  **internalisation**: Utilisation du fichier de traductions afin d’adapter automatiquement le contenu de la page selon la langue sélectionnée (français / anglais).

---
### 4 et 6 Mars: FE - Implémentation du formulaire pour login, register et logout:
**LoginPage**: 
    - Forumulaire: Champs qui demandent les informations pour se connecter à son compte(username, password) et un boutton pour permettre la connection à son compte
    
**Logout**:
    - Boutton: Une fois qu'un utilisateur s'est connecté, il peut cliquer sur son username dans la barre de navigation et se déconnecter.
    
**RegisterPage**:
    - Forumulaire:  Champs qui demandent les informations pour se créer un compte:
        -Nom d'utilisateur
        -Adresse courriel 
        -Mot de passe
        -confirmer Mot de passe
        -Boutton pour creer son compte

**internalisation**: Utilisation du fichier de traductions afin d’adapter automatiquement le contenu de la page selon la langue sélectionnée (français / anglais).

---

### 6pm à 10pm 8 Mars et 12pm à 6pm 9 Mars: BE - Implémentation spring_ai avec ollama (llama3.1:8b) :

**Recherche** : **Regarder** le vidéo **“[Bootiful Spring AI by Mark Pollack, Christian Tzolov, Josh Long, James Ward](https://www.youtube.com/watch?v=Sw3PlFXfWj4)”** pour m'aider à implémenter spring ai avec un LLM.

**Implémenter** spring ai avec ollama (llama3.1:8b):
  - Configurer spring ai (application properties) pour utiliser ollama et llama3.1:8b
      
  - Tester l'intégration avec des requêtes simples pour voir si cela fonctionne
**Remue-méninges** pour ce qui est pertinent du system-prompt (context initial) de l'IA:
**Implémentation** d'un vector-store pour les cartes (id, nom, description et quantité)
    - permet à l'IA de mieux comprendre les cartes et leur contexte
**Re-Tester** 
    - pour voir si l'IA fonctionne correctement avec le nouveau system-prompt
    - pour voir s'il reste en context avec le nouveau system-prompt 
    - pour voir si l'IA comprend les cartes et leur contexte

**Regarder** le vidéo **"[Modular RAG Architectures with Java and Spring AI](https://www.youtube.com/watch?v=ZcB4pNwPklI)"** pour mieux  m'informer sur les vector store, comment structuré notre projet et m'informer plus sur RAG









   
 

 
