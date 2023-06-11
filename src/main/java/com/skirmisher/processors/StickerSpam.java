package com.skirmisher.processors;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.ArrayList;
import java.util.LinkedList;
import com.skirmisher.obibital.Context;
import java.time.LocalDateTime;

public class StickerSpam {
    static int stickerCount = 0;
    static Boolean hasSentMessage = false;
    static ArrayList<Update> lastStickers = new ArrayList<>();
    static LinkedList<Update> last6Messages = new LinkedList<>();

    public static Context spamCheck(Context context, Update update, ObibitalWeaponsPlatform bot){
        //System.out.println("StickerSpam:: spamCheck");
        if(!context.isBlockingResult()){

            if(last6Messages.size() == 6){
                last6Messages.remove();
            }
            last6Messages.add(update);

            stickerCount = 0;
            for(Update u: last6Messages){
                if(u.getMessage().hasSticker()){
                    stickerCount++;
                }
                //reset count to 0 when a new member joins
                if(update.getMessage().getNewChatMembers().size() > 0){
                    stickerCount=0;
                }
            }

            if(stickerCount > 3 && update.getMessage().hasSticker()){
                spamDetected(context, update, bot);
                System.out.println("StickerSpam:: spam detected at " + LocalDateTime.now().toString());
            } else if (stickerCount < 3){
                hasSentMessage = false;
            }
        }
        return context;
    }

    private static void spamDetected(Context context, Update update, ObibitalWeaponsPlatform bot){
        DeleteMessage delete = new DeleteMessage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId());
        try{
            bot.execute(delete);
            context.setResult("StickerSpam:: Sticker Spam - Deleted");
            context.setBlockingResult(true);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        if(stickerCount == 4 && !hasSentMessage){
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Please be considerate of other users and avoid spamming the chat with stickers");
            hasSentMessage = true;
            bot.send(message);
            
            //DBLoader.logEvent("STICKERSPAM", update.getMessage().getFrom().getId(), 0, "Spammer: " + update.getMessage().getFrom().getUserName());
            //TODO: reimplement when user db is working
        }

        last6Messages.remove(update);
    }
}