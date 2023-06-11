package com.skirmisher.obibital;

import com.skirmisher.data.DBLoader;
import com.skirmisher.processors.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.skirmisher.data.beans.ModuleValue;
import java.util.List;

public class ModuleControl {
    static boolean stickerSpam = false;
    static boolean commands = false;

    public static void runChatModules(Context context, Update update, ObibitalWeaponsPlatform bot){

        if(stickerSpam) {
            context = StickerSpam.spamCheck(context, update, bot);
        }

        if(commands) {
            context = Commands.command(context, update, bot);
        }
    }

    public static void reloadModules(){
        List<ModuleValue> mods = DBLoader.getModules();
        for(ModuleValue mod : mods){
            switch(mod.getModule()){
                case "stickerSpam":
                    stickerSpam = mod.getEnabled();
                    break;
                case "commands":
                    commands = mod.getEnabled();
                    break;
            }
        }
    }
}
