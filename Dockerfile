# Multi-stage build for Spring Boot application

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests=false

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -g 1000 appuser && adduser -u 1000 -G appuser appuser

# Copy the built jar from the build stage
COPY --from=build --chown=appuser:appuser /app/target/DeRoyalty-0.0.1-SNAPSHOT.jar app.jar

# Copy AppImages so DataInitializer can read assets at runtime
COPY --from=build --chown=appuser:appuser /app/src/assets/AppImages ./src/assets/AppImages

# Expose port
EXPOSE 8080

# Switch to non-root user
USER appuser

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
