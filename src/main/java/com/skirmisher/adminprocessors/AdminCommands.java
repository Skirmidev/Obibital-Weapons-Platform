package com.skirmisher.adminprocessors;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import com.skirmisher.processors.StickerPackBanner;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.skirmisher.data.DBLoader;
import com.skirmisher.data.beans.ModuleBean;
import com.skirmisher.data.beans.StatisticBean;
import com.skirmisher.obibital.Context;
import com.skirmisher.obibital.ModuleControl;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.time.*;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Chat;

public class AdminCommands {
    static char prefix = '/';

    ////////////////////
    // Regex Patterns //
    ////////////////////
    static Pattern p_userId = Pattern.compile("[0-9]+");
    static Pattern p_userName = Pattern.compile("@([0-9a-zA-Z_]){5,32}");
    static Pattern p_module = Pattern.compile("[a-zA-Z]+");
    static Pattern p_stickerPack = Pattern.compile("[a-zA-Z]+");

    static Matcher m_userId;
    static Matcher m_userName;
    static Matcher m_module;
    static Matcher m_stickerPack;

    ///////////////////////
    // Primary Processor //
    ///////////////////////
    public static Context command(Context context, Update update, ObibitalWeaponsPlatform bot){
        System.out.println("AdminCommands:: command");
        if(update.getMessage().hasText() && !context.isBlockingResult()){
            if(update.getMessage().getText().charAt(0) == prefix){
                

                //split the inputs
                String[] args = update.getMessage().getText().split(" ");

                args[0] = args[0].toLowerCase();

                //this is a command. Switch to find out what it does.
                switch(args[0]){ //add something here to split the string apart based on gaps
                    case "/help":
                        help(update, bot);
                        context.setResult("AdminCommands: Help command");
                        context.setBlockingResult(true);
                        break;
                    case "/addadmin":
                        addAdmin(update, bot, args);
                        context.setResult("AdminCommands: addAdmin command");
                        context.setBlockingResult(true);
                        break;
                    case "/removeadmin":
                        removeAdmin(update, bot, args);
                        context.setResult("AdminCommands: removeAdmin command");
                        context.setBlockingResult(true);
                        break;
                    case "/listadmins":
                        listAdmins(update, bot);
                        context.setResult("AdminCommands: listAdmins command");
                        context.setBlockingResult(true);
                        break;
                    case "/disablemodule":
                        disableModule(update, bot, args);
                        context.setResult("AdminCommands: disableModule command");
                        context.setBlockingResult(true);
                        break;
                    case "/enablemodule":
                        enableModule(update, bot, args);
                        context.setResult("AdminCommands: enableModule command");
                        context.setBlockingResult(true);
                        break;
                    case "/getmodules":
                        getModules(update, bot);
                        context.setResult("AdminCommands: getModules command");
                        context.setBlockingResult(true);
                            break;
                    case "/modulestatus":
                        moduleStatus(update, bot, args);
                        context.setResult("AdminCommands: getModules command");
                        context.setBlockingResult(true);
                        break;
                    case "/addmodule":
                        addModule(update, bot, args);
                        context.setResult("AdminCommands: addModule command");
                        context.setBlockingResult(true);
                        break;
                    case "/removemodule":
                        removeModule(update, bot, args);
                        context.setResult("AdminCommands: removeModule command");
                        context.setBlockingResult(true);
                        break;
                    case "/banpack":
                        banPack(update, bot, args);
                        context.setResult("AdminCommands: banPack command");
                        context.setBlockingResult(true);
                        break;
                    case "/unbanpack":
                        unbanPack(update, bot, args);
                        context.setResult("AdminCommands: unbanPack command");
                        context.setBlockingResult(true);
                        break;
                    case "/getbannedpacks":
                        getBannedPacks(update, bot);
                        context.setResult("AdminCommands: getBnnnedPacks command");
                        context.setBlockingResult(true);
                        break;
                    case "/createunbantimer":
                        createUnbanTimer(update, bot, args);
                        context.setResult("AdminCommands: createUnbanTimer command");
                        context.setBlockingResult(true);
                        break;
                    case "/getlogs":
                        getLogs(update, bot);
                        context.setResult("AdminCommands: getLogs command");
                        context.setBlockingResult(true);
                        break;
                    case "/getalllogs":
                        getAllLogs(update, bot);
                        context.setResult("AdminCommands: getAllLogs command");
                        context.setBlockingResult(true);
                        break;
                    case "/getlastweeklogs":
                        getAllLogs(update, bot);
                        context.setResult("AdminCommands: getLastWeekLogs command");
                        context.setBlockingResult(true);
                        break;
                    case "/weekreport":
                        getAllLogs(update, bot);
                        context.setResult("AdminCommands: weekReport command");
                        context.setBlockingResult(true);
                        break;
                    case "/getlogsbyuser":
                        getLogsByUser(update, bot, args);
                        context.setResult("AdminCommands: weekReport command");
                        context.setBlockingResult(true);
                        break;
                    case "/warn":
                        warn(update, bot, args);
                        context.setResult("AdminCommands: warn command");
                        context.setBlockingResult(true);
                        break;
                    case "/ban":
                        ban(update, bot, args);
                        context.setResult("AdminCommands: ban command");
                        context.setBlockingResult(true);
                        break;
                    case "/usernamefromid":
                        usernameFromId(update, bot, args);
                        context.setResult("AdminCommands: usernameFromId command");
                        context.setBlockingResult(true);
                        break;
                    case "/idfromusername":
                        idFromUsername(update, bot, args);
                        context.setResult("AdminCommands: idFromUsername command");
                        context.setBlockingResult(true);
                        break;
                    case "/getid":
                        getId(update, bot);
                        context.setResult("AdminCommands: getid command");
                        context.setBlockingResult(true);
                        break;
                    default:
                        unrecognisedCommand(update, bot);
                        break;
                }
            }
        }
        return context;
    }

    ///////////
    // /help //
    ///////////
    public static void help(Update update, ObibitalWeaponsPlatform bot){
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(   "OBIBITAL WEAPONS PLATFORM V-0.0.1"  + "\n" +
                            "The following admin command are available (not case sensitive)"  + "\n" +
                            "/help"  + "\n" +
                            "/addAdmin [userId]"  + "\n" +
                            "/removeAdmin [userId]"  + "\n" +
                            "/listAdmins"  + "\n" +
                            "/disableModule [moduleName]"  + "\n" +
                            "/enableModule [moduleName]"  + "\n" +
                            "/getModules"  + "\n" +
                            "/moduleStatus"  + "\n" +
                            "/banPack [packId]"  + "\n" +
                            "/unbanPack [packId]"  + "\n" +
                            "/getBannedPacks"  + "\n" +
                            "/getLogs"  + "\n" +
                            "/getAllLogs"  + "\n" +
                            "/getLastWeekLogs"  + "\n" +
                            "/weekReport"  + "\n" +
                            "/getLogsByUser [userId]"  + "\n" +
                            "/warn [userId] [reason]"  + "\n" +
                            "/ban [userId] [durationVal] [durationField] [reason]"  + "\n" +
                            "" + "\n" + 
                            "**The following commands are for emergency use only**"  + "\n" +
                            "/addModule [moduleName]"  + "\n" +
                            "/removeModule [moduleName]"  + "\n" +
                            "/createUnbanTimer [userId] [durationVal] [durationField]"  + "\n" +
                            "/usernamefromid [userId]"  + "\n" +
                            "");

        bot.send(message);
    }

    //////////////////////////
    // /unrecognisedCommand //
    //////////////////////////
    public static void unrecognisedCommand(Update update, ObibitalWeaponsPlatform bot){
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("Your command was not recognised.");

        bot.send(message);
    }

    ///////////////////////
    // /addAdmin [admin] //
    ///////////////////////
    public static void addAdmin(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(bot.getModChatId());

        if(args.length > 1){
            // args supplied, check if valid
            m_userId = p_userId.matcher(args[1]);
            m_userName = p_userName.matcher(args[1]);
            if (m_userId.find() ) {
                // add the admin by this userid
                if(DBLoader.addAdmin(Long.parseLong(args[1]))){
                    bot.reloadConfig();
                    message.setText(args[1] + " added to admin list by " + update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName());

                    DBLoader.logEvent("ADD_ADMIN", update.getMessage().getFrom().getId().toString(), args[1], "");
                } else {
                    message.setText(args[1] + " is already in the admin list");
                }

                bot.send(message);
            } else if ( m_userName.find() ) {
                //find the userid and add the admin
                //TODO: add @username functionality
                message.setChatId(update.getMessage().getChatId())
                .setText("@usernames are not supported at this time, please use userId");

                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. please use the userid as input");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/addAdmin userid`");

            bot.send(message);
        }
    }

    //////////////////////////
    // /removeAdmin [admin] //
    //////////////////////////
    public static void removeAdmin(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(bot.getModChatId());

        if(args.length > 1){
            // args supplied, check if valid
            m_userId = p_userId.matcher(args[1]);
            m_userName = p_userName.matcher(args[1]);
            if (m_userId.find() ) {
                // add the admin by this userid
                if (DBLoader.removeAdmin(Long.parseLong(args[1]))){
                    bot.reloadConfig();
                    message.setText(args[1] + " removed from admin list by " + update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName());

                    DBLoader.logEvent("REMOVE_ADMIN", update.getMessage().getFrom().getId().toString(), args[1], "");
                } else {
                    message.setText(args[1] + " is not in the admin list");
                }

                bot.send(message);
            } else if ( m_userName.find() ) {
                //find the userid and remove from admin
                //TODO: add @username functionality
                message.setChatId(update.getMessage().getChatId())
                .setText("@usernames are not supported at this time, please use userId");

                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. please use the userid as input");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/removeAdmin userid`");

            bot.send(message);
        }
    }

    /////////////////
    // /listAdmins //
    /////////////////
    public static void listAdmins(Update update, ObibitalWeaponsPlatform bot){
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
        String response = "Current Admin List:" + "\n";
        List<Long> admins = DBLoader.getAdmins();

        for(Long admin : admins){
            response = response + admin.toString() + "\n";
        }

        message.setText(response);
        bot.send(message);
    }

    ////////////////////////////
    // /enableModule [module] //
    ////////////////////////////
    public static void enableModule(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(bot.getModChatId());

        if(args.length > 1){
            // args supplied, check if valid
            m_module = p_module.matcher(args[1]);
            if (m_module.find() ) {
                // enable the module by this name
                String result = DBLoader.enableModule(args[1]);
                if(result.equals("success")){
                    message.setText(args[1] + " has been enabled by " + update.getMessage().getFrom().getFirstName() + update.getMessage().getFrom().getLastName());

                    DBLoader.logEvent("ENABLE_MODULE", update.getMessage().getFrom().getId().toString(), "", args[1]);
                } else {
                    message.setChatId(update.getMessage().getChatId());
                    message.setText(result);
                }
                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. module names are comprised of only letters");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/addModule [modulename]`");

            bot.send(message);
        }
    }

    /////////////////////////////
    // /disableModule [module] //
    /////////////////////////////
    public static void disableModule(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(bot.getModChatId());

        if(args.length > 1){
            // args supplied, check if valid
            m_module = p_module.matcher(args[1]);
            if (m_module.find() ) {
                //disable the module by this name
                String result = DBLoader.disableModule(args[1]);
                if(result.equals("success")){
                    message.setText(args[1] + " has been disabled by " + update.getMessage().getFrom().getFirstName() + update.getMessage().getFrom().getLastName());

                    DBLoader.logEvent("DISABLE_MODULE", update.getMessage().getFrom().getId().toString(), "", args[1]);
                } else {
                    message.setChatId(update.getMessage().getChatId());
                    message.setText(result);
                }
                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. module names are comprised of only letters");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/addModule [modulename]`");

            bot.send(message);
        }
    }

    /////////////////
    // /getModules //
    /////////////////
    public static void getModules(Update update, ObibitalWeaponsPlatform bot){
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());

        List<ModuleBean> modules = DBLoader.getModules();
        
        String response = "The following modules are loaded: \n";

        for(ModuleBean mod : modules){
            response = response + mod.getModule() + " - " + mod.getEnabled() + "\n";
        }

        message.setText(response);
        bot.send(message);
    }

    ////////////////////////////////
    // /moduleStatus [moduleName] //
    ////////////////////////////////
    public static void moduleStatus(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());

        if(args.length > 1){
            // args supplied, check if valid
            m_module = p_module.matcher(args[1]);
            if (m_module.find() ) {
                // add the module by this name
                if(DBLoader.getModuleStatus(args[1])){
                    message.setText(args[1] + " is active");
                } else {
                    message.setText(args[1] + " is inactive");
                }
                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. module names are comprised of only letters");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/addModule [modulename]`");

            bot.send(message);
        }
    }

    //////////////////////////
    // /addModule [module] ///
    //////////////////////////
    public static void addModule(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
        message.setText("WARNING: This is an emergency command and should not be required.");
        bot.send(message);

        message.setChatId(bot.getModChatId());

        if(args.length > 1){
            // args supplied, check if valid
            m_module = p_module.matcher(args[1]);
            if (m_module.find() ) {
                // add the module by this name
                if (DBLoader.addModule(args[1])){
                    ModuleControl.reloadModules();
                    message.setText(args[1] + " added to the module list by " + update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName());

                    DBLoader.logEvent("ADD_MODULE", update.getMessage().getFrom().getId().toString(), "", args[1]);
                } else {
                    message.setText(args[1] + " already present in the modules list");
                }

                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. module names are comprised of only letters");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/addModule [modulename]`");

            bot.send(message);
        }
    }

    ////////////////////////////
    // /removeModule [module] //
    ////////////////////////////
    public static void removeModule(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
        message.setText("WARNING: This is an emergency command and should not be required.");
        bot.send(message);

        message.setChatId(bot.getModChatId());

        if(args.length > 1){
            // args supplied, check if valid
            m_module = p_module.matcher(args[1]);
            if (m_module.find() ) {
                // add the module by this name
                if (DBLoader.removeModule(args[1])){
                    ModuleControl.reloadModules();
                    message.setText(args[1] + " removed from the module list by " + update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName());

                    DBLoader.logEvent("REMOVE_MODULE", update.getMessage().getFrom().getId().toString(), "", args[1]);
                } else {
                    message.setText(args[1] + " not present in the modules list");
                }

                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. module names are comprised of only letters");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/removeModule [modulename]`");

            bot.send(message);
        }
    }

    //////////////////////////
    // /banPack [packname] ///
    //////////////////////////
    public static void banPack(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(bot.getModChatId());

        if(args.length > 1){
            // args supplied, check if valid
            m_stickerPack = p_stickerPack.matcher(args[1]);
            if (m_stickerPack.find() ) {
                // add the module by this name
                if (DBLoader.banSticker(args[1])){
                    StickerPackBanner.reloadBannedPacks();
                    message.setText(args[1] + " added to the banned stickers list by " + update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName());

                    DBLoader.logEvent("BAN_STICKERPACK", update.getMessage().getFrom().getId().toString(), "", args[1]);
                } else {
                    message.setText(args[1] + " already present in the banned stickers list");
                }

                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. stickerpack names are comprised of only letters (may be incorrect)");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/banPack [packName]`");

            bot.send(message);
        }
    }

    ////////////////////////////
    // /unbanPack [packname] //
    ////////////////////////////
    public static void unbanPack(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(bot.getModChatId());

        if(args.length > 1){
            // args supplied, check if valid
            m_stickerPack = p_stickerPack.matcher(args[1]);
            if (m_stickerPack.find() ) {
                // add the module by this name
                if (DBLoader.unbanSticker(args[1])){
                    StickerPackBanner.reloadBannedPacks();
                    message.setText(args[1] + " removed from the banned stickers list by " + update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName());

                    DBLoader.logEvent("UNBAN_STICKERPACK", update.getMessage().getFrom().getId().toString(), "", args[1]);
                } else {
                    message.setText(args[1] + " not present in the banned stickers list");
                }

                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. stickerpack are comprised of only letters (may be incorrect)");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/unbanPack [packname]`");

            bot.send(message);
        }
    }

    /////////////////////
    // /getBannedPacks //
    /////////////////////
    public static void getBannedPacks(Update update, ObibitalWeaponsPlatform bot){
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());

        List<String> bannedPacks = DBLoader.GetBannedStickers();
        
        String response = "The following stickerpacks have been banned: \n";

        for(String pack : bannedPacks){
            response = response + pack + "\n";
        }

        message.setText(response);
        bot.send(message);
    }

    //////////////////////////////////////////////////////////////
    // /createUnbanTimer [userId] [durationVal] [durationField] //
    //////////////////////////////////////////////////////////////
    public static void createUnbanTimer(Update update, ObibitalWeaponsPlatform bot, String[] args){

        if(args.length != 4){
            SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
            message.setText("Incorrect Arguments Supplied. Please use the format /createUnbanTimer [userid] [durationValue] [durationField]");
            bot.send(message);
            return;
        }

        LocalDateTime expiryTime = LocalDateTime.now();
        switch(args[3].toLowerCase()){
            case "minutes":
                expiryTime = expiryTime.plusMinutes(Long.parseLong(args[2]));
                break;
            case "days": 
                expiryTime = expiryTime.plusDays(Long.parseLong(args[2]));
                break;
            case "weeks":
                expiryTime = expiryTime.plusWeeks(Long.parseLong(args[2]));
                break; 
            case "months":
                expiryTime = expiryTime.plusMonths(Long.parseLong(args[2]));
                break; 
            default:    
                SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
                message.setText("Incorrect Arguments Supplied. durationField should be minutes, days, weeks, or months");
                bot.send(message);
                return;
        }

        DBLoader.addTimer("UNBAN", args[1], expiryTime);

        DBLoader.logEvent("CREATE_MANUAL_TIMER", update.getMessage().getFrom().getId().toString(), args[1], "UNBAN - " + expiryTime.toString());
    }

    ////////////////////////////////////////////////////////
    // /getLogs //
    ////////////////////////////////////////////////////////
    public static void getLogs(Update update, ObibitalWeaponsPlatform bot){
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
        List<StatisticBean> logs = DBLoader.getLogs();
        String text = "Last " + logs.size() + " log entries: \n";

        for(StatisticBean log : logs){
            text = text + log + "\n";
        }
        message.setText(text);

        bot.send(message);
    }
    
    ////////////////////////////////////////////////////////
    // /getAllLogs //
    ////////////////////////////////////////////////////////
    public static void getAllLogs(Update update, ObibitalWeaponsPlatform bot){
        
        SendDocument doc = DBLoader.getLogsFile();
        doc.setChatId(update.getMessage().getChatId());
        doc.setReplyToMessageId(update.getMessage().getMessageId());

        try{
            bot.execute(doc);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////
    // /ban [user] [durationVal] [durationField] [reason] //
    ////////////////////////////////////////////////////////
    public static void ban(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(bot.getModChatId());

        if(args.length > 3){
            // args supplied, check if valid
            m_userId = p_userId.matcher(args[1]);
            m_userName = p_userName.matcher(args[1]);
            if (m_userId.find() ) {
                //determine length
                LocalDateTime expiryTime = LocalDateTime.now();
                for(int i = 0; i < args.length; i++){
                    System.out.println(args[i]);
                }
                switch(args[3].toLowerCase()){
                    case "minutes":
                        expiryTime = expiryTime.plusMinutes(Long.parseLong(args[2]));
                        break;
                    case "days": 
                        expiryTime = expiryTime.plusDays(Long.parseLong(args[2]));
                        break;
                    case "weeks":
                        expiryTime = expiryTime.plusWeeks(Long.parseLong(args[2]));
                        break; 
                    case "months":
                        expiryTime = expiryTime.plusMonths(Long.parseLong(args[2]));
                        break; 
                    default:    
                        message = new SendMessage().setChatId(update.getMessage().getChatId());
                        message.setText("Incorrect Arguments Supplied. durationField should be minutes, days, weeks, or months");
                        bot.send(message);
                    return;
                }

                KickChatMember kick = new KickChatMember(bot.getGroupChatId().toString(), Integer.parseInt(args[1]));
                kick.setUntilDate(expiryTime.toInstant(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now())));
                boolean success = true;
                try{
                    bot.execute(kick);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    success = false;
                }

                //create reason
                String reason = "";
                for(int i = 3; i < args.length; i++){
                    reason = reason + args[i] + " ";
                }

                if(success){
                    // add a ban and perform the ban
                    DBLoader.logEvent("BAN", update.getMessage().getFrom().getId().toString(), args[1], reason + "- " + "automatic unban on " + expiryTime);

                    List<StatisticBean> warnLogs = DBLoader.getLogsByAffectedUserAndEvents(args[1], new String[]{"WARN"});
                    List<StatisticBean> banLogs = DBLoader.getLogsByAffectedUserAndEvents(args[1], new String[]{"BAN"});
                    
                    message.setText("Banned succesfully for " + reason + " until " + expiryTime.toString() + ". User " + args[1] + " has " + warnLogs.size() + " warnings and " + banLogs.size() + " manual bans on record.");
                    bot.send(message);

                    DBLoader.addTimer("NOTIFY", bot.getModChatId() + args[1] + " has been unbanned from " + DBLoader.configValue("networkName") + " and may now rejoin: " + DBLoader.configValue("groupLink"), expiryTime);
                } else {
                    message.setText("Ban failure - please consult an administrator");
                    bot.send(message);
                }

            } else if ( m_userName.find() ) {
                //find the userid and add the admin
                //TODO: add @username functionality
                message.setChatId(update.getMessage().getChatId())
                .setText("@usernames are not supported at this time, please use userId");

                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. please use the userid as input");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/ban userid durationVal durationField reason`. To permaban use /permaban user reason");

            bot.send(message);
        }
    }

    ////////////////////////////////////////////////////////
    // /warn [user] [reason] //
    ////////////////////////////////////////////////////////
    public static void warn(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(bot.getModChatId());

        if(args.length > 1){
            // args supplied, check if valid
            m_userId = p_userId.matcher(args[1]);
            m_userName = p_userName.matcher(args[1]);
            if (m_userId.find() ) {
                //create reason
                String reason = "";
                for(int i = 2; i < args.length; i++){
                    reason = reason + args[i] + " ";
                }

                // add a warning 
                DBLoader.logEvent("WARN", update.getMessage().getFrom().getId().toString(), args[1], reason);

                List<StatisticBean> warnLogs = DBLoader.getLogsByAffectedUserAndEvents(args[1], new String[]{"WARN"});
                List<StatisticBean> banLogs = DBLoader.getLogsByAffectedUserAndEvents(args[1], new String[]{"BAN"});
                
                message.setText("Warning succesfully added for " + reason + ". User " + args[1] + " has " + warnLogs.size() + " warnings and " + banLogs.size() + " manual bans on record.");
                bot.send(message);

            } else if ( m_userName.find() ) {
                //find the userid and add the admin
                //TODO: add @username functionality
                message.setChatId(update.getMessage().getChatId())
                .setText("@usernames are not supported at this time, please use userId");

                bot.send(message);
            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. please use the userid as input");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/warn userid reason`");

            bot.send(message);
        }
    }

    //////////////////////
    // /getLastWeekLogs //
    //////////////////////
    public static void getLastWeekLogs(Update update, ObibitalWeaponsPlatform bot){
        SendDocument doc = DBLoader.getLogsFileInDateRange(LocalDateTime.now().minusWeeks(1l), LocalDateTime.now());
        doc.setChatId(update.getMessage().getChatId());
        doc.setReplyToMessageId(update.getMessage().getMessageId());

        try{
            bot.execute(doc);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    /////////////////
    // /weekReport //
    /////////////////
    public static void weekReport(Update update, ObibitalWeaponsPlatform bot){
        List<StatisticBean> logs = DBLoader.getLogsInDateRange(LocalDateTime.now().minusWeeks(1l), LocalDateTime.now());

        int warn = 0;
        int ban = 0;
        int ban_stickerpack = 0;
        int night_join = 0;
        int stickerspam = 0;

        for(StatisticBean bean : logs){
            switch(bean.getEvent()){
                case "WARN":
                    warn++;
                    break;
                case "BAN":
                    ban++;
                    break;
                case "BAN_STICKERPACK":
                    ban_stickerpack++;
                    break;
                case "NIGHT_JOIN":
                    night_join++;
                    break;
                case "STICKERSPAM":
                    stickerspam++;
                    break;
                default:
                    break;
            }
        }

        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
        message.setText("Events in the last week:\n" + 
            "Warnings: " + warn + "\n" +
            "Bans: " + ban + "\n" +
            "Stickerpack Bans: " + ban_stickerpack + "\n" +
            "Nighttime Joiners: " + night_join + "\n" +
            "Sticker Spam Attempts: " + stickerspam + "\n" 
        );

        bot.send(message);
    }

    /////////////////////////////
    // /getLogsByUser [userId] //
    /////////////////////////////
    public static void getLogsByUser(Update update, ObibitalWeaponsPlatform bot, String[] args){
        if(args.length > 1){
            SendDocument doc = DBLoader.getLogsFileBySourceUser(args[1]);
            doc.setChatId(update.getMessage().getChatId());
            doc.setReplyToMessageId(update.getMessage().getMessageId());

            try{
                bot.execute(doc);
            } catch (TelegramApiException e){
                e.printStackTrace();
            }
        } else {
            // please supply a valid username or userid
            SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/getLogsByUser [userId]`");

            bot.send(message);
        }
    }

    /////////////////////////////
    // /usernameFromId [userId] //
    /////////////////////////////
    public static void usernameFromId(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
        if(args.length > 1){
            // args supplied, check if valid
            m_userId = p_userId.matcher(args[1]);
            if (m_userId.find() ) {

                GetChat chat = new GetChat();
                chat.setChatId(Long.parseLong(args[1]));

                Chat result = null;

                try{
                    result = bot.execute(chat);
                } catch (TelegramApiException e){
                    e.printStackTrace();
                    System.out.println("Failed to get chat :(");
                }

                if(result != null){
                    message.setText(result.getUserName());
                    bot.send(message);
                } else {
                    message.setText("Couldn't Find User");
                    bot.send(message);
                }
                

            } else {
                //field doesn't match, prompt user for better one
                message.setChatId(update.getMessage().getChatId())
                .setText("incorrect format. please use the userid as input");

                bot.send(message);
            }
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/usernameFromId userid`");

            bot.send(message);
        }
    }

    /////////////////////////////
    // /idFromUsername [username] //
    /////////////////////////////
    public static void idFromUsername(Update update, ObibitalWeaponsPlatform bot, String[] args){
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
        message.setChatId(update.getMessage().getChatId())
            .setText("This method does not work, please find a solution for the problem");

            bot.send(message);
        if(args.length > 1){
            // args supplied, check if valid
            //m_userName = p_userName.matcher(args[1]);
            //if (m_userName.find() ) {

                GetChat chat = new GetChat();
                chat.setChatId(args[1]);

                Chat result = null;

                try{
                    result = bot.execute(chat);
                } catch (TelegramApiException e){
                    e.printStackTrace();
                    System.out.println("Failed to get chat :(");
                }

                if(result != null){
                    message.setText(result.getId().toString());
                    bot.send(message);
                } else {
                    message.setText("Couldn't Find User");
                    bot.send(message);
                }
                

            //} else {
            //    //field doesn't match, prompt user for better one
            //    message.setChatId(update.getMessage().getChatId())
            //    .setText("incorrect format. please use the username as input");
            //
            //    bot.send(message);
            //}
        } else {
            // please supply a valid username or userid
            message.setChatId(update.getMessage().getChatId())
            .setText("Please provide the correct input `/idFromUsername username`");

            bot.send(message);
        }
    }

    ////////////////////////////////////////////////
    // /getId // must be in response to a message //
    ////////////////////////////////////////////////
    public static void getId(Update update, ObibitalWeaponsPlatform bot){
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
        
        if(update.getMessage().getReplyToMessage() != null){
            //replied to a message, lets get the userid of it
            message.setText("userid: " + update.getMessage().getReplyToMessage().getFrom().getId());
            bot.send(message);
        } else {
            //they didn't reply to a message. silly.
            message.setText("please use this command in response to a message");
            bot.send(message);
        }
    }
}