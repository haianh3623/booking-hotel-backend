# Sử dụng Java 21 runtime (Debian slim thay vì Alpine)
FROM openjdk:21-jdk-slim

# Tạo thư mục chứa app
WORKDIR /booking-hotel-backend

# Copy file jar vào image
COPY target/booking-hotel-backend-0.0.1-SNAPSHOT.jar app.jar

# Mở port ứng dụng
EXPOSE 8080

# Lệnh chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]
