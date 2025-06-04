# Stage 1: Build the application
# Use an official Maven image with Eclipse Temurin JDK 17
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to leverage Docker cache for dependencies
COPY pom.xml .

# Download all dependencies. This layer is cached if pom.xml does not change.
# Using dependency:go-offline first can speed up subsequent builds if source changes but pom doesn't.
RUN mvn dependency:go-offline -B

# Copy the source code into the container
COPY src ./src

# Package the application, skipping tests for faster Docker builds
# The -B flag runs Maven in batch mode (non-interactive)
RUN mvn clean package -B -DskipTests

# Stage 2: Create the runtime image
# Use a slim JRE image for the final application
FROM eclipse-temurin:17-jre-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the builder stage to the current stage
# Renaming it to app.jar for simplicity
COPY --from=builder /app/target/distributeur-automatique-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on.
# Spring Boot defaults to 8080. If you have configured a different port
# in your application.properties or application.yml, update this value.
EXPOSE 8080

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
