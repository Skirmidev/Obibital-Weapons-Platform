package com.skirmisher.obibital;

import com.skirmisher.data.DBLoader;
import com.skirmisher.processors.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.skirmisher.data.beans.ModuleBean;
import java.util.List;

public class ModuleControl {
    static boolean stickerSpam = false;
    static boolean commands = false;
    static boolean stickerPackBanner = false;
    static boolean newJoinRestrictions = false;
    static boolean nightTimeRestrictor = false;
    static boolean imageSpam = false;

    public static void runChatModules(Context context, Update update, ObibitalWeaponsPlatform bot){
        

        if(stickerPackBanner) {
            StickerPackBanner.checkValidity(context, update, bot);
        }

        if(stickerSpam) {
            StickerSpam.spamCheck(context, update, bot);
        }

        if(imageSpam) {
            ImageSpam.spamCheck(context, update, bot);
        }

        if(commands) {
            Commands.command(context, update, bot);
        }

        if(newJoinRestrictions) {
            NewJoinRestrictions.manageNewJoin(context, update, bot);
        }

    
        if(nightTimeRestrictor) {
            NightTimeRestrictor.manageNewJoin(context, update, bot);
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
                case "nightTimeRestrictor":
                    nightTimeRestrictor = mod.getEnabled();
                    break;
                case "imageSpam":
                    imageSpam = mod.getEnabled();
                    break;
            }
        }
    }
}