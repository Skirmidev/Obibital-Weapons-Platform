package com.skirmisher.obibital;

import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.Executors;

import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.*;

public class BotLauncher {
    public static void main(String [] args){

        try{

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            ObibitalWeaponsPlatform bot = new ObibitalWeaponsPlatform();

            ModuleControl.reloadModules();

            bot.reloadConfig();

            botsApi.registerBot(bot);

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