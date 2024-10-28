# SWE_AppDesign_LevelX



## Getting started

### Connection à la DB 

1. Créer le fichier **application.properties** (dans `/ressources`)

```
# Configuration de la connexion à la base de données MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/swe_appdesign_levelx?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=ton_mot_de_passe
```

### Étapes pour compiler et exécuter

1. **Librairies nécessaires** :
   - [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
   - Ouvre le lien ci-dessus.
   - Sélectionne la version Platform Independent (qui fonctionne avec tous les systèmes d'exploitation).
   - Télécharge le fichier ZIP qui contient le .jar nécessaire (mysql-connector-java-<version>.jar).
   - Décompresse le fichier ZIP et place le .jar dans ton dossier lib.

2. **Compiler le projet** :
   - Crée un fichier `build.bat` (Windows) ou `build.sh` (Linux/macOS) pour compiler les fichiers Java :
    ```
    @echo off
    javac -cp "src/lib/mysql-connector-j-<version>.jar" -d "./out" "./src/com/monsite/Application.java" "./src/com/monsite/controller/UserController.java" "./src/com/monsite/model/User.java" "./src/com/monsite/repository/UserRepository.java" "./src/com/monsite/service/UserService.java"
    ```

3. **Exécuter le projet** :
   - Crée un fichier `run.bat` (Windows) ou `run.sh` (Linux/macOS) pour exécuter l'application :
    ```
    java -cp "out;src/lib/mysql-connector-j-<version>.jar" com.monsite.Application
    ```

Il est désormais possible d'exécuter le projet. Assure-toi de bien configurer ton chemin de classe (`-cp`) avec le fichier `.jar` nécessaire pour que la connexion à MySQL fonctionne correctement.

## Quick start

Tu peux désormais lancer l'application en effectuant les commandes `./build.bat` puis `./run.bat` !