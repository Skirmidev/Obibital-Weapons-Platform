# Pull base image
FROM openjdk:8-jdk-alpine

# Set Environment properties
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
CMD ["/usr/bin/java","-jar","/telegram-modbot-0.0.1.jar"]