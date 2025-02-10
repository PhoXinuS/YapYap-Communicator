FROM node:21-bullseye
WORKDIR /app

COPY package.json /app
COPY package-lock.json /app

RUN npm install

COPY . .

ARG REACT_APP_BACKEND_URL
ENV REACT_APP_BACKEND_URL=$REACT_APP_BACKEND_URL

EXPOSE 3000

CMD ["npm", "start"]