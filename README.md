# Agora

## Table of Content
- [1. Pre-requisites](#1-pre-requisites)
- [2. Launch back-end server](#2-launch-back-end-server)
- [3. Endpoints](#3-endpoints)
- [4. Add some data](#4-add-some-data)

## 1. Pre-requisites
- Java 17
- Docker

## 2. Set-up
- duplicate `.env.example` to `.env`, no need to change variables for local run
- launch databases via `docker compose up -d`

## 3. Launch back-end server
- Simply open the project using IntelliJ or your favorite IDE
- Run `AgoraBackApplication`

## 4. Endpoints
You can use a Postman collection provided in `/docs` directory to have a list of this back-end's endpoints.

To use any endpoints, please make sure beforehand to call `/signup` the first time you use it, otherwise call `/login`. This will generate and automatically store a JWT that will last **24 hours**.

## 5. Add some data

Use instructions from SQL files in `data` directory to have some data on your newly installed Agora Server.
You can also create your owns, but it might be a bit tedious as there are no back-office at all, for now.
