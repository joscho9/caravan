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
# Optional: Setze eine Default-URL, falls keine übergeben wird
ARG REACT_APP_API_URL=http://localhost:8086/api
ENV REACT_APP_API_URL=$REACT_APP_API_URL

# Production-Build erstellen
RUN echo "Building with API URL: $REACT_APP_API_URL" && npm run build

# Debugging: was ist da tatsächlich?
RUN echo "Contents of /app after build:" && ls -l /app && \
    echo "Contents of /app/build:" && ls -l /app/build || echo "Build folder fehlt!"