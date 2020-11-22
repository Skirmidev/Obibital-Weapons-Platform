FROM alpine:3.7

# install Java
RUN apk update \
&& apk upgrade \
&& apk add --no-cache bash \
&& apk add --no-cache --virtual=build-dependencies unzip \
&& apk add --no-cache curl \
&& apk add --no-cache openjdk8-jre

ENV JAVA_HOME="/usr/lib/jvm/java-1.8-openjdk"

# install postgresql
RUN apk add postgresql \
&& apk add postgresql-contrib

ARG JAR_FILE
COPY target/telegram-modbot.jar /telegram-modbot.jar

USER postgres

RUN /etc/init.d/postgresql start &&\
    psql --command "CREATE USER postgresondocker WITH SUPERUSER PASSWORD 'postgresondocker';" &&\
    createdb -O postgresondocker postgresondocker

# Adjust PostgreSQL configuration so that remote connections to the
# database are possible.
RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.3/main/pg_hba.conf
 
# And add ``listen_addresses`` to ``/etc/postgresql/9.3/main/postgresql.conf``
RUN echo "listen_addresses='*'" >> /etc/postgresql/9.3/main/postgresql.conf
 
# Expose the PostgreSQL port
EXPOSE 5432
 
# Add VOLUMEs to allow backup of config, logs and databases
VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]
 
# Set the default command to run when starting the container
CMD ["/usr/lib/postgresql/9.3/bin/postgres", "-D", "/var/lib/postgresql/9.3/main", "-c", "config_file=/etc/postgresql/9.3/main/postgresql.conf"]

ENTRYPOINT ["java", "-jar", "/telegram-modbot.jar"]