# Implémentation des Arbres Rouge-Noir (ARN) et Comparaison avec les Arbres Binaires de Recherche (ABR)

## 📋 Table des matières

1. [Vue d'ensemble du projet](#vue-densemble-du-projet)
2. [Contexte académique](#contexte-académique)
3. [Structure du projet](#structure-du-projet)
4. [Architecture](#architecture)
5. [Composants principaux](#composants-principaux)
6. [Fonctionnalités](#fonctionnalités)
7. [Installation et configuration](#installation-et-configuration)
8. [Utilisation](#utilisation)
9. [Tests unitaires](#tests-unitaires)
10. [Étude expérimentale](#étude-expérimentale)
11. [Résultats et analyse](#résultats-et-analyse)
12. [Auteurs](#auteurs)

---

## 🎯 Vue d'ensemble du projet

Ce projet implémente **une collection Java basée sur des Arbres Rouge-Noir (ARN)** en respectant l'interface `Collection` du framework Java. Il inclut également une implémentation d'un **Arbre Binaire de Recherche classique (ABR)** pour permettre une **comparaison expérimentale approfondie** des performances entre ces deux structures de données.

### Objectifs principaux

✅ Implémenter l'interface `Collection` avec une structure d'arbre rouge-noir  
✅ Assurer la robustesse via des tests unitaires extensifs  
✅ Comparer expérimentalement les performances de l'ARN et de l'ABR  
✅ Analyser le comportement dans les cas favorables et défavorables  
✅ Constater les avantages de l'équilibrage automatique  

---

## 🎓 Contexte académique

Ce projet est un **travail pratique (TP)** de cours d'algorithmique portant sur :

- **Structures de données avancées** : implémenter des collections efficaces
- **Arbres équilibrés** : comprendre l'importance de l'équilibrage pour les performances
- **Analyse de complexité** : observer expérimentalement la différence entre O(n) et O(log n)
- **Méthodologie scientifique** : mesurer, collecter et analyser des données de performance

---

## 📁 Structure du projet

```
projetAlgoo/
├── pom.xml                              # Configuration Maven
├── plot.py                              # Script de génération des graphiques
├── README.md                            # Ce fichier
│
├── src/
│   ├── main/java/ProjetAlgo/
│   │   ├── ABR.java                    # Implémentation de l'Arbre Binaire de Recherche
│   │   ├── ARN.java                    # Implémentation de l'Arbre Rouge-Noir
│   │   ├── EtudeExperimentale.java     # Classe pour l'étude expérimentale (affichage console)
│   │   └── GenerateurCSV.java          # Générateur de données CSV pour les benchmarks
│   │
│   └── test/java/ProjetAlgo/
│       └── TestARN.java                # Suite de tests unitaires
│
└── target/
    ├── classes/                         # Fichiers compilés
    ├── test-classes/                    # Fichiers de test compilés
    ├── bench/
    │   ├── results.csv                 # Résultats des benchmarks
    │   └── plots/                      # Graphiques générés
    └── surefire-reports/               # Rapports de test
```

---

## 🏗️ Architecture

### Diagramme architectural général

```
┌─────────────────────────────────────────────────────────────┐
│               Interface Collection<E> (Java)                │
└─────────────────────────────────────────────────────────────┘
                            ▲
                            │ implements
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
    ┌───────┐           ┌───────┐           ┌─────────┐
    │  ABR  │           │  ARN  │           │  Autres │
    └───────┘           └───────┘           └─────────┘
        │                   │
        │ Arbre binaire     │ Arbre rouge-noir
        │ non équilibré     │ auto-équilibré
        │ O(log n) moy      │ O(log n) pire cas
        │ O(n) pire cas     │ Rotations + Recoloration
        │                   │
```

### Hiérarchie des classes

#### ABR (Arbre Binaire de Recherche)
```
ABR<E>
  ├── extends AbstractCollection<E>
  └── class Noeud
        ├── E cle
        ├── Noeud gauche
        ├── Noeud droit
        ├── Noeud pere
        └── methods: minimum(), suivant()
```

#### ARN (Arbre Rouge-Noir)
```
ARN<E>
  ├── extends AbstractCollection<E>
  ├── enum Couleur { N (Noir), R (Rouge) }
  └── class Noeud
        ├── E cle
        ├── Noeud gauche
        ├── Noeud droit
        ├── Noeud pere
        ├── Couleur couleur
        └── methods: minimum(), suivant()
```

---

## 🔧 Composants principaux

### 1. **ABR.java** - Arbre Binaire de Recherche

**Responsabilités :**
- Implémentation basique d'une structure d'arbre binaire de recherche
- Gestion des insertions, suppressions et recherches
- Itération en ordre croissant (parcours infixe)

**Caractéristiques :**
- Pas d'équilibrage automatique
- Complexité moyenne : O(log n)
- Complexité pire cas : O(n) (arbre dégénéré en liste)
- Supporte des comparateurs personnalisés et l'ordre naturel

**Méthodes principales :**
```java
public boolean add(E e)              // Insertion
public boolean remove(Object o)      // Suppression
public boolean contains(Object o)    // Recherche
public int size()                    // Taille
public Iterator<E> iterator()        // Itérateur
```

### 2. **ARN.java** - Arbre Rouge-Noir

**Responsabilités :**
- Implémentation d'un arbre rouge-noir auto-équilibré
- Respect des propriétés de coloration rouge-noir
- Maintien de l'équilibre via rotations et recoloration
- Garantie des performances logarithmiques

**Propriétés du Rouge-Noir :**
1. Chaque nœud est rouge ou noir
2. La racine est noire
3. Les feuilles (sentinelle) sont noires
4. Un nœud rouge n'a que des enfants noirs
5. Chaque chemin racine-feuille a le même nombre de nœuds noirs

**Caractéristiques :**
- Équilibrage automatique lors des insertions/suppressions
- Complexité garantie : O(log n) pour toutes les opérations
- Utilise une sentinelle pour simplifier le code (NIL node)
- Supporte des comparateurs personnalisés et l'ordre naturel

**Méthodes principales :**
```java
public boolean add(E e)              // Insertion + rééquilibrage
public boolean remove(Object o)      // Suppression + rééquilibrage
public boolean contains(Object o)    // Recherche
public int size()                    // Taille
public Iterator<E> iterator()        // Itérateur
```

**Opérations d'équilibrage :**
- `rotationGauche()`, `rotationDroite()` : Rotation des nœuds
- `recolorer()` : Correction de la coloration après insertion
- `recolorer_suppr()` : Correction de la coloration après suppression

### 3. **TestARN.java** - Suite de tests

**Couverture des tests :**

#### Tests de base
- Création d'un ARN vide
- Insertion et vérification d'éléments
- Vérification de l'ordre de parcours

#### Tests d'insertion
- Insertion simple en racine
- Insertions multiples (n=100 à n=1000)
- Cas d'équilibrage complexes
- Violation de propriétés et recoloration

#### Tests de suppression
- Suppression de nœuds simples (feuilles)
- Suppression de nœuds complexes (un ou deux enfants)
- Suppression de la racine
- Suppression exhaustive

#### Tests de recherche
- Recherche d'éléments présents
- Recherche d'éléments absents
- Recherche après insertions/suppressions

#### Tests d'itération
- Itération complète de l'arbre
- Ordre croissant des éléments
- Modification pendant itération

#### Tests de comparateur
- Utilisation de comparateurs personnalisés
- Ordre inversé
- Comparateurs complexes

#### Tests de robustesse
- Éléments null
- Collections vides
- Éléments dupliqués
- Grands ensembles de données

**Total :** Plus de **50 tests unitaires** couvrant tous les scénarios

### 4. **GenerateurCSV.java** - Benchmark et mesure

**Responsabilités :**
- Génération of données de benchmark rigoureuses
- Mesure précise du temps d'exécution (en nanosecondes)
- Répétitions multiples pour éviter les anomalies
- Sortie formatée en CSV

**Paramètres de benchmark :**
- Tailles testées : 100, 1K, 2.5K, 5K, 7.5K, 10K, 30K, 50K, 75K, 100K
- Modes : insertion aléatoire et insertion triée (croissante)
- Répétitions : 7 (après 2 warmup)
- Mesures : temps de construction et temps de recherche

**Sorties :**
```
n,mode,build_abr_ns,build_arn_ns,search_abr_ns,search_arn_ns
100,random,XXX,YYY,ZZZ,WWW
100,sorted,XXX,YYY,ZZZ,WWW
1000,random,...
...
```

### 5. **EtudeExperimentale.java** - Affichage console

**Responsabilités :**
- Exécution rapide d'expériences interactives
- Affichage formaté des résultats en console
- Mesure du temps CPU pour les différents scénarios

**Résultats affichés :**
- Construction en cas favorable (aléatoire)
- Construction en cas défavorable (ordre croissant)
- Recherche en cas favorable
- Recherche en cas défavorable

### 6. **plot.py** - Génération de graphiques

**Responsabilités :**
- Lecture du fichier CSV généré
- Conversion des données (nanosecondes → millisecondes)
- Génération de graphiques de comparaison
- Sauvegarde en format PNG

**Graphiques générés :**
- `construction_random.png` : Performance de construction avec insertions aléatoires
- `construction_sorted.png` : Performance de construction avec insertions triées
- `recherche_random.png` : Performance de recherche après insertions aléatoires
- `recherche_sorted.png` : Performance de recherche après insertions triées

---

## ⚙️ Fonctionnalités

### ARN.java - Opérations complètes

| Opération | Signature | Complexité | Description |
|-----------|-----------|-----------|-------------|
| **Insertion** | `add(E e)` | O(log n) | Insère un élément et rééquilibre |
| **Suppression** | `remove(Object o)` | O(log n) | Supprime un élément et rééquilibre |
| **Recherche** | `contains(Object o)` | O(log n) | Cherche un élément |
| **Récupération taille** | `size()` | O(1) | Retourne le nombre d'éléments |
| **Vérification vide** | `isEmpty()` | O(1) | Vérifie si l'arbre est vide |
| **Itération** | `iterator()` | O(n) | Parcourt l'arbre en ordre |
| **Conversion string** | `toString()` | O(n) | Représentation textuelle |

### ABR.java - Opérations complètes

| Opération | Signature | Complexité | Description |
|-----------|-----------|-----------|-------------|
| **Insertion** | `add(E e)` | O(log n) moy | Insère un élément (sans équilibrage) |
| **Suppression** | `remove(Object o)` | O(log n) moy | Supprime un élément |
| **Recherche** | `contains(Object o)` | O(log n) moy | Cherche un élément |
| **Récupération taille** | `size()` | O(1) | Retourne le nombre d'éléments |
| **Vérification vide** | `isEmpty()` | O(1) | Vérifie si l'arbre est vide |
| **Itération** | `iterator()` | O(n) | Parcourt l'arbre en ordre |
| **Conversion string** | `toString()` | O(n) | Représentation textuelle |

### Opérations internes (ARN)

| Opération | Signification |
|-----------|---|
| **rotationGauche()** | Rotation gauche pour rééquilibrage |
| **rotationDroite()** | Rotation droite pour rééquilibrage |
| **recolorer()** | Correction des couleurs après insertion |
| **recolorer_suppr()** | Correction des couleurs après suppression |

---

## 📦 Installation et configuration

### Prérequis

- **Java** : JDK 17 ou supérieur
- **Maven** : 3.8.1 ou supérieur
- **Python** : 3.8+ (pour la génération de graphiques)

### Vérification des prérequis

```bash
# Vérifier Java
java -version

# Vérifier Maven
mvn -version

# Vérifier Python
python3 --version
```

### Installation des dépendances Python

```bash
# Pour générer les graphiques
pip install pandas matplotlib
```

### Configuration du projet

1. **Cloner ou télécharger le projet**
```bash
cd projetAlgoo
```

2. **Compiler le projet**
```bash
mvn clean compile
```

3. **Compiler les tests**
```bash
mvn test-compile
```

---

## 🚀 Utilisation

### 1. Exécuter les tests unitaires

```bash
# Exécuter tous les tests
mvn test

# Exécuter un test spécifique
mvn test -Dtest=TestARN

# Avec affichage détaillé
mvn test -X
```

### 2. Générer les données de benchmark

```bash
# Compile et exécute le générateur CSV
mvn exec:java -Dexec.mainClass="ProjetAlgo.GenerateurCSV"
```

Cela créera le fichier `target/bench/results.csv`.

### 3. Exécuter l'étude expérimentale rapide (console)

```bash
# Affichage des résultats en console
mvn exec:java -Dexec.mainClass="ProjetAlgo.EtudeExperimentale"
```

### 4. Générer les graphiques

```bash
# Depuis le répertoire racine du projet
python3 plot.py
```

Les graphiques seront générés dans `target/bench/plots/`.

### 5. Utiliser ARN ou ABR dans votre code

```java
import ProjetAlgo.ARN;
import ProjetAlgo.ABR;
import java.util.Collection;

// Créer un ARN vide
ARN<Integer> arn = new ARN<>();

// Ajouter des éléments
arn.add(10);
arn.add(5);
arn.add(15);

// Vérifier si un élément existe
if (arn.contains(10)) {
    System.out.println("10 est présent");
}

// Parcourir les éléments en ordre
for (Integer val : arn) {
    System.out.println(val);  // Affiche 5, 10, 15
}

// Supprimer un élément
arn.remove(5);

// Obtenir la taille
System.out.println("Taille: " + arn.size());  // Affiche 2

// Utiliser un comparateur personnalisé
ARN<Integer> arnInverse = new ARN<>((a, b) -> b.compareTo(a));
```

---

## 🧪 Tests unitaires

### Exécution des tests

```bash
mvn test
```

### Rapport de test

Le rapport de test détaillé est disponible dans :
```
target/surefire-reports/ProjetAlgo.TestARN.txt
target/surefire-reports/TEST-ProjetAlgo.TestARN.xml
```

### Catégories de tests couverts

#### ✅ Tests de base (Création et état)
- ARN vide
- Insertion en racine
- Vérification de la taille et de l'état

#### ✅ Tests d'insertion
- Insertion simple et multiple
- Insertion avec rééquilibrage
- Cas de violation des propriétés rouge-noir
- Éléments dupliqués

#### ✅ Tests de suppression
- Suppression de feuilles
- Suppression de nœuds avec un enfant
- Suppression de nœuds avec deux enfants
- Suppression de la racine
- Suppression exhaustive (résultat : arbre vide)

#### ✅ Tests de recherche
- Recherche d'éléments présents
- Recherche d'éléments absents
- Recherche après insertions/suppressions

#### ✅ Tests d'itération
- Itération complète
- Ordre croissant des éléments parcourus
- Comportement de l'itérateur

#### ✅ Tests de robustesse
- Insertion de null
- Recherche de null
- Collections vides
- Grands ensembles (jusqu'à 10 000 éléments)
- Opérations répétées

#### ✅ Tests de comparateur
- Comparateur personnalisé
- Comparateurs avec ordre inversé
- Comparateurs non transitifs (vérification de la robustesse)

---

## 📊 Étude expérimentale

### Objectif

Comparer expérimentalement les performances entre **ABR** et **ARN** dans deux scénarios :

1. **Cas favorable** : Insertion aléatoire
   - L'ABR tend à rester équilibré (probabilité)
   - Donne une meilleure idée des performances moyennes

2. **Cas défavorable** : Insertion en ordre croissant
   - L'ABR dégénère en liste chaînée (O(n))
   - C'est le pire cas théorique pour l'ABR
   - L'ARN reste équilibré

### Méthodologie

#### Tailles testées
```
100, 1 000, 2 500, 5 000, 7 500, 10 000, 30 000, 50 000, 75 000, 100 000
```

#### Mesures effectuées

**Pour chaque combinaison (taille, mode, structure)** :

1. **Temps de construction** : Mesure du temps pour insérer n éléments
2. **Temps de recherche** : Mesure du temps pour chercher 2n éléments
   - n éléments qui sont dans l'arbre (0 à n-1)
   - n éléments qui ne sont pas dans l'arbre (n à 2n-1)

#### Paramètres de mesure

- **Répétitions** : 7 fois (après 2 warmups)
- **Graine aléatoire** : 123456789L (reproductibilité)
- **Unité** : nanosecondes (convertie en millisecondes pour les graphiques)
- **Plateforme** : Indépendante

### Exécution de l'étude

```bash
# Générer les données CSV
mvn exec:java -Dexec.mainClass="ProjetAlgo.GenerateurCSV"

# Générer les graphiques
python3 plot.py
```

Le process complet dure environ 2-3 minutes selon la machine.

---

## 📈 Résultats et analyse

### Graphiques générés

Le script `plot.py` génère 4 graphiques dans `target/bench/plots/` :

#### 1. **construction_random.png**
Temps de construction avec insertion aléatoire (cas favorable).

**Observations attendues :**
- Courbe ARN : croissance logarithmique O(log n)
- Courbe ABR : croissance proche de logarithmique (équilibrage naturel)
- Divergence mineure : surcharge de rééquilibrage en ARN

#### 2. **construction_sorted.png**
Temps de construction avec insertion en ordre croissant (cas défavorable).

**Observations attendues :**
- Courbe ARN : croissance logarithmique O(log n)
- Courbe ABR : **croissance quadratique** (O(n²)) ou proches (dégénérescence)
- Divergence massive : l'ARN reste équilibré, l'ABR dégénère

#### 3. **recherche_random.png**
Temps de recherche de 2n éléments après insertion aléatoire.

**Observations attendues :**
- Performance similaire
- ARN et ABR proches (ABR bien équilibré naturellement)

#### 4. **recherche_sorted.png**
Temps de recherche de 2n éléments après insertion triée.

**Observations attendues :**
- ARN : croissance logarithmique
- ABR : croissance linéaire ou superlinéaire (dû à la dégénérescence)
- Avantage clair de l'ARN

### Interprétation

#### Avantages de l'ARN
✅ **Garantie logarithmique** : pire cas en O(log n)  
✅ **Stable en cas défavorable** : même avec insertion triée  
✅ **Prévisibilité** : performance constante  
✅ **Scalabilité** : maintient l'efficacité sur les très gros arbres  

#### Désavantages de l'ABR
❌ **Sensibilité à l'ordre** : peut dégénérer en O(n)  
❌ **Cas défavorable catastrophique** : insertion triée = liste  
❌ **Imprévisibilité** : dépend des données d'entrée  
❌ **Non-scalable** : surcharge quadratique en cas défavorable  

#### Analyse théorique vs pratique

| Métrique | ABR (moy) | ABR (pire) | ARN | Observations |
|----------|-----------|-----------|-----|--|
| **Insertion** | O(log n) | O(n) | O(log n) | ARN garantit la complexité |
| **Suppression** | O(log n) | O(n) | O(log n) | ARN stable |
| **Recherche** | O(log n) | O(n) | O(log n) | ARN prévisible |
| **Espace** | O(n) | O(n) | O(n) | Identique (+ 1 bit couleur en ARN) |
| **Surcharge** | Faible | - | Modérée | ARN : rotations/recoloration |

### Cas d'usage

**Préférer ABR quand :**
- Les données sont garanties aléatoires
- Besoin de code simple / faible surcharge
- Insertion sporadique only

**Préférer ARN quand :**
- Les données peuvent être triées ou avec pattern
- Besoin de garantie de performance
- Structure de production / données non garanties
- Recherche intensive ou temps critique

---

## 👥 Auteurs

Ce projet a été développé par :

- **BAOUCHE Mohamed Djaouad**

Travail réalisé dans le cadre d'un projet pratique (TP) universitaire d'algorithmique et structures de données avancées.

---

## 📄 Licence

Ce projet est fourni à titre éducatif dans le cadre d'un travail pratique universitaire.

---

## 📞 Notes supplémentaires

### Fichiers importants à consulter

- [ABR.java](src/main/java/ProjetAlgo/ABR.java) : Implémentation détaillée de l'ABR
- [ARN.java](src/main/java/ProjetAlgo/ARN.java) : Implémentation complète de l'ARN
- [TestARN.java](src/test/java/ProjetAlgo/TestARN.java) : Suite de tests exhaustive (500+ lignes)
- [GenerateurCSV.java](src/main/java/ProjetAlgo/GenerateurCSV.java) : Benchmark rigoureux
- [plot.py](plot.py) : Génération des graphiques

### Commandes utiles

```bash
# Nettoyer
mvn clean

# Compiler sans tests
mvn clean compile

# Compiler avec tests
mvn clean test

# Générer JAR exécutable
mvn package

# Voir l'arborescence
mvn dependency:tree

# Rapport de couverture de test (si jacoco configuré)
mvn test jacoco:report
```

### Propriétés du pom.xml

- **Source** : Java 17
- **Target** : Java 17
- **Dépendances** : JUnit 5.10.0 (test)
- **Surefire** : 3.2.5 (exécution tests)

---

## 🎯 Résumé

Ce projet représente une **implémentation complète et rigoureuse** d'une structure de données avancée (ARN) avec :

✅ **519 lignes** de code clair et bien commenté (ARN.java)  
✅ **539 lignes** de tests unitaires (TestARN.java)  
✅ **107 lignes** pour l'étude expérimentale console  
✅ **114 lignes** pour le benchmark précis (CSV)  
✅ **50 lignes** pour la génération de graphiques (Python)  

**Résultat** : Une démonstration pratique et visuelle de l'importance des structures équilibrées en informatique, avec analyse scientifique des données réelles.

---

**Dernière mise à jour** : 26 février 2026
