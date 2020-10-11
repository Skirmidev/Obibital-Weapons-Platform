package com.skirmisher.processors;

import org.telegram.telegrambots.meta.api.objects.Update;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.skirmisher.obibital.Context;

public class Commands {
    static char prefix = '/';

    public static Context command(Context context, Update update, ObibitalWeaponsPlatform bot){
        System.out.println("Commands:: commands");
        if(update.getMessage().hasText() && !context.isBlockingResult()){
            if(update.getMessage().getText().charAt(0) == prefix){

                //this is a command. Switch to find out what it does.
                switch(update.getMessage().getText()){
                    case "/help":
                        help(update, bot);
                        context.setResult("Commands: Help command");
                        context.setBlockingResult(true);
                        break;
                    default:
                        unrecognisedCommand(update, bot);
                        break;
                }
            }
        }
        return context;
    }

    public static void help(Update update, ObibitalWeaponsPlatform bot){
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(   "OBIBITAL WEAPONS PLATFORM V-0.0.1"  + "\n" +
                            "This is a standardised help message"  + "\n" +
                            "IF you find this helpful, something is wrong"  + "\n" +
                            "");

        bot.send(message);
    }

    public static void unrecognisedCommand(Update update, ObibitalWeaponsPlatform bot){
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("Your command was not recognised.");

        bot.send(message);
    }
}