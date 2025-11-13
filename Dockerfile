# --- Stage 1: The Build Stage ---
# Use a Maven image with Java 17 to build the application
# This image INCLUDES mvn (Maven)
FROM maven:3.9-eclipse-temurin-17 AS build-stage

# Set the working directory
WORKDIR /app


# Copy the pom.xml and download dependencies
# This layer is cached, making future builds faster
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and build the application
COPY src ./src
# This runs 'mvn package' inside the container, skipping tests
RUN mvn clean package -DskipTests



# --- Stage 2: The Final Runtime Stage ---
# Use a minimal JRE 17 image. We don't need the full JDK to run.
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Create a non-root user and group for security
RUN groupadd -r spring && useradd -r -g spring spring

# Copy the JAR file built in Stage 1
# It's named based on your pom.xml <artifactId> and <version>
COPY --from=build-stage /app/target/notification-service-0.0.1-SNAPSHOT.jar app.jar

# Give ownership of the app to the new user
RUN chown spring:spring app.jar


# Switch to the non-root user
USER spring

# Expose the port your application runs on
EXPOSE 8084

# The command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]


