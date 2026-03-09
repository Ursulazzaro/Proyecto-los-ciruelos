FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /app/target/los_ciruelos-0.0.1.jar app_los_ciruelos.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app_los_ciruelos.jar" ]