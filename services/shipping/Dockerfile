# Stage 1: Cache dependencies
FROM gradle:8.5.0-jdk21 AS dependencies
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon

# Stage 2: build the application
FROM gradle:8.5.0-jdk21 AS build
WORKDIR /app
COPY --from=dependencies /home/gradle/.gradle /home/gradle/.gradle
COPY . .
RUN ./gradlew clean bootJar --no-daemon

# Stage 3: Runtime image with Corretto 21
FROM amazoncorretto:21
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]