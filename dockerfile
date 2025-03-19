
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY target/cycle-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Anwendung starten
CMD ["java", "-jar", "app.jar"]