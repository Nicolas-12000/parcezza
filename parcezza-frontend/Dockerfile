FROM node:20-alpine as build
WORKDIR /app
RUN apk add --no-cache python3 make g++
COPY package*.json ./
COPY .npmrc* ./
COPY . ./
RUN npm ci --silent
RUN npm run build -- --configuration=production

FROM nginx:stable-alpine
COPY --from=build /app/dist/parcezza-frontend /usr/share/nginx/html
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
