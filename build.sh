#!/bin/bash

set -e  # Exit immediately if a command exits with a non-zero status
set -u  # Treat unset variables as an error

# Set environment variables
export FILE_PATH="input.txt"
export API_URL="https://jsonplaceholder.typicode.com/todos/1"

# Set the source file and class name
SOURCE_FILE="src/main/java/com/example/Main.java"
CLASS_NAME="com.example.Main"
JAR_FILE="target/main.jar"

# Create the target directory if it doesn't exist
mkdir -p target

# Copy file system input to the target directory
cp src/main/resources/input.txt target/input.txt

# Compile the Java code
javac -d target "$SOURCE_FILE"

# Create a temporary directory for the manifest file
TEMP_DIR=$(mktemp -d)
MANIFEST_FILE="$TEMP_DIR/manifest.txt"
echo "Main-Class: $CLASS_NAME" > "$MANIFEST_FILE"
jar cvfm "$JAR_FILE" "$MANIFEST_FILE" -C target .
rm -rf "$TEMP_DIR"

# Build the container image using podman with context directory switch
echo "Building container image with podman..."
podman build -t legacyapp4container -f dockerfile .

# Run the container and output results to the console
echo "Running container..."
podman run --rm legacyapp4container
