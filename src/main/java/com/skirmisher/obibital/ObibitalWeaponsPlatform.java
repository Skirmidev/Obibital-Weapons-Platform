package com.skirmisher.obibital;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.time.Duration;
import java.time.Instant;

import com.skirmisher.processors.*;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;

import com.skirmisher.data.*;

public class ObibitalWeaponsPlatform extends TelegramLongPollingBot {
    boolean debug = false;

    HashMap<Long,String> chatIDs = new HashMap<Long, String>();
    HashMap<String,Long> chatIDsInverse = new HashMap<String,Long>();

    StickerSpam sfwSpam = new StickerSpam();
    StickerSpam nsfwSpam = new StickerSpam();

    public ObibitalWeaponsPlatform() {
        super(DBLoader.configValue("bottoken"));

        for(Chats i : Chats.values()){
            //System.out.println("Loading chat: " + i.toString() + " with id: " + DBLoader.configValue(i.toString()));
            chatIDs.put(Long.parseLong(DBLoader.configValue(i.toString())),i.toString());
        }

        //chatIDsInverse is just the above reversed
        for(Entry<Long,String> entry : chatIDs.entrySet()){
            chatIDsInverse.put(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public void onUpdateReceived(final Update update) {
        Context context = new Context();

        String intendedChat = "";
        if(update.hasMessage()){
            intendedChat = chatIDs.get(update.getMessage().getChatId());
        } else if (update.hasChannelPost()) {
            intendedChat = chatIDs.get(update.getChannelPost().getChatId());
        }

        switch (Chats.fromString(intendedChat)) {
            case SFW:
                context = sfwSpam.spamCheck(context, update, this);
                context = NewJoin.kickFromLobby(context, update, this);
                break;
            case EVENTS:
                //no current intended use
                //topic creation/deletion/automation? discuss with events team
                break;
            case LOBBY:
                break;
            case NSFW:
                context = nsfwSpam.spamCheck(context, update, this);
                context = NewJoin.kickFromLobby(context, update, this);
                break;
            case MOD:
                GenerateLink.nsfwLink(context, update, this);
                break;
            case ANNOUNCE:
                EventBroadcast.broadcast(context, update, this);
                break;
            default:
                //no chat selected
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

    public void send(SendMessage message){
        try {
            //System.out.println("Sending message: " + message.getText() + " To chat ID: " + message.getChatId());
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

    public Message forward(Long fromChat, Long toChat, Integer messageId){ //need to determine if message stays pinned
        ForwardMessage forward = new ForwardMessage();
        forward.setFromChatId(fromChat);
        forward.setChatId(toChat);
        forward.setMessageId(messageId);

        //System.out.println("Forwarding: " + forward.toString());

        try{
            return this.execute(forward);    
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message forward(String fromChatString, String toChatString, Integer messageId){ //need to determine if message stays pinned
        Long fromChat = chatIDsInverse.get(fromChatString);
        Long toChat = chatIDsInverse.get(toChatString);
        return forward(fromChat, toChat, messageId);
    }

    public void pin(Long inChat, Integer messageId){
        PinChatMessage pinMessage = new PinChatMessage();
        pinMessage.setChatId(inChat);
        pinMessage.setMessageId(messageId);
        pinMessage.setDisableNotification(true); //never notify all members of a chat

        //System.out.println("Pinning: " + pinMessage.toString());

        try{
            this.execute(pinMessage);    
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void pin(String inChatString, Integer messageId){
        Long inChat = chatIDsInverse.get(inChatString);
        pin(inChat, messageId);
    }

    public void kick(Long fromChat, Long userId, boolean deleteMessages){
        BanChatMember kick = new BanChatMember();
        kick.setChatId(fromChat);
        kick.setUserId(userId);
        kick.setRevokeMessages(deleteMessages); //true if delete all messages
        kick.forTimePeriodDuration(Duration.ofSeconds(60l)); //removed for 1 minute
        try{
            this.execute(kick);    
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void kick(String fromChatString, Long userId, boolean deleteMessages){
        Long fromChat = chatIDsInverse.get(fromChatString);
        kick(fromChat, userId, deleteMessages);
    }

    public ChatInviteLink generateInvite(Long chatId){
        //get current unix time
        int expire = (int) Instant.now().getEpochSecond();
        expire = expire + 86400; //1 day expiry
        CreateChatInviteLink invite = new CreateChatInviteLink();
        invite.setChatId(chatId);
        invite.setMemberLimit(1);
        invite.setExpireDate(expire);
        
        try{
            return this.execute(invite);    
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChatInviteLink generateInvite(String chatIdString){
        Long chatId = chatIDsInverse.get(chatIdString);
        return generateInvite(chatId);
    }

    public void announceRedeployment(){
        SendMessage message = new SendMessage();
        message.setChatId(chatIDsInverse.get(Chats.MOD.toString()));
        message.setText("Bot redeployed succesfully");
        message.disableNotification();
        send(message);
    }
}

