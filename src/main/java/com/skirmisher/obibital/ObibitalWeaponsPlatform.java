package com.skirmisher.obibital;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import java.util.Collections;

import com.skirmisher.data.*;

public class ObibitalWeaponsPlatform extends TelegramLongPollingBot {
    boolean debug = false;

    Long groupId = 0l;
    Long modChatId = 0l;
    List<Long> admins = new ArrayList<>();

    //issue is not caused by wrong values present. :/
    // public ObibitalWeaponsPlatform(DefaultBotOptions options) {
    //     super(options);
    // }

    @Override
    public void onUpdateReceived(final Update update) {
        Context context = new Context();

        if(update.hasCallbackQuery()){
            ////////////////////
            // Callback Query //
            ////////////////////
            System.out.println("ObibitalWeaponsPlatform:: onUpdateReceived:: callback Query");
            String callback = update.getCallbackQuery().getData();
            String [] callSplit = callback.split(":");
            if(callSplit[0].equals("group")) {
                // Group //
                CallbackManager.manageGroupCallback(context, update, this, callSplit);
            } else if (callSplit[0].equals("admin")) {
                // Admin //
                CallbackManager.manageAdminCallback(context, update, this, callSplit);
            }

        } else if (update.hasMessage()) {
            /////////////
            // Message //
            /////////////
            System.out.println("ObibitalWeaponsPlatform:: onUpdateReceived:: message");
            if(update.getMessage().getChatId().equals(groupId)) {
                ModuleControl.runChatModules(context, update, this);
                //message in the group, process as normal
            } else if (admins.contains(update.getMessage().getChatId())){
                //message from an approved admin, permit command interface
                AdminInterface.run(context, update, this);
            } else if (update.getMessage().getChatId().equals(modChatId)) {
                //message in the mod chat, permit command interface
                AdminInterface.run(context, update, this);
            } else {
                System.out.println("ObibitalWeaponsPlatform:: onUpdateReceived:: message from unknown source: " + update.getMessage().getChatId());
                System.out.println(update);
            }

        } else {
            /////////////////
            // Unsupported //
            /////////////////
            System.out.println("unrecognised update");
        }
    }

    @Override
    public String getBotUsername() {
        return DBLoader.configValue("botusername");
    }


    @Override
    public String getBotToken() {
        return DBLoader.configValue("bottoken");
    }
    
    public Long getModChatId() {
        return modChatId;
    }
    
    public Long getGroupChatId() {
        return groupId;
    }

    public void send(SendMessage message){
        try {
            System.out.println("Sending message: " + message.getText() + " To chat ID: " + message.getChatId());
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendBulk(ArrayList<SendMessage> messages){
        for(SendMessage message : messages){
            send(message);
        }
    }

    public void reloadConfig(){
        groupId = Long.parseLong(DBLoader.configValue("groupId"));
        modChatId = Long.parseLong(DBLoader.configValue("modChatId"));
        admins=DBLoader.getAdmins();
    }
}

