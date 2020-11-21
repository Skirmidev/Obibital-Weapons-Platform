package com.skirmisher.processors;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.ArrayList;
import com.skirmisher.obibital.Context;
import com.skirmisher.data.DBLoader;

public class ImageSpam {
    static int imageCount = 0;

    public static Context spamCheck(Context context, Update update, ObibitalWeaponsPlatform bot){
        System.out.println("ImageSpam:: spamCheck");
        if((update.getMessage().hasVideo() || update.getMessage().hasPhoto() || update.getMessage().hasAnimation() )&& !context.isBlockingResult()){ //treats videos and images the same. would be nice to include gifs, not sure how. should ignore non-compress images?
            imageCount++;
            
            if(imageCount > 3){
                spamDetected(context, update, bot);

            }

        } else {
            imageCount = 0;
        }
        return context;
    }

    private static void spamDetected(Context context, Update update, ObibitalWeaponsPlatform bot){
        DeleteMessage delete = new DeleteMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId());
        try{
            bot.execute(delete);
            context.setResult("ImageSpam:: Image Spam - Deleted");
            context.setBlockingResult(true);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        if(imageCount == 4){
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Please be considerate of other users and avoid spamming the chat with images");
            bot.send(message);
            
            DBLoader.logEvent("IMAGESPAM", update.getMessage().getFrom().getId().toString(), "", "Spammer: " + update.getMessage().getFrom().getUserName());
        }
    }
}