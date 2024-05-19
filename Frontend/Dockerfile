FROM node:20.12.2 as builder
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build || exit 1

FROM node:20.12.2
WORKDIR /app

COPY --from=builder /app/dist /app/dist

RUN npm install -g serve

EXPOSE 3000

ENTRYPOINT ["serve", "-l", "3000", "-s", "dist"]