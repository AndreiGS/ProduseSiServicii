# Build stage
FROM maven:3.6.1-jdk-13-alpine AS build
COPY pom.xml /app/
COPY src /app/src
RUN mvn -f /app/pom.xml clean install

# Run stage
FROM openjdk:13-jdk-alpine
COPY --from=build /app/target/find-the-business-backend*.jar /app/find-the-business-backend.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/find-the-business-backend.jar"]
