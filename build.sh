#!/bin/bash
# Render build script

echo "Starting build process..."

# Make mvnw executable
chmod +x mvnw

# Clean and build the project
./mvnw clean install -DskipTests

echo "Build completed successfully!"
