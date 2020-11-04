package com.skirmisher.obibital;

import com.skirmisher.data.DBLoader;
import com.skirmisher.data.beans.TimerBean;
import java.util.List;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import java.time.LocalDateTime;

public class TimerPoller implements Runnable{
    ObibitalWeaponsPlatform bot;    

    public TimerPoller(ObibitalWeaponsPlatform botParam){
        bot=botParam;
    }

    public void run(){
        try{
            //check for expired timers
            List<TimerBean> expiredTimers = DBLoader.getExpiredTimers();
            System.out.println(LocalDateTime.now().toString() + "Timer Poller:: Found " + expiredTimers.size() + " expired timers");

            //for each expired timer, see what we're meant to do with it
            for(TimerBean tim : expiredTimers){
                String splitArgs[] = tim.getArgs().split(" ");
                switch( tim.getAction() ){
                    case "UNBAN":
                        //args - userid
                        //unban user
                        unBan(splitArgs);
                        break;
                    case "PERMISSIONS":
                        //args - userid
                        permissions(splitArgs);
                        break;
                    case "NOTIFY":
                        //args - userid - message  
                        sendNotif(splitArgs);
                    case "WEEKLYREPORT":
                        //args - 
                        weeklyReport();
                        break;
                    default:
                        System.out.println("TimerPoller:: Unrecognised Timer: " + tim.toString());
                        break;
                }

                DBLoader.removeTimer(tim.getTimerId());
            }
        } catch (RuntimeException e) {
            //catch necessary for poller to continue
            //errors expected in some cases of failure responses due to illegal actions
            //e.printStackTrace();
        }
    }
    
    ////////////////////
    // UNBAN - userId //
    ////////////////////
    public void unBan(String[] args){
        UnbanChatMember unban = new UnbanChatMember(bot.getGroupChatId(), Integer.parseInt(args[0]));
        boolean success = false;
        try{
            success = bot.execute(unban);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }

        
        if(success){
            System.out.println("User Unbanned Succesfully");

            //send message to user, saying they have been unbanned      
            SendMessage userMessage = new SendMessage().setChatId(Long.parseLong(args[0]));
            userMessage.setText("You have been unbanned from " + DBLoader.configValue("networkName") + " and may now rejoin: " + DBLoader.configValue("groupLink")); //TODO: make this generic  
            bot.send(userMessage);
    
            //send message to modchat, say userid has been unbanned and informed
            SendMessage modMessage = new SendMessage().setChatId(bot.getModChatId());
            modMessage.setText("User: " + args[0] + " has been unbanned from " + "CHAT NAME and has been informed as such"); //TODO: make this generic, get username from id
            bot.send(modMessage);

            DBLoader.logEvent("UNBAN", "TIMED_EVENT", args[0], "");
        } else {
            //send message to modchat, user unban attempted but failed
            SendMessage modMessage = new SendMessage().setChatId(bot.getModChatId());
            modMessage.setText("User: " + args[0] + " scheduled unban was unsuccesful. Please consult an administrator and consider a manual unbanning");
            bot.send(modMessage);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PERMISSIONS - userId - webPagePreviews - changeInfo - inviteUsers - pinMessages - sendMessages - sendOtherMessages - sendPolls - sendMediaMessages //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void permissions(String [] args){ //this timer is unlikely to be required at any time, given permissions can already be set to expire at a certain time
        RestrictChatMember managePerms = new RestrictChatMember();
        managePerms.setUserId(Integer.parseInt(args[0]));
        managePerms.setChatId(bot.getGroupChatId());
        
        ChatPermissions perms = new ChatPermissions();
        perms.setCanAddWebPagePreviews(Boolean.parseBoolean(args[1]));
        perms.setCanChangeInfo(Boolean.parseBoolean(args[2]));
        perms.setCanInviteUsers(Boolean.parseBoolean(args[3]));
        perms.setCanPinMessages(Boolean.parseBoolean(args[4]));
        perms.setCanSendMessages(Boolean.parseBoolean(args[5]));
        perms.setCanSendOtherMessages(Boolean.parseBoolean(args[6]));
        perms.setCanSendPolls(Boolean.parseBoolean(args[7]));
        perms.setGetCanSendMediaMessages(Boolean.parseBoolean(args[8]));

        managePerms.setPermissions(perms);

        try{        
            bot.execute(managePerms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //////////////////
    // WEEKLYREPORT //
    //////////////////
    public void weeklyReport(){
        //TODO: get details from a logging/statistics system

        //create a new timer set for a week from now
        
        SendMessage modMessage = new SendMessage().setChatId(bot.getModChatId());
        modMessage.setText("THIS IS THE WEEKLY REPORT");
        bot.send(modMessage);
    }

    //////////////////////////////////
    // sendNotif - userId - message //
    //////////////////////////////////
    public void sendNotif(String [] args){
        SendMessage message = new SendMessage().setChatId(args[0]);
        String info = "";
        for(int i = 1; i < args.length; i++){
            info = info + args[i];
        }
        message.setText(info);
        bot.send(message);
    }
}