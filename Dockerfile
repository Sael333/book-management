# Etapa 1: Construcción del proyecto usando Maven con JDK 17
FROM maven:3.8-jdk-17 AS build

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo pom.xml y las fuentes al contenedor
COPY pom.xml .
COPY src ./src

# Ejecuta la compilación y empaquetado, saltando tests para acelerar
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con JDK 17 slim para correr la app
FROM openjdk:17-jdk-slim

# Define el directorio de trabajo para la ejecución
WORKDIR /app

# Copia el archivo jar compilado de la etapa build
COPY --from=build /app/target/*.jar book.management-1.0.0-SNAPSHOT.jar

# Expone el puerto 8080 para que sea accesible desde fuera
EXPOSE 8080

# Comando para ejecutar la aplicación Java
ENTRYPOINT ["java", "-jar", "book.management-1.0.0-SNAPSHOT.jar"]
