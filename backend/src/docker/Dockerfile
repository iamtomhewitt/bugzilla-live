FROM openjdk:8-alpine
ARG JAR_FILE=backend/target/*.jar
COPY ${JAR_FILE} backend.jar
ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://db:27017/bugzilla-live","-Djava.security.egd=file:/dev/./urandom", "-jar","/backend.jar"]