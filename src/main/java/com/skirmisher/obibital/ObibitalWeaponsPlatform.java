package com.skirmisher.obibital;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.skirmisher.data.*;

public class ObibitalWeaponsPlatform extends TelegramLongPollingBot {

    Long groupId = 0l;
    Long modChatId = 0l;
    ArrayList<Long> admins = new ArrayList<>();

    @Override
    public void onUpdateReceived(Update update) {
        Context context = new Context();

        System.out.println("ObibitalWeaponsPlatform:: New Update Received");
        System.out.println(update);

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
        }
    }

    @Override
    public String getBotUsername() {
        return config("botusername");
    }


    @Override
    public String getBotToken() {
        return config("bottoken");
    }

    public String config(String element){
        try {
            List<ConfigBean> config = DBLoader.loadConfig();

            for(ConfigBean bean : config){
                if(bean.getElement().equals(element)){
                    return bean.getValue();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("FAILEDTOLOAD: " + element);
        return "FAILEDTOLOAD: " + element;
    }

    public void updateConfig(String element, String value){
        try {
            List<ConfigBean> config = DBLoader.loadConfig();

            for(ConfigBean bean : config){
                if(bean.getElement().equals(element)){
                    bean.setValue(value);
                }
            }

            DBLoader.saveConfig(config);
        } catch (IOException e){
            e.printStackTrace();
        }
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
        try {
            List<ConfigBean> config = DBLoader.loadConfig();

            for(ConfigBean bean : config){
                if(bean.getElement().equals("groupId")){
                    groupId = Long.parseLong(bean.getValue());
                }

                if(bean.getElement().equals("modChatId")){
                    modChatId = Long.parseLong(bean.getValue());
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

