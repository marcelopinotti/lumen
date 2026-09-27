FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY modular/pom.xml modular/pom.xml
COPY cloudgateway/.mvn cloudgateway/.mvn
COPY cloudgateway/mvnw cloudgateway/pom.xml cloudgateway/

WORKDIR /app/cloudgateway

RUN chmod +x mvnw && ./mvnw dependency:resolve --no-transfer-progress -Dmaven.main.skip -Dmaven.test.skip

COPY cloudgateway/src/ src/

RUN ./mvnw package --no-transfer-progress -DskipTests


FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

WORKDIR /app

EXPOSE 8080

COPY --from=build /app/cloudgateway/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
