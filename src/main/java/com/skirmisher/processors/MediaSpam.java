package com.skirmisher.processors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.skirmisher.obibital.Context;
import com.skirmisher.data.DBLoader;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MediaSpam {
    static int mediaCount = 0;

    public static Context spamCheck(Context context, Update update, ObibitalWeaponsPlatform bot){
        System.out.println("MediaSpam:: spamCheck");
        if((update.getMessage().hasVideo() || update.getMessage().hasPhoto() || update.getMessage().hasAnimation() || update.getMessage().hasSticker() ) && !context.isBlockingResult()){ //treats videos and images the same. would be nice to include gifs, not sure how. should ignore non-compress images?
            mediaCount++;
            
            if(mediaCount > 5){
                spamDetected(context, update, bot);

            }

        } else {
            mediaCount = 0;
        }
        return context;
    }

    private static void spamDetected(Context context, Update update, ObibitalWeaponsPlatform bot){
        DeleteMessage delete = new DeleteMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId());
        try{
            bot.execute(delete);
            context.setResult("MediaSpam:: Media Spam - Deleted");
            context.setBlockingResult(true);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        if(mediaCount == 4){
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Please be considerate of other users and avoid spamming the chat with images, videos, gifs or stickers");
            bot.send(message);
            
            DBLoader.logEvent("MEDIASPAM", update.getMessage().getFrom().getId(), 0, "Spammer: " + update.getMessage().getFrom().getUserName());
        }
    }
}