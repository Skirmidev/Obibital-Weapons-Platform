package com.skirmisher.processors;
import com.skirmisher.obibital.Context;
import com.skirmisher.obibital.Chats;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import com.skirmisher.obibital.ObibitalWeaponsPlatform;

public class NewJoin {
    public static Context kickFromLobby(Context context, Update update, ObibitalWeaponsPlatform bot){
        if(!context.isBlockingResult() &! (update.getMessage().getNewChatMembers().isEmpty())){
            for(User i : update.getMessage().getNewChatMembers()){
                bot.kick(Chats.LOBBY.toString(), i.getId(), true);
            }
            context.setResult("NewJoin :: kicked new joined from lobby");
            context.setBlockingResult(true);
        }
        return context;
    }
}
