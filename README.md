# SWE_AppDesign_LevelX



## Getting started

### Connection à la DB 

1. Créer le fichier **application.properties** (dans `/resources`)

```
# Configuration de la connexion à la base de données MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/swe_appdesign_levelx?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=<password>
```

### Étapes pour compiler et exécuter

1. **Librairies nécessaires** :
   - [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
   - Ouvre le lien ci-dessus.
   - Sélectionne la version Platform Independent (qui fonctionne avec tous les systèmes d'exploitation).
   - Télécharge le fichier ZIP qui contient le .jar nécessaire (mysql-connector-java-<version>.jar).
   - Décompresse le fichier ZIP et place le .jar dans ton dossier lib.

2. **Compiler le projet** :
   - Lancer le programme `./build.bat`

3. **Demmarer le serveur** :
   - Lancer le programme `./run.bat`

4. **Demmarer le(s) client(s)** :
   - Lancer le programme `./client.bat`

Il est désormais possible d'exécuter le projet. Assure-toi de bien configurer ton chemin de classe (`-cp`) avec le fichier `.jar` nécessaire pour que la connexion à MySQL fonctionne correctement.

## Quick start

Tu peux désormais lancer suivre les instructions du client et démarrer une discusion entre utilisateurs !