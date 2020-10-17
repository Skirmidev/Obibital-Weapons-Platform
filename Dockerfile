FROM alpine:3.7

# install Java
RUN apk update \
&& apk upgrade \
&& apk add --no-cache bash \
&& apk add --no-cache --virtual=build-dependencies unzip \
&& apk add --no-cache curl \
&& apk add --no-cache openjdk8-jre

ENV JAVA_HOME="/usr/lib/jvm/java-1.8-openjdk"

ARG JAR_FILE
COPY target/telegram-modbot.jar /telegram-modbot.jar

ENTRYPOINT ["java", "-jar", "/telegram-modbot.jar"]