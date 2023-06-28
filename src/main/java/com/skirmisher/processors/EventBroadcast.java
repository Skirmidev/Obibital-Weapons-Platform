package com.skirmisher.processors;

import com.skirmisher.obibital.Context;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import com.skirmisher.obibital.Chats;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class EventBroadcast {
    public static Context broadcast(Context context, Update update, ObibitalWeaponsPlatform bot){
        if(!context.isBlockingResult()){
            Integer messageId = update.getChannelPost().getMessageId();
            //SFW
            Message sfw = bot.forward(Chats.ANNOUNCE.toString(), Chats.SFW.toString(), messageId);
            bot.pin(Chats.SFW.toString(), sfw.getMessageId());

            
            //NSFW
            Message nsfw = bot.forward(Chats.ANNOUNCE.toString(), Chats.NSFW.toString(), messageId);
            bot.pin(Chats.NSFW.toString(), nsfw.getMessageId());

            context.setResult("EventBroadcast :: forwarded to SFW and NSFW");
            context.setBlockingResult(true);
        }
        return context;
    }
}
