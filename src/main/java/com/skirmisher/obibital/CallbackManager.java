package com.skirmisher.obibital;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CallbackManager {

    static Pattern p_userId = Pattern.compile("id: [0-9]+");
    static Matcher m_userId;


    public static Context manageGroupCallback(Context context, Update update, ObibitalWeaponsPlatform bot, String[] callback){
        System.out.println("CallbackManager:: manageGroupCallback:: " + callback[1]);
        switch (callback[1]) {
            case "feedback":
                feedback(context, update, bot, callback);
                break;
            
            case "peaceful":
                //peaceful(context, update, bot);
                break;
        
            case "messagey":
                //messagey(context, update, bot);
                break;
        }
        return context;
    }

    
    public static Context manageAdminCallback(Context context, Update update, ObibitalWeaponsPlatform bot, String[] callback){
        System.out.println("CallbackManager:: manageAdminCallback:: " + callback[1]);
        switch (callback[1]) {
        case "angewy":
            //angewy(context, update, bot);
            break;
        
        case "peaceful":
            //peaceful(context, update, bot);
            break;
    
        case "messagey":
            //messagey(context, update, bot);
            break;
        }
        return context;
    }

    static void feedback(Context context, Update update, ObibitalWeaponsPlatform bot, String[] callback){
        context.setBlockingResult(true);
        context.setResult("Callback:: group:: feedback");

        switch(callback[2]){
            case "good":
                //TODO log the response in the DB, tie userid to messageid to good/bad
                break;
            case "bad":
                //TODO log the response in the DB, tie userid to messageid to good/bad
                break;
        }

        
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(update.getCallbackQuery().getId());
        answer.setShowAlert(false);
        answer.setText("Thank you for your feedback");

        try{
            bot.execute(answer);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    static void angewy(Context context, Update update, ObibitalWeaponsPlatform bot){
        context.setBlockingResult(true);
        context.setResult("Callback:: group:: angewy");


        AnswerCallbackQuery answer = new AnswerCallbackQuery();
		answer.setCallbackQueryId(update.getCallbackQuery().getId());
		answer.setShowAlert(true);
		answer.setText("YOU HAVE PWESSED THE ANGEWY BUTTON");

        try{
            bot.execute(answer);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    static void peaceful(Context context, Update update, ObibitalWeaponsPlatform bot){
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