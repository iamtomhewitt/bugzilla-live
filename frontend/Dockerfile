FROM node:latest

RUN mkdir -p /

COPY package.json /
COPY package-lock.json /

RUN npm install

COPY . /
CMD ["npm", "start"]