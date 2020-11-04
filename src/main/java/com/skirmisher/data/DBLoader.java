package com.skirmisher.data;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import com.skirmisher.data.beans.*;
import java.time.*;
import java.util.Arrays;

public class DBLoader {
    static String dataPath = "src/main/data/";

    static String configFile = "config.csv";
    static String adminsFile = "admins.csv";
    static String bannedStickersFile = "bannedStickers.csv";
    static String modulesFile = "modules.csv";

    static Path configPath = Paths.get(dataPath+configFile);
    static Path adminsPath = Paths.get(dataPath+adminsFile);
    static Path bannedStickersPath = Paths.get(dataPath+bannedStickersFile);
    static Path modulesPath = Paths.get(dataPath+modulesFile);
    
    ///////////////////////////////////////////
    // Config                                //
    ///////////////////////////////////////////
    public static List<ConfigBean> loadConfig() {
        List<ConfigBean> config = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(configPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<ConfigBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ConfigBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(ConfigBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            config = csvToBean.parse();

            
        } catch (IOException e){
            e.printStackTrace();
        }
        return config;
    }

    public static void saveConfig(List<ConfigBean> input) {
        try{
            Writer writer = new FileWriter(dataPath+configFile);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(input);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String configValue(String value){
        List<ConfigBean> config = loadConfig();

        for(ConfigBean bean : config){
            if(bean.getElement().equals(value)){
                return bean.getValue();
            }
        }
        System.out.println("FAILEDTOLOAD: " + value);
        return "FAILEDTOLOAD: " + value;
    }

    public static void updateConfigValue(String elementToUpdate, String value){
        if(elementToUpdate == "botusername" || elementToUpdate == "bottoken" ){
            return;
        }
        List<ConfigBean> config = loadConfig();
        List<ConfigBean> updatedConfig = new ArrayList<ConfigBean>();

        for(ConfigBean bean : config){
            if(bean.getElement().equals(elementToUpdate)){
                bean.setValue(value);
            }
            updatedConfig.add(bean);
        }

        saveConfig(updatedConfig);
    }

    ///////////////////////////////////////////
    // Banned Sticker Packs                  //
    ///////////////////////////////////////////
    public static List<String> GetBannedStickers() {

        List<BannedStickerBean> bannedBeans = GetBannedStickerBeans();

        ArrayList<String> bannedPacks = new ArrayList<>();
        for(BannedStickerBean bean : bannedBeans){
            bannedPacks.add(bean.getPackId());
        }

        return bannedPacks;
    }

    public static List<BannedStickerBean> GetBannedStickerBeans(){
        List<BannedStickerBean> bannedPacks = new ArrayList<BannedStickerBean>();

        try (BufferedReader br = Files.newBufferedReader(bannedStickersPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<BannedStickerBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(BannedStickerBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(BannedStickerBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            bannedPacks = csvToBean.parse();
        } catch (IOException e){
            e.printStackTrace();
        }
        return bannedPacks;
    }

    public static boolean banStickers(List<String> packsToBan) {
        boolean success = false;
        try{
            List<BannedStickerBean> currentlyBanned = GetBannedStickerBeans();

            for(BannedStickerBean bannedPack : currentlyBanned){
                if(packsToBan.contains(bannedPack.getPackId())){
                    packsToBan.remove(bannedPack.getPackId());
                }
            }

            for(String pack : packsToBan){
                currentlyBanned.add(new BannedStickerBean(pack));
                success = true;
            }

            Writer writer = new FileWriter(dataPath + bannedStickersFile);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(currentlyBanned);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean banSticker(String packToBan) {
        boolean success = false;
        try{
            List<BannedStickerBean> currentlyBanned = GetBannedStickerBeans();

            for(BannedStickerBean bean : currentlyBanned){
                if (bean.getPackId().equals(packToBan)){
                    return success;
                }
            }

            currentlyBanned.add(new BannedStickerBean(packToBan));
            success=true;

            Writer writer = new FileWriter(dataPath + bannedStickersFile);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(currentlyBanned);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean unbanSticker(String packToUnban) {
        boolean success = false;
        try{
            List<String> currentlyBanned = GetBannedStickers();

            if(currentlyBanned.remove(packToUnban)){
                success = true;
            } else {
                return false;
            }

            List<BannedStickerBean> bannedBeans = new ArrayList<BannedStickerBean>();
            for(String pack : currentlyBanned){
                bannedBeans.add(new BannedStickerBean(pack));
            }

            Writer writer = new FileWriter(dataPath + bannedStickersFile);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(bannedBeans);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    ///////////////////////////////////////////
    // Approved Admin List                   //
    ///////////////////////////////////////////
    public static List<Long> getAdmins(){
        ArrayList<Long> admins = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(adminsPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<AdminBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(AdminBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(AdminBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<AdminBean> adminBeans = csvToBean.parse();

            for(AdminBean ad: adminBeans) {
                admins.add(ad.getUserId());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return admins;
    }

    public static boolean addAdmin(Long id) {
        System.out.println("DBLoader:: addAdmin:: " + id);
        List<AdminBean> adminBeans = new ArrayList<>();
        List<Long> admins = new ArrayList<Long>();
        boolean success = false;

        try (BufferedReader br = Files.newBufferedReader(adminsPath, StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<AdminBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(AdminBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(AdminBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            adminBeans = csvToBean.parse();
            for(AdminBean ad: adminBeans) {
                admins.add(ad.getUserId());
            }

            if (admins.contains(id)){
                // admin already in list, abort
            } else {
                adminBeans.add(new AdminBean(id));
                success=true;

                Writer writer = new FileWriter(dataPath + adminsFile);
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                
                beanToCsv.write(adminBeans);
                writer.close();
            }
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean removeAdmin(Long id) {
        System.out.println("DBLoader:: removeAdmin:: " + id);
        List<AdminBean> adminBeans = new ArrayList<>();
        boolean success = false;
        List<AdminBean> newAdmins = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(adminsPath, StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<AdminBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(AdminBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(AdminBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            adminBeans = csvToBean.parse();
            for(AdminBean ad: adminBeans) {
                if(!ad.getUserId().equals(id)){
                    newAdmins.add(ad);
                } else {
                    success=true;
                }
            }

            Writer writer = new FileWriter(dataPath + adminsFile);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            
            beanToCsv.write(newAdmins);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    ///////////////////////
    // Module Enablement //
    ///////////////////////
    public static String enableModule(String moduleToEnable){
        System.out.println("DBLoader:: enableModule:: " + moduleToEnable);
        List<ModuleBean> moduleBeans = getModules();
        List<ModuleBean> newModules = new ArrayList<>();
        String response = "";
        boolean foundModule = false;
        
        for(ModuleBean mod: moduleBeans) {
            if(mod.getModule().equals(moduleToEnable)){
                foundModule = true;
                if(mod.getEnabled()){
                    //already enabled, say as much
                    response = "module " + moduleToEnable + " is already enabled";
                } else {
                    mod.setEnabled(true);
                    response = "success";
                }
            }
            newModules.add(mod);
        }

        if(!foundModule){
            //module was not found
            response = "module " + moduleToEnable + " was not found. Please use /addModule if this is in error";
        }

        
        try{
            Writer writer = new FileWriter(dataPath + modulesFile);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                
            beanToCsv.write(newModules);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
        
        return response;
    }

    public static String disableModule(String moduleToDisable){
        System.out.println("DBLoader:: disableModule:: " + moduleToDisable);
        List<ModuleBean> moduleBeans = getModules();
        List<ModuleBean> newModules = new ArrayList<>();
        String response = "";
        boolean foundModule = false;
        
        for(ModuleBean mod: moduleBeans) {
            if(mod.getModule().equals(moduleToDisable)){
                foundModule = true;
                if(!mod.getEnabled()){
                    //already enabled, say as much
                    response = "module " + moduleToDisable + " is already disabled";
                } else {
                    mod.setEnabled(false);
                    response = "module " + moduleToDisable + " has been disabled";
                }
            }
            newModules.add(mod);
        }

        if(!foundModule){
            //module was not found
            response = "module " + moduleToDisable + " was not found. Please use /addModule if this is in error";
        }

        
        try{
            Writer writer = new FileWriter(dataPath + modulesFile);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                
            beanToCsv.write(newModules);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
        
        return response;
    }

    public static List<ModuleBean> getModules(){
        System.out.println("DBLoader:: getModules:: ");
        List<ModuleBean> modules = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(modulesPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<ModuleBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ModuleBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(ModuleBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            modules = csvToBean.parse();
        } catch (IOException e){
            e.printStackTrace();
        }
        return modules;
    }

    public static boolean getModuleStatus(String moduleToCheck){
        System.out.println("DBLoader:: getModuleStatus:: " + moduleToCheck);
        //caveat: will return false if no module found
        List<ModuleBean> modules = getModules();
        boolean status = false;

        for(ModuleBean mod : modules){
            if(mod.getModule().equals(moduleToCheck)){
                status = mod.getEnabled();
            }
        }

        return status;
    }

    public static boolean addModule(String moduleToAdd){
        //WARNING: should be in default config, not being present is an error
        System.out.println("DBLoader:: addModule:: " + moduleToAdd);
        List<ModuleBean> moduleBeans = new ArrayList<>();
        List<String> modules = new ArrayList<>();
        boolean success = false;

        try (BufferedReader br = Files.newBufferedReader(adminsPath, StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<ModuleBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ModuleBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(AdminBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            moduleBeans = csvToBean.parse();
            for(ModuleBean mod: moduleBeans) {
                modules.add(mod.getModule());
            }

            if (modules.contains(moduleToAdd)){
                // module already in list, abort
            } else {
                moduleBeans.add(new ModuleBean(moduleToAdd, false));
                success=true;

                Writer writer = new FileWriter(dataPath + modulesFile);
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                
                beanToCsv.write(moduleBeans);
                writer.close();
            }
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean removeModule(String moduleToRemove){
        //WARNING: should be in default config, not being present is an error
        System.out.println("DBLoader:: removeModule:: " + moduleToRemove);
        List<ModuleBean> moduleBeans = new ArrayList<>();
        boolean success = false;
        List<ModuleBean> newModules = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(modulesPath, StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<ModuleBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ModuleBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(AdminBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

                moduleBeans = csvToBean.parse();
            for(ModuleBean mod: moduleBeans) {
                if(!mod.getModule().equals(moduleToRemove)){
                    newModules.add(mod);
                } else {
                    success=true;
                }
            }

            Writer writer = new FileWriter(dataPath + adminsFile);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            
            beanToCsv.write(newModules);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    ///////////////////////////////////////////
    // Timers                                //
    ///////////////////////////////////////////
    static int timerId = 0;

    public static List<TimerBean> getAllTimers() {
        List<TimerBean> timerBeans = new ArrayList<>();
        Path myPath = Paths.get(dataPath + "timers.csv");

        // try {
        //     CsvToBeanBuilder<TimerBean> beanBuilder = new CsvToBeanBuilder<>(new InputStreamReader(new FileInputStream(dataPath + "timers.csv")));
        //     beanBuilder.withType(TimerBean.class);
        //     timerBeans = beanBuilder.build().parse();
        // } catch (FileNotFoundException e){
        //     e.printStackTrace();
        // }

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<TimerBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(TimerBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(TimerBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            timerBeans = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return timerBeans;
    }

    public static List<TimerBean> getExpiredTimers() {
        List<TimerBean> timerBeans = getAllTimers();
        List<TimerBean> expiredBeans = new ArrayList<>();

        for(TimerBean tim : timerBeans){
            if(tim.getExpiry().isBefore(LocalDateTime.now())){
                expiredBeans.add(tim);
            }
        }
        return expiredBeans;
    }

    public static void addTimer(String action, String args, LocalDateTime time) {
        List<TimerBean> timerBeans = getAllTimers();

        timerBeans.add(new TimerBean(
            timerId++,
            action,
            args,
            time
        ));

        try{
            Writer writer = new FileWriter(dataPath + "timers.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            
            beanToCsv.write(timerBeans);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean removeTimer(int id) {
        List<TimerBean> timerBeans = getAllTimers();
        List<TimerBean> newBeans = new ArrayList<>();
        Path myPath = Paths.get(dataPath + "timers.csv");
        boolean success=false;

        for(TimerBean tim : timerBeans){
            if(tim.getTimerId() == id ){
                success=true;
            } else {
                newBeans.add(tim);
            }
        }

        if(success){
            try{
                Writer writer = new FileWriter(dataPath + "timers.csv");
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                
                beanToCsv.write(newBeans);
                writer.close();
            } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public static void updateTimerId() {
        //get timers
        List<TimerBean> timerBeans = getAllTimers();
        //find largest timer val
        int largestVal = 0;
        for(TimerBean tim : timerBeans){
            if(tim.getTimerId() > largestVal){
                largestVal = tim.getTimerId();
            }
        }
        //add 1
        largestVal++;
        //timerId = above
        timerId = largestVal;
    }

    
    ///////////////////////////////////////////
    // Statistics                            //
    ///////////////////////////////////////////
    public static void logEvent(String event, String sourceUser, String affectedUser, String notes) {
        //Time, Event, SourceUser, AffectedUser, Notes
        //TODO: refactor how we add new values to a file
        Path myPath = Paths.get(dataPath + "statistics.csv");
        List<StatisticBean> statBeans = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<StatisticBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(StatisticBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(TimerBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            statBeans = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StatisticBean newStat = new StatisticBean();
        newStat.setTime(LocalDateTime.now());
        newStat.setEvent(event);
        newStat.setSourceUser(sourceUser);
        newStat.setAffectedUser(affectedUser);
        newStat.setNotes(notes);

        statBeans.add(newStat);

        try{
            Writer writer = new FileWriter(dataPath + "statistics.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            
            beanToCsv.write(statBeans);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }

    }

    public static List<StatisticBean> getLogs() {
        //by default returns last 5 events. 
        Path myPath = Paths.get(dataPath + "statistics.csv");
        List<StatisticBean> statBeans = new ArrayList<>();
        List<StatisticBean> returnBeans = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<StatisticBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(StatisticBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(TimerBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            statBeans = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(statBeans.size()-5 < 0){
            returnBeans = statBeans.subList(0, statBeans.size());
        } else {
            returnBeans = statBeans.subList(statBeans.size()-5, statBeans.size());
        }

        return returnBeans;

    }

    public static void getLogsFile() {
        //TODO: return a copy of the full csv file 
    }

    public static void getLogsFileBySourceUser(String userId) {
        //return a csv file containing logs regarding said user
    }

    public static List<StatisticBean> getLogsByAffectedUser(String userId) {
        Path myPath = Paths.get(dataPath + "statistics.csv");
        List<StatisticBean> statBeans = new ArrayList<>();
        List<StatisticBean> returnBeans = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<StatisticBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(StatisticBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(TimerBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            statBeans = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(StatisticBean bean : statBeans){
            if(bean.getAffectedUser() == userId){
                returnBeans.add(bean);
            }
        }

        return returnBeans;
        //returns the beans
    }

    public static List<StatisticBean> getLogsByAffectedUserAndEvents(String userId, String[] event) {
        Path myPath = Paths.get(dataPath + "statistics.csv");
        List<StatisticBean> statBeans = new ArrayList<>();
        List<StatisticBean> returnBeans = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<StatisticBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(StatisticBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(TimerBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            statBeans = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException while trying to read file");
        }

        System.out.println("statbeans length: " + statBeans.size());
        System.out.println("event length: " + event.length);

        for(StatisticBean bean : statBeans){
            System.out.println("gonna compare: " + bean.getAffectedUser() + " with " + userId);
            if(bean.getAffectedUser().equals(userId)/* && Arrays.stream(event).anyMatch(bean.getEvent()::equals)*/){
                System.out.println("found a bean for the userId");
                for(int i = 0; i < event.length; i++){
                    System.out.println("gonna compare: " + bean.getEvent() + " with " + event[i]);
                    if(event[i].equals(bean.getEvent())){
                        System.out.println("bean is eventy");
                        returnBeans.add(bean);
                    }
                }
                //returnBeans.add(bean);
            }
        }

        return returnBeans;
        //returns the beans
    }

    public static void getLogsFileByEvent(String event) {
        //returns a csv file containing logs regarding said event
    }

    public static void getLogsInDateRange(String startDate, String endDate) {
        //returns a csv file containing logs regarding said event
    }
    
}
