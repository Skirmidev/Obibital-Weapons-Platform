package com.skirmisher.obibital;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;

public class CallbackManager {
    public static void manageGroupCallback(Context context, Update update, ObibitalWeaponsPlatform bot, String[] callback){
        System.out.println("callback[1] is: " + callback[1]);
        switch (callback[1]) {
            case "angewy":
                System.out.println("angewy has been identified");
                //angewy(context, update, bot);
                break;
            
            case "peacefur":
                System.out.println("peacefur has been identified");
                //peacefur(context, update, bot);
                break;
        
            case "messagey":
                System.out.println("messagey has been identified");
                //messagey(context, update, bot);
                break;
        }
    }

    
    public static void manageAdminCallback(Context context, Update update, ObibitalWeaponsPlatform bot, String[] callback){
    }

    static void angewy(Context context, Update update, ObibitalWeaponsPlatform bot){
        context.setBlockingResult(true);
        context.setResult("Callback:: group:: angewy");


        AnswerCallbackQuery answer = new AnswerCallbackQuery();
		answer.setCallbackQueryId(update.getCallbackQuery().getId());
		answer.setShowAlert(true);
		answer.setText("YOU HAVE PWESSED THE ANGEWY BUTTON ( ͡⚆ ͜ʖ ͡⚆)╭∩╮");

        try{
            bot.execute(answer);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    static void peacefur(Context context, Update update, ObibitalWeaponsPlatform bot){
        context.setBlockingResult(true);
        context.setResult("Callback:: group:: angewy");


        AnswerCallbackQuery answer = new AnswerCallbackQuery();
		answer.setCallbackQueryId(update.getCallbackQuery().getId());
		answer.setShowAlert(false);
		answer.setText("peace be with you, my child");

        try{
            bot.execute(answer);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    static void messagey(Context context, Update update, ObibitalWeaponsPlatform bot){
        context.setBlockingResult(true);
        context.setResult("Callback:: group:: angewy");


        AnswerCallbackQuery answer = new AnswerCallbackQuery();
		answer.setCallbackQueryId(update.getCallbackQuery().getId());

        try{
            bot.execute(answer);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }

        //System.out.println(update);
        
        SendMessage message = new SendMessage();
        message.setChatId(update.getCallbackQuery().getMessage().getChat().getId().toString());
        message.setText("A button was pressed. Now, we shall all suffer greatly.");

        bot.send(message);
    }
}