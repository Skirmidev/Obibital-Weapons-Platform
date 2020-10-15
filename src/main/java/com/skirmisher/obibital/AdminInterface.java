package com.skirmisher.obibital;

import com.skirmisher.adminprocessors.*;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdminInterface {
    public static void run(Context context, Update update, ObibitalWeaponsPlatform bot){
        AdminCommands.command(context, update, bot); //is this level of abstraction necessary? may be useful later, may not be. allows for admin context swapping perhaps?
    }
}