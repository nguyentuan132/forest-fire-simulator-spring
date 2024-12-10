# Simulation de Propagation du Feu en Forêt

Ce projet implémente une simulation de la propagation d'un feu de forêt à travers une grille, où le feu se propage aux cases adjacentes avec une certaine probabilité. La simulation continue jusqu'à ce qu'il n'y ait plus de cases en feu.

## Objectif

L'objectif du système est de simuler la propagation du feu sur une grille en tenant compte de la probabilité de propagation du feu. À chaque étape, le feu peut se propager aux cases adjacentes, et une case en feu sera finalement transformée en cendres.

## Spécifications Fonctionnelles

### Entrée

- **Dimensions de la grille** (hauteur et largeur)
- **Positions initiales du feu**
- **Probabilité de propagation du feu**

### Sortie

Une liste des états de la forêt au cours de la simulation, représentant l'évolution du feu.

## Spécifications Exécutables

### Fonction : Simulation et propagation du feu

#### Scénario 1 : Configuration Initiale

- **Étant donné** une forêt avec une hauteur, une largeur et des positions initiales du feu spécifiées,
- **Lorsque** je lance la simulation,
- **Alors** la simulation doit commencer avec le feu dans les positions initiales spécifiées et simuler l'évolution du feu étape par étape.

#### Scénario 2 : Propagation du Feu

- **Étant donné** une forêt avec du feu dans certaines cases,
- **Lorsque** je lance la simulation pour une étape,
- **Alors** le feu doit se propager aux cases adjacentes avec une probabilité définie dans la configuration, et les cases en feu doivent être transformées en cendres.

#### Scénario 3 : Fin de la Simulation

- **Étant donné** une forêt qui brûle depuis plusieurs étapes,
- **Lorsque** la simulation continue jusqu'à ce qu'il n'y ait plus de feu,
- **Alors** la simulation doit s'arrêter lorsqu'il n'y a plus de cases en feu.

#### Scénario 4 : Vérification de la Validité d'une Case

- **Étant donné** une forêt,
- **Lorsque** je vérifie si une case est dans les limites de la grille,
- **Alors** le système doit retourner vrai si la case est dans la grille et faux sinon.

#### Scénario 5 : Propagation du Feu

- **Étant donné** une forêt avec du feu à une position spécifique,
- **Lorsque** le feu se propage aux cases adjacentes avec une certaine probabilité,
- **Alors** le système doit décider de manière aléatoire si le feu se propage à chacune des quatre cases adjacentes en fonction de la probabilité de propagation donnée.

## Vue d'ensemble de l'Implémentation

### Configuration de la Forêt

La forêt est initialisée avec les dimensions données, et les positions initiales du feu sont définies.

### Propagation du Feu

À chaque étape, le feu se propage aux cellules voisines avec la probabilité donnée, et les cellules en feu sont marquées comme cendres, empêchant toute propagation supplémentaire.

### Condition d'Arrêt

La simulation continue jusqu'à ce qu'il n'y ait plus de cellules en feu.

## Cas Limites

- Le feu peut ne pas se propager si la probabilité est faible.
- La simulation doit correctement gérer les limites de la grille pour éviter les erreurs lors de l'accès aux cellules voisines.

## Démarrer rapidement

### Exécution des Tests Unitaires

Pour exécuter les tests unitaires du projet, utilisez la commande suivante dans votre terminal :

```bash
mvn clean test
```

- Cela va nettoyer le projet et exécuter les tests unitaires définis dans le code.

### Démarrer l'application avec Spring Boot

Pour démarrer rapidement l'application avec Spring Boot et tester la simulation en temps réel, vous pouvez utiliser la commande suivante :

```bash
mvn clean spring-boot:run
```

- Cette commande démarre le serveur HTTP sur le port 8080. 
- Le résultat de la simulation peut être récupéré via l'API à l'adresse /api/simulation/simulate en envoyant une requête HTTP GET.

### Configuration de la simulation
- Le fichier de configuration par défaut de l'application est application.properties. Pour modifier les paramètres de la simulation (tels que la taille de la forêt, les positions initiales du feu et la probabilité de propagation du feu), vous devez éditer ce fichier.
# Exemple de configuration dans application.properties :

- `simulation.height=20` : définit la hauteur de la grille (forêt) à 20.
- `simulation.width=20` : définit la largeur de la grille (forêt) à 20.
- `simulation.initial-fire-positions=0,1;5,5;6,6` : spécifie les positions initiales du feu sur la grille : (0,1), (5,5), et (6,6).
- `simulation.fireSpreadProbability=0.5` : définit la probabilité de propagation du feu à 50% (0.5).

### Remarque importante

Les positions initiales du feu (par exemple `0,1`, `5,5`, `6,6`) ne peuvent pas dépasser les limites de la forêt. Par conséquent, les valeurs de `x` doivent être dans la plage `0 <= x < height` et les valeurs de `y` doivent être dans la plage `0 <= y < width`. Dans cet exemple, la forêt fait 20x20, donc les positions valides pour le feu sont dans les intervalles `0 <= x < 20` et `0 <= y < 20`.
