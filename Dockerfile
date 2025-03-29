FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from target directory
COPY target/trading_app-0.0.1-SNAPSHOT.jar app.jar

# Command to run the app
CMD ["java", "-jar", "app.jar"]