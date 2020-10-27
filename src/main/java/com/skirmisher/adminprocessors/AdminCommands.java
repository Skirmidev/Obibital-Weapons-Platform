package com.skirmisher.adminprocessors;

import org.telegram.telegrambots.meta.api.objects.Update;
import com.skirmisher.obibital.ObibitalWeaponsPlatform;
import com.skirmisher.processors.StickerPackBanner;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.skirmisher.data.DBLoader;
import com.skirmisher.data.beans.ModuleBean;
import com.skirmisher.obibital.Context;
import com.skirmisher.obibital.ModuleControl;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;

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

                //this is a command. Switch to find out what it does.
                switch(args[0]){ //add something here to split the string apart based on gaps
                    case "/help":
                        help(update, bot);
                        context.setResult("AdminCommands: Help command");
                        context.setBlockingResult(true);
                        break;
                    case "/addAdmin":
                        addAdmin(update, bot, args);
                        context.setResult("AdminCommands: addAdmin command");
                        context.setBlockingResult(true);
                        break;
                    case "/removeAdmin":
                        removeAdmin(update, bot, args);
                        context.setResult("AdminCommands: removeAdmin command");
                        context.setBlockingResult(true);
                        break;
                    case "/listAdmins":
                        listAdmins(update, bot);
                        context.setResult("AdminCommands: listAdmins command");
                        context.setBlockingResult(true);
                        break;
                    case "/disableModule":
                        disableModule(update, bot, args);
                        context.setResult("AdminCommands: disableModule command");
                        context.setBlockingResult(true);
                        break;
                    case "/enableModule":
                        enableModule(update, bot, args);
                        context.setResult("AdminCommands: enableModule command");
                        context.setBlockingResult(true);
                        break;
                    case "/getModules":
                        getModules(update, bot);
                        context.setResult("AdminCommands: getModules command");
                        context.setBlockingResult(true);
                            break;
                    case "/moduleStatus":
                        moduleStatus(update, bot, args);
                        context.setResult("AdminCommands: getModules command");
                        context.setBlockingResult(true);
                        break;
                    case "/addModule":
                        addModule(update, bot, args);
                        context.setResult("AdminCommands: addModule command");
                        context.setBlockingResult(true);
                        break;
                    case "/removeModule":
                        removeModule(update, bot, args);
                        context.setResult("AdminCommands: removeModule command");
                        context.setBlockingResult(true);
                        break;
                    case "/banPack":
                        banPack(update, bot, args);
                        context.setResult("AdminCommands: banPack command");
                        context.setBlockingResult(true);
                        break;
                    case "/unbanPack":
                        unbanPack(update, bot, args);
                        context.setResult("AdminCommands: unbanPack command");
                        context.setBlockingResult(true);
                        break;
                    case "/getBannedPacks":
                        getBannedPacks(update, bot);
                        context.setResult("AdminCommands: unbanPack command");
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
                            "The following admin command are available"  + "\n" +
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
                            "The following commands are for emergency use only"  + "\n" +
                            "/addModule [moduleName]"  + "\n" +
                            "/removeModule [moduleName]"  + "\n" +
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
}