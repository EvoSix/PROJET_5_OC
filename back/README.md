# Testez une Application Full-Stack - Yoga App

## Démarrage du Projet
### Git Clone :
```bash
git clone https://github.com/EvoSix/PROJET_5_OC
```
[https://github.com/EvoSix/PROJET_5_OC](https://github.com/EvoSix/PROJET_5_OC)

## Installation du Backend
### Aller dans le dossier :
```bash
cd back
```
### Installer les dépendances :
```bash
mvn clean install
```
### Lancer le serveur backend :
```bash
mvn spring-boot:run
```

### Pour lancer et générer la couverture de code Jacoco :
```bash
mvn clean test
```
### Pour lancer et générer la couverture de code Jacoco pour les integration :
```bash
mvn clean test -Pintegration
```
### Pour lancer et générer la couverture de code Jacoco pour les unitaires :
```bash
mvn clean test -Punitaire
```


### MySQL
Le script SQL pour créer le schéma est disponible ici : `ressources/sql/script.sql`

Par défaut, le compte administrateur est :

- login : yoga@studio.com
- mot de passe : test!1234


