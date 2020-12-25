# Build stage
FROM maven:3.6.1-jdk-13-alpine AS build
COPY src /src
COPY pom.xml /pom.xml
RUN mvn -f /pom.xml clean install

# Start stage
FROM openjdk:13-jdk-alpine
ADD target/find-the-business-backend.jar find-the-business-backend.jar
ENTRYPOINT ["java","-jar","/find-the-business-backend.jar"]
