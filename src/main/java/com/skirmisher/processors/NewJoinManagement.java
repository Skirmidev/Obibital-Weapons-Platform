package com.skirmisher.processors;

import org.telegram.telegrambots.meta.api.objects.Update;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import com.skirmisher.obibital.Utilities;
import com.skirmisher.obibital.Context;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.Instant;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.User;
import com.skirmisher.data.DBLoader;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class NewJoinManagement {

    public static Context manageNewJoin(Context context, Update update, ObibitalWeaponsPlatform bot){
        System.out.println("NewJoinManagement:: manageNewJoin");

        //set 1 day media restrictions
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
            perms.setCanSendMediaMessages(false);

            managePerms.setPermissions(perms);

            long ut1 = Instant.now().getEpochSecond();
            managePerms.setUntilDate(Integer.parseInt(String.valueOf(ut1)) + 86400); //unix time, 1 day restriction
            managePerms.setChatId(update.getMessage().getChatId().toString());

            for(User user : update.getMessage().getNewChatMembers()){
                managePerms.setUserId(user.getId());
                DBLoader.logEvent("NEWJOINRESTRICT", user.getId().toString(), "", "");
                System.out.println("NewJoinRestrictions:: Restricted new user: " + user.getUserName() + " - " + user.getId());
                context.setResult("NewJoinRestrictions:: A new member has joined and been restricted");
                context.setBlockingResult(true);

                //create an admin chat button
                final List<InlineKeyboardButton> keyboard = new ArrayList<InlineKeyboardButton>(3);

                InlineKeyboardButton but = new InlineKeyboardButton();
                but.setText("Ban");
                but.setCallbackData("admin:newjoin:ban");

                InlineKeyboardButton but2 = new InlineKeyboardButton();
                but2.setText("Safe");
                but2.setCallbackData("admin:newjoin:safe");

                keyboard.add(but);
                keyboard.add(but2);
                
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboard));
                
                SendMessage message = new SendMessage();
                message.setChatId(bot.getModChatId().toString());

                String text = "New user has joined: ";
                if(user.getFirstName() != null){
                    text = text + Utilities.makeMarkdownFriendly(user.getFirstName());
                }
                if(user.getLastName() != null){
                    text = text + Utilities.makeMarkdownFriendly(user.getLastName());
                }
                if(user.getUserName() != null){
                    text = text + "\\[ `username: @" + user.getUserName() +"` \\] ";
                }
                text = text + "\\[ `id: " + user.getId() + "` \\]";

                message.setText(text);
                message.setParseMode("MarkdownV2");
                message.setReplyMarkup(inlineKeyboardMarkup);

                try{        
                    bot.execute(message);
                    bot.execute(managePerms);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }


        return context;
    }
}