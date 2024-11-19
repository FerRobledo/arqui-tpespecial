# Usa la imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Copia el archivo jar del microservicio
COPY target/microservice.viajes-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto 8084
EXPOSE 8084

# Comando de entrada
ENTRYPOINT ["java", "-jar", "/app.jar"]
