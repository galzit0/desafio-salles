FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/desafio-salles-0.0.1-SNAPSHOT.jar desafio-salles.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "desafio-salles.jar"]
