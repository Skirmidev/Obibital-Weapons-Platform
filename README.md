# Obibital Weapons Platform
A moderation bot built for Telegram

## Configuration:
Please refer to the samples in `src/main/resources/Samples` provided.

 the config.csv must have the following data:  
 botusername - the username of your bot  
 bottoken - the token provided by godfatherbot
 groupId - the group the bot is intended to function in (your own userId if you wish to play with it directly)

*Configured files should be placed in `src/main/resources`*

## Building the bot:
`mvn clean install` run in the root directory.

## Running the bot

java -jar ./target/telegram-modbot-0.0.1.jar

##docker running the bot INDEV

 docker run -it --mount type=bind,source=D:\Files\Projects\Programming\VersionControlled\TelegramModerationBot\dockerdata,target=/src/main/data telegram-modbot