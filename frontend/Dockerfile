# ---- Build-Stage ----
FROM node:18-alpine AS builder

WORKDIR /app

# Nur notwendige Dateien kopieren
COPY package.json package-lock.json ./

# Schnell & reproduzierbar installieren
RUN npm ci --silent

# Restliche Quellen
COPY . .

# Optionaler Build-Parameter für API URL
ARG REACT_APP_API_URL=http://localhost:8086/api
ENV REACT_APP_API_URL=$REACT_APP_API_URL

# Production-Build erstellen
RUN echo "Building with API URL: $REACT_APP_API_URL" && npm run build


# ---- Production Stage ----
FROM nginx:alpine

# Entferne Default-Nginx-Seite
RUN rm -rf /usr/share/nginx/html/*

# Kopiere statischen Build vom vorherigen Stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Eigene Nginx-Konfiguration hinzufügen
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
