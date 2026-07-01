# 1. Image de base : On part d'un environnement Java 17 minimaliste (Alpine)
# 'Alpine' signifie que c'est une version allégée de Linux, parfaite pour des conteneurs rapides.
FROM eclipse-temurin:17-jre-alpine

# 2. Espace de travail : On se place dans le dossier /app à l'intérieur du conteneur
WORKDIR /app

# 3. Copie du fichier : On prend le fichier .jar généré par notre compilation Maven (dans notre dossier target)
# et on le copie dans le conteneur sous le nom "app.jar"
COPY target/*.jar app.jar

# 4. Point d'entrée : La commande exacte qui sera lancée au démarrage du conteneur
# C'est l'équivalent de taper "java -jar app.jar" dans notre terminal.
ENTRYPOINT ["java", "-jar", "app.jar"]
