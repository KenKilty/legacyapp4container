# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file and resources into the container
COPY target/main.jar /app/main.jar
COPY target/input.txt /app/input.txt

# Set environment variables
ENV FILE_PATH="input.txt"
ENV API_URL="https://jsonplaceholder.typicode.com/todos/1"

# Run the JAR file
CMD ["java", "-jar", "main.jar"]