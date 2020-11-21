package com.skirmisher.obibital;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import com.skirmisher.data.DBLoader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember;

public class CallbackManager {

    static Pattern p_userId = Pattern.compile("id: [0-9]+");
    static Matcher m_userId;


    public static Context manageGroupCallback(Context context, Update update, ObibitalWeaponsPlatform bot, String[] callback){
        System.out.println("CallbackManager:: manageGroupCallback:: " +callback[1]);
        switch (callback[1]) {
            case "angewy":
                //angewy(context, update, bot);
                break;
            
            case "peacefur":
                //peacefur(context, update, bot);
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
        
        case "peacefur":
            //peacefur(context, update, bot);
            break;
    
        case "messagey":
            //messagey(context, update, bot);
            break;
        case "newjoin":
            newJoin(context, update, bot, callback);
            break;
        }
        return context;
    }

    ////////////////////////
    // newjoin - ban/safe //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // messageFormat: "New user has joined: " + user.getFirstName()+" "+user.getLastName() + "(" + user.getUserName() +") " + "[ID: " + user.getId() + "]" //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void newJoin(Context context, Update update, ObibitalWeaponsPlatform bot, String[] callback){
        context.setBlockingResult(true);
        context.setResult("Callback:: admin:: newJoin");

        if(update.getCallbackQuery().getMessage() == null){
            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            answer.setCallbackQueryId(update.getCallbackQuery().getId());
            answer.setShowAlert(true);
            answer.setText("Message is too old to interact succesfully");
    
            try{
                bot.execute(answer);
            } catch (TelegramApiException e){
                e.printStackTrace();
            }
        } else {
            String originalMessage = update.getCallbackQuery().getMessage().getText();

            m_userId = p_userId.matcher(originalMessage);

            String userid = "";
            if (m_userId.find() ) {
                String[] splittin = m_userId.group(0).split(" ");
                userid=splittin[1];
                System.out.println("userid: " + userid);

                EditMessageText editM = new EditMessageText();

                editM.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editM.setChatId(update.getCallbackQuery().getChatInstance().toString());
                editM.setChatId("" + update.getCallbackQuery().getMessage().getChat().getId());

                switch(callback[2]){
                    case "ban":
                        try{
                            System.out.println("alpha");
                            //todo: add actual banning
                            KickChatMember kick = new KickChatMember(bot.getGroupChatId().toString(), Integer.parseInt(userid));
                            System.out.println("beta");
                            bot.execute(kick);
                            System.out.println("charlie");

                            //messages
                            editM.setParseMode("MarkdownV2");
                            System.out.println("delta");
                            String response = "~~" + originalMessage + "~~\r banned by " + update.getCallbackQuery().getFrom().getFirstName() + " " + update.getCallbackQuery().getFrom().getLastName();
                            System.out.println("echo");
                            response = Utilities.makeMarkdownFriendly(response);
                            System.out.println("foxtrot");
                            editM.setParseMode("MarkdownV2");
                            System.out.println("gamma");
                            editM.setText(response);
                            System.out.println("hotel");
                            bot.execute(editM);
                            System.out.println("india");
                            DBLoader.logEvent("BAN", update.getCallbackQuery().getFrom().getId().toString(), userid, "New join");
                            System.out.println("jacob");
                        } catch (TelegramApiException e){
                            System.out.println(e.toString());
                            e.printStackTrace();
                        }
                        break;
                    case "safe":
                        editM.setParseMode("MarkdownV2");
                        String response = "~~" + originalMessage + "~~\r deemed safe by " + update.getCallbackQuery().getFrom().getFirstName() + " " + update.getCallbackQuery().getFrom().getLastName();
                        response = Utilities.makeMarkdownFriendly(response);
                        editM.setText(response);
                        try{
                            bot.execute(editM);
                            DBLoader.logEvent("MARK SAFE", update.getCallbackQuery().getFrom().getId().toString(), userid, "New Join");
                        } catch (TelegramApiException e){
                            System.out.println(e.toString());
                            e.printStackTrace();
                        }
                        break;
                }
                
                AnswerCallbackQuery answer = new AnswerCallbackQuery();
                answer.setCallbackQueryId(update.getCallbackQuery().getId());
                answer.setShowAlert(false);
        
                try{
                    bot.execute(answer);
                } catch (TelegramApiException e){
                    e.printStackTrace();
                }
            } else {
                System.out.print("Userid not found - error");
            }

            
        }
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