FROM node:20 as build
WORKDIR /app
COPY package*.json ./
COPY .npmrc* ./
COPY . ./
RUN npm ci
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build /app/dist/parcezza-frontend /usr/share/nginx/html
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
