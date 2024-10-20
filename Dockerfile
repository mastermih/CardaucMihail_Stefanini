
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/order_management-1.0-SNAPSHOT.jar /app/order_management-1.0-SNAPSHOT.jar

ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/test

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/order_management-1.0-SNAPSHOT.jar"]
