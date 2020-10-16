FROM  arm32v7/adoptopenjdk:11-jre-hotspot

ARG JAR_FILE
COPY target/telegram-modbot.jar /telegram-modbot.jar

ENTRYPOINT ["java", "-jar", "/telegram-modbot.jar"]