package com.skirmisher.obibital;

import com.skirmisher.processors.*;
import com.skirmisher.data.*;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ModuleControl {
    static boolean stickerSpam = false;
    static boolean commands = false;
    static boolean stickerPackBanner = false;
    static boolean newJoinRestrictions = false;

    public static void runChatModules(Context context, Update update, ObibitalWeaponsPlatform bot){
        
        if(stickerSpam) {
            StickerSpam.spamCheck(context, update, bot);
        }

        if(commands) {
            Commands.command(context, update, bot);
        }

        if(stickerPackBanner) {
            StickerPackBanner.checkValidity(context, update, bot);
        }

        if(newJoinRestrictions) {
            NewJoinRestrictions.manageNewJoin(context, update, bot);
        }
    }

    public static void reloadChatModules(){
        //TODO: make an active commands bean

    }
}