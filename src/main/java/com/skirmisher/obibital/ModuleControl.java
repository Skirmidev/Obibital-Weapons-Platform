package com.skirmisher.obibital;

import com.skirmisher.data.DBLoader;
import com.skirmisher.processors.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.skirmisher.data.beans.ModuleBean;
import java.util.List;

public class ModuleControl {
    static boolean stickerSpam = true;
    static boolean commands = true;
    static boolean stickerPackBanner = true;
    static boolean newJoinRestrictions = true;

    public static void runChatModules(Context context, Update update, ObibitalWeaponsPlatform bot){
        

        if(stickerPackBanner) {
            StickerPackBanner.checkValidity(context, update, bot);
        }

        if(stickerSpam) {
            StickerSpam.spamCheck(context, update, bot);
        }

        if(commands) {
            Commands.command(context, update, bot);
        }

        if(newJoinRestrictions) {
            NewJoinRestrictions.manageNewJoin(context, update, bot);
        }
    }

    public static void reloadModules(){
        List<ModuleBean> mods = DBLoader.getModules();
        for(ModuleBean mod : mods){
            switch(mod.getModule()){
                case "stickerSpam":
                    stickerSpam = mod.getEnabled();
                    break;
                case "commands":
                    commands = mod.getEnabled();
                    break;
                case "stickerPackBanner":
                    stickerPackBanner = mod.getEnabled();
                    break;
                case "newJoinRestrictions":
                    newJoinRestrictions = mod.getEnabled();
                    break;
            }
        }
    }
}