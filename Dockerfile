# Multi-stage build
FROM eclipse-temurin:17-jdk as builder

WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradlew.bat .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src/ src/

# Build the application
RUN chmod +x ./gradlew && ./gradlew build -x test

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD java -cp /app/app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]
