# ==============================
# ETAPE 1 : COMPILATION ANGULAR
# ==============================

FROM node:24 AS build

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY . .

RUN npm run build -- --configuration production


# ==============================
# ETAPE 2 : SERVEUR NGINX
# ==============================

FROM nginx:stable-alpine

COPY --from=build /app/dist/xvdereve-front/browser /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]