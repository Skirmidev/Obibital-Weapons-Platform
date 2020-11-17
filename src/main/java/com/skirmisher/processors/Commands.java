package com.skirmisher.processors;

import org.telegram.telegrambots.meta.api.objects.Update;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.skirmisher.obibital.Context;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

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
                    case "/buttontest":
                        buttonTest(update, bot);
                        context.setResult("Commands: ButtonTest command");
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
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(   "OBIBITAL WEAPONS PLATFORM V-0.0.1"  + "\n" +
                            "This is a standardised help message"  + "\n" +
                            "IF you find this helpful, something is wrong"  + "\n" +
                            "");

        bot.send(message);
    }

    public static void unrecognisedCommand(Update update, ObibitalWeaponsPlatform bot){
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText("Your command was not recognised.");

        bot.send(message);
    }

    public static void buttonTest(Update update, ObibitalWeaponsPlatform bot){
        
        final List<InlineKeyboardButton> keyboard = new ArrayList<InlineKeyboardButton>(3);

        InlineKeyboardButton but = new InlineKeyboardButton();
        but.setText("Do Not Press");
        but.setCallbackData("group:angewy");

        
        InlineKeyboardButton but2 = new InlineKeyboardButton();
        but2.setText("Press");
        but2.setCallbackData("group:peacefur");

        
        InlineKeyboardButton but3 = new InlineKeyboardButton();
        but3.setText("This will generate a message");
        but3.setCallbackData("group:messagey");

        
        keyboard.add(but);
        keyboard.add(but2);
        keyboard.add(but3);
        
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboard));
        
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(update.getMessage().getText());
        message.setReplyMarkup(inlineKeyboardMarkup);

        try{
            bot.execute(message);
        } catch ( TelegramApiException e) {
            e.printStackTrace();
        }
        
    }
}