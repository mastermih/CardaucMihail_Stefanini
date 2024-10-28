FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y curl

WORKDIR /app

COPY target/order_management-1.0-SNAPSHOT.jar /app/order_management-1.0-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/order_management-1.0-SNAPSHOT.jar"]
