#!/bin/sh
cd consumer &&
./gradlew bootJar &&
cd .. &&
cd producer
./gradlew bootJar &&
cd .. &&
docker compose up --build -d   &&
cd client 
docker compose up --build 