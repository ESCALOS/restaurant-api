# Usa la imagen base de JDK 21 para compilar y ejecutar la aplicación
FROM openjdk:21-jdk-slim AS build

# Crear un directorio para la aplicación
WORKDIR /app

# Copiar los archivos del proyecto al contenedor
COPY . .

RUN ./mvnw clean package -DskipTests

# Fase de ejecución
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copiar el archivo JAR generado desde la fase de construcción
COPY --from=build /app/target/restaurant-api-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 80 para HTTP
EXPOSE 80

# Ejecutar la aplicación
ENTRYPOINT ["java", "-Dserver.port=80", "-jar", "app.jar"]
