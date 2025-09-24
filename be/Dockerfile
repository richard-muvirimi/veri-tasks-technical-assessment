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
RUN ./gradlew build -x test

# Production stage with JRE
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8080 (default Spring Boot port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]