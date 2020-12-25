# Build stage
FROM maven:3.6.1-jdk-13-alpine AS build
COPY pom.xml /pom.xml
COPY src /src
RUN mvn -f /pom.xml clean install

# Start stage
FROM openjdk:13-jdk-alpine
ADD target/find-the-business-backend.jar find-the-business-backend.jar
ENTRYPOINT ["java","-jar","/find-the-business-backend.jar"]
