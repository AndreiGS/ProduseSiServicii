# build stage
FROM node:lts-alpine as build-stage
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json /app/package.json
RUN npm install --silent
RUN npm install @vue/cli -g
COPY . /app
ARG VUE_APP_STRIPE_PUBLIC_KEY
ARG VUE_APP_FACEBOOK_CLIENT_ID
ARG VUE_APP_BACKEND
ARG VUE_APP_FRONTEND
ENV VUE_APP_STRIPE_PUBLIC_KEY $VUE_APP_STRIPE_PUBLIC_KEY
ENV VUE_APP_FACEBOOK_CLIENT_ID $VUE_APP_FACEBOOK_CLIENT_ID
ENV VUE_APP_BACKEND $VUE_APP_BACKEND
ENV VUE_APP_FRONTEND $VUE_APP_FRONTEND
RUN npm run build

# production stage
FROM nginx:stable-alpine as production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx/nginx.conf /etc/nginx/conf.d
EXPOSE 3000
CMD ["nginx", "-g", "daemon off;"]
