FROM java:8-jdk as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
FROM base as build
RUN ./mvnw package -Dmaven.test.skip=true
FROM openjdk:11-jre-slim as production
COPY --from=build /app/target/truthtables-*.jar /truthtables.jar
CMD ["java", "-jar", "/truthtables.jar"]