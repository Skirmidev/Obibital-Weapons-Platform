package com.skirmisher.obibital;

import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.Executors;

import com.skirmisher.data.DBLoader;
import com.skirmisher.processors.StickerPackBanner;
import com.skirmisher.obibital.ModuleControl;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.concurrent.TimeUnit.*;

public class BotLauncher {
    public static void main(String [] args){

        try{
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            ObibitalWeaponsPlatform bot = new ObibitalWeaponsPlatform();
            
            System.out.println("Bot options: " + bot.getOptions().getAllowedUpdates());
            //bot.getOptions().setAllowedUpdates(allowedUpdates);

            StickerPackBanner.reloadBannedPacks();

            ModuleControl.reloadModules();

            bot.reloadConfig();

            botsApi.registerBot(bot);

            DBLoader.updateTimerId();

            //set poller to run every minute
            final Runnable poller = new TimerPoller(bot);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            final ScheduledFuture<?> timer = scheduler.scheduleAtFixedRate(poller, 1, 1, MINUTES);
            
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println("Exception while bot was running");
        }
    }
}