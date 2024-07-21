From maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/jwtAuth-0.0.1-SNAPSHOT.jar jwtAuth.jar
EXPOSE 9000
ENTRYPOINT [ "java","-jar","jwtAuth.jar" ]