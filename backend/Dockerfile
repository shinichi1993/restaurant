# Sử dụng JDK 17
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy file JAR sau khi build
COPY target/*.jar app.jar

# Expose port backend
EXPOSE 8080

# Lệnh chạy Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
