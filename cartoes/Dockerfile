FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY modular/pom.xml modular/pom.xml
COPY cartoes/.mvn cartoes/.mvn
COPY cartoes/mvnw cartoes/pom.xml cartoes/

WORKDIR /app/cartoes

RUN chmod +x mvnw && ./mvnw dependency:resolve --no-transfer-progress -Dmaven.main.skip -Dmaven.test.skip

COPY cartoes/src/ src/

RUN ./mvnw package --no-transfer-progress -DskipTests


FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

WORKDIR /app

COPY --from=build /app/cartoes/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
