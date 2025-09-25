# Use Eclipse Temurin JDK 21 for building
FROM eclipse-temurin:21-jdk AS build

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts ./
COPY gradle/ gradle/

# Copy source code
COPY src/ src/

# Build the application JAR (skip tests for faster build)
RUN chmod +x ./gradlew && ./gradlew build -x test

# Production stage with JRE
FROM eclipse-temurin:21-jre

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8081 (configured Spring Boot port)
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]