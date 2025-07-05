# ---- Build Stage ----
FROM node:18-alpine AS builder

WORKDIR /app

# copy only necessary files
COPY package.json package-lock.json ./

# install dependencies quickly and reproducibly
RUN npm ci --silent

# copy remaining sources
COPY . .

# optional build parameter for API URL
ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL

# create production build
RUN echo "VITE_API_URL=$VITE_API_URL" > .env.production
RUN echo "Building with API URL: $VITE_API_URL" && npm run build


# ---- Production Stage ----
FROM nginx:alpine

# remove Default-Nginx-Page
RUN rm -rf /usr/share/nginx/html/*

# copy static build from previous stage
COPY --from=builder /app/dist /usr/share/nginx/html

# add custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]