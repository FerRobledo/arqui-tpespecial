FROM openjdk:17-jdk-slim

# Copia el archivo jar del microservicio
COPY target/microservice.config-0.0.1-SNAPSHOT.jar app.jar

# Copia la carpeta de configuraciones
COPY src/main/resources/configurations /configurations

# Expone el puerto 8888
EXPOSE 8888

# Comando de entrada
ENTRYPOINT ["java", "-Dspring.profiles.active=native", "-jar", "/app.jar"]
