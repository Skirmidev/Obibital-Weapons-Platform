package com.skirmisher.processors;

import org.telegram.telegrambots.meta.api.objects.Update;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import com.skirmisher.data.DBLoader;
import com.skirmisher.obibital.Context;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.Instant;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.User;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class NightTimeRestrictor {
    public static Context manageNewJoin(Context context, Update update, ObibitalWeaponsPlatform bot){
        System.out.println("NightTimeRestrictor:: manageNewJoin");
        if((!update.getMessage().getNewChatMembers().isEmpty()) && !context.isBlockingResult()){
            System.out.println(update.getMessage().getNewChatMembers());

            LocalDateTime currentTime = LocalDateTime.now();
            System.out.println("current hour: " + currentTime.getHour());

            if(currentTime.getHour() < 9){

                String users = "";

                RestrictChatMember managePerms = new RestrictChatMember();

                ChatPermissions perms = new ChatPermissions();
                perms.setCanAddWebPagePreviews(false);
                perms.setCanChangeInfo(false);
                perms.setCanInviteUsers(false);
                perms.setCanPinMessages(false);
                perms.setCanSendMessages(false);
                perms.setCanSendOtherMessages(false);
                perms.setCanSendPolls(false);
                perms.setGetCanSendMediaMessages(false);

                managePerms.setPermissions(perms);

                LocalDateTime nextMorning = currentTime.toLocalDate().atTime(9, 0);

                if((nextMorning.toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now())) - 60l) < ( LocalDateTime.now().toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now())))){
                    //1 minute til 9 am, leave them be sure
                    //this ensures they are never infinitely restricte
                } else {
                    managePerms.setUntilDate(Math.toIntExact(nextMorning.toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()))));
                    managePerms.setChatId(update.getMessage().getChatId());

                    for(User user : update.getMessage().getNewChatMembers()){
                        managePerms.setUserId(user.getId());
                        System.out.println("NewJoinRestrictions:: Restricted new user: " + user.getUserName() + " - " + user.getId());
                        context.setResult("NewJoinRestrictions:: A new member has joined and been restricted");
                        context.setBlockingResult(true);
                        users = users + "@" + user.getUserName() + " ";

                        DBLoader.logEvent("NIGHT_JOIN", "", user.getUserName(), "");

                        try{        
                            bot.execute(managePerms);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }

                    
                    SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
                    message.setText("Welcome to the chat! Due to security concerns, you are unable to send messages until 9am. We apologise for the inconvenience.");
                    message.setReplyToMessageId(update.getMessage().getMessageId());
                    bot.send(message);

                    //DBLoader.addTimer(action, args, time);
                    DBLoader.addTimer("NOTIFY", bot.getGroupChatId() + " " + users + "- daytime is here, you are no longer muted. ", nextMorning);
                }
            }

        }
        return context;
    }
}