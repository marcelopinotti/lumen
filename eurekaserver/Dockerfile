FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY modular/pom.xml modular/pom.xml
COPY eurekaserver/.mvn eurekaserver/.mvn
COPY eurekaserver/mvnw eurekaserver/pom.xml eurekaserver/

WORKDIR /app/eurekaserver

RUN chmod +x mvnw && ./mvnw dependency:resolve --no-transfer-progress -Dmaven.main.skip -Dmaven.test.skip

COPY eurekaserver/src/ src/

RUN ./mvnw package --no-transfer-progress -DskipTests


FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

WORKDIR /app

EXPOSE 8761

COPY --from=build /app/eurekaserver/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
