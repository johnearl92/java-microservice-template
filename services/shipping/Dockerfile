# Stage 1: Cache and build dependencies
FROM gradle:8.5.0-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN ./gradlew clean build -x test

# Stage 2: Runtime image with Corretto 21
FROM amazoncorretto:21
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]