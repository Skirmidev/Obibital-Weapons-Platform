package com.skirmisher.obibital;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.Executors;

import com.skirmisher.data.DBLoader;
import com.skirmisher.processors.StickerPackBanner;

import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.*;

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

        DBLoader.updateTimerId();

        //set poller to run every minute
        final Runnable poller = new TimerPoller(bot);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final ScheduledFuture<?> timer = scheduler.scheduleAtFixedRate(poller, 1, 1, MINUTES);
    }
}