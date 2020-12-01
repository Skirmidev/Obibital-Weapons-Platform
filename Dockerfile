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

COPY docker-entrypoint.sh /

COPY /src/main/data/configValues.csv /
COPY /src/main/data/moduleValues.csv /

#fix line endings windows -> linux
RUN sed -i 's/\r$//' configValues.csv
#ensure executable
RUN chmod +x /docker-entrypoint.sh

# install postgresql
RUN apk add postgresql \
&& apk add postgresql-contrib

# TODO: determine if necessary
RUN chown -R postgres:postgres /usr/bin/postgres

#essential fixes for postgres to run
RUN mkdir /var/run/postgresql
RUN chown -R postgres:postgres /var/run/postgresql

ENTRYPOINT ["/docker-entrypoint.sh"]