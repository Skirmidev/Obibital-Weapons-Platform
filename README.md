# Obibital Weapons Platform
A moderation bot built for Telegram

## Configuration:
Please refer to the samples in `src/main/data/Samples` provided.

 the config.csv must have the following data:  
 botusername - the username of your bot  
 bottoken - the token provided by godfatherbot
 groupId - the group the bot is intended to function in. This must be a supergroup (see telegram documentation).

*Configured files should be placed in `src/main/data`*

 # Building & Running the bot
The current pom configuration requires a local docker registry and running daemon to build succesfully.

Upon running `mvn clean install` any existing bot container will be removed, and a new one will replace it, automatically binding to the data path. The container name will be `obibital-platform`

Modification of the pom can disable these additional steps