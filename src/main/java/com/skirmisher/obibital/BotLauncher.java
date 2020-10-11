package com.skirmisher.obibital;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.skirmisher.processors.StickerPackBanner;

public class BotLauncher {
    public static void main(String [] args){
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();
        ObibitalWeaponsPlatform bot = new ObibitalWeaponsPlatform();

        StickerPackBanner.reloadBannedPacks();

        bot.reloadConfig();

        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}