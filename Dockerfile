# Stage 1: Build the application with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml, source code, test code, testng config, and webpage folder
COPY pom.xml .
COPY src ./src
COPY testng.xml .
COPY webpage ./webpage

# Build package while skipping tests during docker build (since tests run in Job 1)
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime container image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar and webpage assets from the build stage
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/webpage ./webpage

ENTRYPOINT ["java", "-jar", "app.jar"]