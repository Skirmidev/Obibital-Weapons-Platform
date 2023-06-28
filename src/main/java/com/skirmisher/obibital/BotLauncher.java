package com.skirmisher.obibital;

import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotLauncher {
    public static void main(String [] args){

        try{

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            ObibitalWeaponsPlatform bot = new ObibitalWeaponsPlatform();

            botsApi.registerBot(bot);

            //confirm that bot redeployed
            bot.announceRedeployment();
            
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println("Exception while bot was running");
        }
    }
}