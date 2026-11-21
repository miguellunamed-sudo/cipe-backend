# Etapa 1: Compilar el proyecto con Maven y JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# Etapa 2: Ejecutar el .jar en un entorno ligero con JRE 17
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx300m", "-Xms128m", "-jar", "app.jar"]