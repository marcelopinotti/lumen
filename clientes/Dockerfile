FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY modular/pom.xml modular/pom.xml
COPY clientes/.mvn clientes/.mvn
COPY clientes/mvnw clientes/pom.xml clientes/

WORKDIR /app/clientes

RUN chmod +x mvnw && ./mvnw dependency:resolve --no-transfer-progress -Dmaven.main.skip -Dmaven.test.skip

COPY clientes/src/ src/

RUN ./mvnw package --no-transfer-progress -DskipTests


FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

WORKDIR /app

COPY --from=build /app/clientes/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
