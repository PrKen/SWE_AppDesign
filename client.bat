@echo off
setlocal enabledelayedexpansion

:main_menu
echo Bienvenue sur l'application de chat !
echo 1. Se connecter
echo 2. Créer un profil
echo 3. Quitter
set /p choice="Choisissez une option : "

if "%choice%" == "1" goto login
if "%choice%" == "2" goto create_profile
if "%choice%" == "3" goto exit

echo Option invalide, veuillez réessayer.
goto main_menu

:login
echo.
set /p username="Entrez votre nom d'utilisateur : "
set /p password="Entrez votre mot de passe : "

:: Appeler le programme Java UserController pour vérifier les identifiants
java -cp "out;src/lib/mysql-connector-j-9.1.0.jar" com.monsite.controller.UserController login %username% %password%
if errorlevel 1 (
    echo Nom d'utilisateur ou mot de passe incorrect.
    pause
    goto main_menu
) else (
    echo Connexion réussie !
    goto choose_chat
)

:create_profile
echo.
set /p lastname="Entrez votre nom de famille : "
set /p firstname="Entrez votre prénom : "
set /p email="Entrez votre adresse email : "
set /p username="Entrez votre nom d'utilisateur : "
set /p password="Entrez votre mot de passe : "

:: Appeler le programme Java UserController pour créer le profil
java -cp "out;src/lib/mysql-connector-j-9.1.0.jar" com.monsite.controller.UserController create %lastname% %firstname% %email% %username% %password%
echo Profil créé avec succès ! Veuillez vous connecter.
pause
goto main_menu

:choose_chat
echo.
set /p target_username="Entrez le nom d'utilisateur avec qui vous voulez discuter : "

echo Démarrage de la discussion avec %target_username% sur le port 12345.

:: Lancer le client de chat après la connexion sans redemander les informations
java -cp "out;src/lib/mysql-connector-j-9.1.0.jar" com.monsite.network.ChatClient %username% %target_username% 12345

pause
goto main_menu

:exit
echo Fermeture de l'application.
pause
exit