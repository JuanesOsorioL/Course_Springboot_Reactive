# 1. Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 2. Copiamos el JAR compilado
COPY target/producto-api-0.0.1-SNAPSHOT.jar app.jar

# 3. Puerto en el que corre tu aplicación
EXPOSE 8080

# 4. Entry point
ENTRYPOINT ["java","-jar","/app/app.jar"]