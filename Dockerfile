# Stage 1: build the jar with Maven
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw -q clean package -DskipTests

# Stage 2: run the jar with a smaller Java runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/daily-study-planner-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]