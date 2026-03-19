# Dockerfile para Spring Boot Backend

FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

# Copiar archivos
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Build
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copiar JAR desde builder
COPY --from=builder /app/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"

EXPOSE 8080

# Run
ENTRYPOINT ["java", "-jar", "app.jar"]
