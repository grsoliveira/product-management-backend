FROM maven:3.8.7-amazoncorretto-17 AS build
COPY /src /app/src
COPY /pom.xml /app
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip

FROM amazoncorretto:17-alpine3.17-jdk
EXPOSE 80
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
