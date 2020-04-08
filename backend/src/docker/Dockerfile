FROM openjdk:8-jdk-alpine
ARG JAR_FILE=backend/target/*.jar
COPY ${JAR_FILE} backend.jar
ENTRYPOINT ["java","-jar","/backend.jar"]