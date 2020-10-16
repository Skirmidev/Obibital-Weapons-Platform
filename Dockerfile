FROM openjdk:7-jre-alpine

ARG JAR_FILE
COPY target/telegram-modbot.jar /telegram-modbot.jar

ENTRYPOINT ["java", "-jar", "/telegram-modbot.jar"]