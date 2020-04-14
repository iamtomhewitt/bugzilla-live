# mvn - build & package
FROM maven:3.6.0-jdk-11-slim AS build
COPY backend/src /home/app/src
COPY backend/pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Run
FROM openjdk:8-alpine
COPY --from=build /home/app/target/*.jar backend.jar
ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://db:27017/bugzilla-live", "-Djava.security.egd=file:/dev/./urandom", "-jar","/backend.jar"]