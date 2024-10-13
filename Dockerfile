# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your local machine to the container
COPY target/order_management-1.0-SNAPSHOT.jar /app/order_management-1.0-SNAPSHOT.jar

# Expose the port your app will run on (e.g., 8080)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/order_management-1.0-SNAPSHOT.jar"]
