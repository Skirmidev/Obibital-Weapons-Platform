package com.skirmisher.processors;

import org.telegram.telegrambots.meta.api.objects.Update;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.skirmisher.obibital.Context;

import com.skirmisher.data.*;

public class StickerPackBanner {
    static List<String> bannedPacks = new ArrayList<>();

    public static Context checkValidity(Context context, Update update, ObibitalWeaponsPlatform bot){
        System.out.println("StickerPackBanner:: stickerPackBannner");
        if(update.getMessage().hasSticker() && !context.isBlockingResult()){
            String source = update.getMessage().getSticker().getSetName();

            System.out.println("StickerPackBanner:: Bannedpacks:" + bannedPacks.toString());
            System.out.println("StickerPackBanner:: Pack:" + source);
            if(bannedPacks.contains(source)){
                DeleteMessage delete = new DeleteMessage(update.getMessage().getChatId(), update.getMessage().getMessageId());
                System.out.println("StickerPackBanner:: Sticker not permitted");
                context.setResult("StickerPackBanner:: Recieved Sticker from Banned Pack");
                context.setBlockingResult(true);
                try{
                    bot.execute(delete);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        return context;
    }

    public static void reloadBannedPacks(){    
        try {
            List<String> bannedBeans = DBLoader.GetBannedStickers();
            System.out.println("StickerPackBanner:: Loaded: " + bannedBeans);
            bannedPacks = bannedBeans;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void banNewSticker(String packId){ 
        if(bannedPacks.contains(packId)){
            System.out.println("StickerPackBanner:: Pack" + packId + "is already banned - how did this get through?");
        } else {
            DBLoader.banSticker(packId);

            System.out.println("StickerPackBanner:: Pack" + packId + " has been banned");
            reloadBannedPacks();
        }
    }
}