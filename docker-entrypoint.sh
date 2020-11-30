#!/bin/bash

chown postgres:postgres -R /database

# CREATE DATABASE 
su -c "initdb -D /database/config/" postgres

# ADJUSTMENT TO WORK WITH DOCKER
sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '\*'/g" /database/config/postgresql.conf


# START DATABASE
su -c "postgres -D /database/config/ >/database/log.log 2>&1" postgres&

# WAIT UNTIL DB STARTS
RETRIES=5
until psql -U postgres -c "select 1" > /dev/null 2>&1 || [ $RETRIES -eq 0 ]; do   
    echo "Waiting for postgres server to start, $((RETRIES)) remaining attempts..."   RETRIES=$((RETRIES-=1))   
    sleep 1 
done

# CREATE DATABASE
su -c "createdb obibital-db" postgres

# CREATE TABLESPACE
psql -U postgres -d obibital-db -c"CREATE TABLESPACE obibitalTables LOCATION '/database/tables';"

# CHECK IF CONFIG EXISTS
tableExists=$(psql -t -U postgres -d obibital-db -c"SELECT * FROM information_schema.tables WHERE table_name='config';")

echo "does table existing"
echo $tableExists
echo "that should be clear"

# CREATE TABLE: CONFIG
psql -U postgres -d obibital-db -c"CREATE TABLE IF NOT EXISTS config ( element VARCHAR ( 50 ) PRIMARY KEY UNIQUE NOT NULL, value VARCHAR ( 500 ));"

# CREATE TABLE: USERS
psql -U postgres -d obibital-db -c"CREATE TABLE IF NOT EXISTS users ( userid integer PRIMARY KEY UNIQUE NOT NULL, username VARCHAR ( 50 ), name VARCHAR ( 50 ) NOT NULL, isadmin BOOLEAN DEFAULT false);"

# CREATE TABLE: BANNEDSTICKERS
psql -U postgres -d obibital-db -c"CREATE TABLE IF NOT EXISTS stickerbans ( packname VARCHAR ( 50 ) UNIQUE NOT NULL, bandate TIMESTAMP default current_timestamp);"

# CREATE TABLE: LOGGING
psql -U postgres -d obibital-db -c"CREATE TABLE IF NOT EXISTS timers ( id serial PRIMARY KEY UNIQUE NOT NULL, action VARCHAR ( 20 ) NOT NULL, args VARCHAR(100), expiry TIMESTAMP default current_timestamp);"

# CREATE TABLE: TIMERS
psql -U postgres -d obibital-db -c"CREATE TABLE IF NOT EXISTS logging ( id serial PRIMARY KEY UNIQUE NOT NULL, event VARCHAR ( 50 ), sourceuser integer REFERENCES users (userid), affecteduser integer REFERENCES users( userid ), notes VARCHAR ( 100 ), date TIMESTAMP default current_timestamp);"


#if config did not exist, populate config
if test -z $tableExists 
then
    echo "Populating new config"

    while IFS=, read -r element value; do
        psql -U postgres -d obibital-db -c"INSERT INTO config ( element, value ) VALUES ('$element', '$value');"
    done < configValues.csv

else
    echo "Config already exists"
fi

#run telegram modbot
java -jar /telegram-modbot.jar




#psql -U postgres -d obibital-db -c"SELECT * FROM config;"