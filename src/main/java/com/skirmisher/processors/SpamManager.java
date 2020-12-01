package com.skirmisher.processors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.skirmisher.obibital.Context;
import com.skirmisher.data.DBLoader;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SpamManager {
    static int gifCount = 0;
    static int videoCount = 0;
    static int photoCount = 0;
    static int stickerCount = 0;

    public static Context spamCheck(Context context, Update update, ObibitalWeaponsPlatform bot){
        System.out.println("SpamManager:: spamCheck");
        if(!context.isBlockingResult()){
            if(update.getMessage().hasVideo()){
                videoCount++;
            } else if (update.getMessage().hasPhoto()){
                photoCount++;
            } else if (update.getMessage().hasAnimation()){
                gifCount++;
            } else if (update.getMessage().hasSticker()){
                stickerCount++;
            }else if (update.getMessage().hasText()){
                gifCount = 0;
                videoCount = 0;
                photoCount = 0;
                stickerCount = 0;
            }



        }
        return context;
    }

    private static void spamDetected(Context context, Update update, ObibitalWeaponsPlatform bot){
        // DeleteMessage delete = new DeleteMessage(update.getMessage().getChatId(), update.getMessage().getMessageId());
        // try{
        //     bot.execute(delete);
        //     context.setResult("MediaSpam:: Media Spam - Deleted");
        //     context.setBlockingResult(true);
        // } catch (TelegramApiException e) {
        //     e.printStackTrace();
        // }

        // if(mediaCount == 4){
        //     SendMessage message = new SendMessage()
        //     .setChatId(update.getMessage().getChatId())
        //     .setText("Please be considerate of other users and avoid spamming the chat with images, videos, gifs or stickers");
        //     bot.send(message);
            
        //     DBLoader.logEvent("MEDIASPAM", update.getMessage().getFrom().getId().toString(), "", "Spammer: " + update.getMessage().getFrom().getUserName());
        // }
    }
}