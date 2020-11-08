package com.skirmisher.processors;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.util.ArrayList;
import com.skirmisher.obibital.Context;
import com.skirmisher.data.DBLoader;

public class StickerSpam {
    static int stickerCount = 0;
    static ArrayList<Update> lastStickers = new ArrayList<>();

    public static Context spamCheck(Context context, Update update, ObibitalWeaponsPlatform bot){
        System.out.println("StickerSpam:: spamCheck");
        if(update.getMessage().hasSticker() && !context.isBlockingResult()){
            stickerCount++;
            lastStickers.add(update);
            
            if(stickerCount > 2){
                checkIfPackSpam(context, update, bot);
            
                if(stickerCount > 3){
                    spamDetected(context, update, bot);
                }
            }

            //handle the same sticker sent multiple times

        } else {
            stickerCount = 0;
            lastStickers = new ArrayList<>();
        }
        return context;
    }

    private static void checkIfPackSpam(Context context, Update update, ObibitalWeaponsPlatform bot){
        //check to see if the exact same sticker has been sent 3 times. If so, ban the pack.
        if( lastStickers.get(stickerCount-1).getMessage().getSticker().getFileUniqueId().equals(    lastStickers.get(stickerCount-2).getMessage().getSticker().getFileUniqueId()    ) &&
        lastStickers.get(stickerCount-2).getMessage().getSticker().getFileUniqueId().equals(    lastStickers.get(stickerCount-3).getMessage().getSticker().getFileUniqueId()    )
        ) {
        context.setResult("StickerSpam:: Duplicate Sticker Spam - Banning Pack");
        context.setBlockingResult(true);
        //same sticker sent 3 times. delete the messages and ban the pack
        StickerPackBanner.banNewSticker(lastStickers.get(stickerCount-1).getMessage().getSticker().getSetName());

        ArrayList<DeleteMessage> toBeDeleted = new ArrayList<>();
        toBeDeleted.add(new DeleteMessage(lastStickers.get(stickerCount-1).getMessage().getChatId(), lastStickers.get(stickerCount-1).getMessage().getMessageId()));
        toBeDeleted.add(new DeleteMessage(lastStickers.get(stickerCount-2).getMessage().getChatId(), lastStickers.get(stickerCount-2).getMessage().getMessageId()));
        toBeDeleted.add(new DeleteMessage(lastStickers.get(stickerCount-3).getMessage().getChatId(), lastStickers.get(stickerCount-3).getMessage().getMessageId()));
        for(DeleteMessage del : toBeDeleted){
            try{
                bot.execute(del);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        SendMessage message = new SendMessage()
        .setChatId(update.getMessage().getChatId())
        .setText("Repeated use has caused this sticker pack to be banned. Keep it Clean.");
        bot.send(message);

        lastStickers.remove(stickerCount-1);
        lastStickers.remove(stickerCount-2);
        lastStickers.remove(stickerCount-3);
        stickerCount = stickerCount - 3;
        } 
    }

    private static void spamDetected(Context context, Update update, ObibitalWeaponsPlatform bot){
        DeleteMessage delete = new DeleteMessage(update.getMessage().getChatId(), update.getMessage().getMessageId());
        try{
            bot.execute(delete);
            context.setResult("StickerSpam:: Sticker Spam - Deleted");
            context.setBlockingResult(true);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        if(stickerCount == 4){
            SendMessage message = new SendMessage()
            .setChatId(update.getMessage().getChatId())
            .setText("Please be considerate of other users and avoid spamming the chat with images or stickers");
            bot.send(message);
            
            DBLoader.logEvent("STICKERSPAM", update.getMessage().getFrom().getId().toString(), "", "Spammer: " + update.getMessage().getFrom().getUserName() + " ID: " + update.getMessage().getFrom().getUserName());
        }
    }
}