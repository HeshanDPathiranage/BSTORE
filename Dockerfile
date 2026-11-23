# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy Maven wrapper and project descriptor
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Ensure Unix line endings and execution permissions for Maven wrapper
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Copy application source code and build the JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose default port
EXPOSE 8080

# Execute Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
