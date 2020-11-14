package com.skirmisher.obibital;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

import com.skirmisher.data.*;

public class ObibitalWeaponsPlatform extends TelegramLongPollingBot {
    boolean debug = true;

    Long groupId = 0l;
    Long modChatId = 0l;
    List<Long> admins = new ArrayList<>();

    @Override
    public void onUpdateReceived(Update update) {
        Context context = new Context();

        //switch on chat ID
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
            System.out.println("ObibitalWeaponsPlatform:: New Update Received - unidentified source");
            System.out.println(update);
        }
        
        if (update.hasCallbackQuery()){
            System.out.println("CALLBACK CONTENTS: " + update.getCallbackQuery().getData());
        }

        if(debug){
            System.out.println("Update: " + update);
            System.out.println("Context Result: " + context.getResult());
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

