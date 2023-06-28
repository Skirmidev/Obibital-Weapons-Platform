package com.skirmisher.processors;

import com.skirmisher.obibital.Context;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import com.skirmisher.obibital.Utilities;
import com.skirmisher.obibital.Chats;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.Update;

public class GenerateLink {
    static char prefix = '/';

    public static Context nsfwLink(Context context, Update update, ObibitalWeaponsPlatform bot){
        if(update.getMessage().hasText() && !context.isBlockingResult()){
            if(update.getMessage().getText().charAt(0) == prefix){
                switch(update.getMessage().getText()){
                    case "/nsfw":
                        ChatInviteLink nsfwLink = bot.generateInvite(Chats.NSFW.toString());

                        String nsfw = Utilities.makeMarkdownFriendly(nsfwLink.getInviteLink());

                        SendMessage message = new SendMessage();
                        message.setParseMode("MarkdownV2");
                        message.setChatId(update.getMessage().getChatId().toString());
                        message.setText("*NSFW Link:* " + nsfw);
                        bot.send(message);

                        context.setResult("GenerateLink :: created invite link for NSFW");
                        context.setBlockingResult(true);
                        break;
                    default:
                        break;
                }
            }
        }
        return context;
    }
}
