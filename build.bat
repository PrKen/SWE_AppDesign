@echo off
echo Compilation des fichiers Java...

rem Créer une variable pour le classpath
set CLASSPATH="src/lib/mysql-connector-j-9.1.0.jar"

rem Créer le dossier de sortie s'il n'existe pas
if not exist out mkdir out

rem Compiler chaque fichier Java trouvé dans le répertoire src
for /r "./src" %%f in (*.java) do (
    echo Compilation de "%%f"
    javac -cp %CLASSPATH%;"src" -d "./out" "%%f"
)

echo Compilation terminée.