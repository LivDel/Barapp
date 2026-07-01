# 1. Image de base : On utilise Nginx (un serveur web très rapide) dans sa version allégée (Alpine)
FROM nginx:alpine

# 2. Copie des fichiers statiques : On prend tout le contenu de notre dossier "dist" (généré par VueJS lors du build)
# et on le colle dans le dossier que Nginx utilise par défaut pour servir des pages web.
COPY dist/ /usr/share/nginx/html

# 3. Exposition du port : On indique que le serveur écoute sur le port 80 (standard pour le HTTP)
EXPOSE 80

# 4. Point d'entrée : On démarre Nginx en mode "premier plan" (daemon off) pour que le conteneur reste en vie
CMD ["nginx", "-g", "daemon off;"]
