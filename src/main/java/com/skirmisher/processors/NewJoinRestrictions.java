package com.skirmisher.processors;

import org.telegram.telegrambots.meta.api.objects.Update;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import com.skirmisher.obibital.Context;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.Instant;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.User;


public class NewJoinRestrictions {

    public static Context manageNewJoin(Context context, Update update, ObibitalWeaponsPlatform bot){
        System.out.println("NewJoinRestrictions:: newJoinRestrictions");
        if((!update.getMessage().getNewChatMembers().isEmpty()) && !context.isBlockingResult()){
            RestrictChatMember managePerms = new RestrictChatMember();

            ChatPermissions perms = new ChatPermissions();
            perms.setCanAddWebPagePreviews(false);
            perms.setCanChangeInfo(false);
            perms.setCanInviteUsers(false);
            perms.setCanPinMessages(false);
            perms.setCanSendMessages(true);
            perms.setCanSendOtherMessages(false);
            perms.setCanSendPolls(false);
            perms.setGetCanSendMediaMessages(false);

            managePerms.setPermissions(perms);


            long ut1 = Instant.now().getEpochSecond();
            managePerms.setUntilDate(Integer.parseInt(String.valueOf(ut1)) + 86400); //unix time, 1 day restriction
            managePerms.setChatId(update.getMessage().getChatId());

            for(User user : update.getMessage().getNewChatMembers()){
                managePerms.setUserId(user.getId());
                System.out.println("NewJoinRestrictions:: Restricted new user: " + user.getUserName() + " - " + user.getId());
                context.setResult("NewJoinRestrictions:: A new member has joined and been restricted");
                context.setBlockingResult(true);

                try{        
                    bot.execute(managePerms);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }


        }
        return context;
    }
}