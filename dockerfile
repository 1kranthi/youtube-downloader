 FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
 COPY . .
 RUN mvn clean package -DskipTests

 FROM openjdk:21-slim
COPY --from=build /target/youtube-downloader-0.0.1-SNAPSHOT.jar youtube-downloader.jar
EXPOSE 8081

ENTRYPOINT [ "java","-jar","youtube-downloader.jar" ]