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

public class DBLoader {
    static String dataPath = "src/main/data/";
    
    ///////////////////////////////////////////
    // Config                                //
    ///////////////////////////////////////////
    public static List<ConfigBean> loadConfig() throws IOException {
        Path myPath = Paths.get(dataPath + "config.csv");

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<ConfigBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ConfigBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(ConfigBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<ConfigBean> config = csvToBean.parse();

            return config;
        }
    }

    public static void saveConfig(List<ConfigBean> input) {
        try{
            Writer writer = new FileWriter(dataPath + "config.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(input);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////
    // Banned Sticker Packs                  //
    ///////////////////////////////////////////
    public static List<String> GetBannedStickers() throws IOException {

        List<BannedStickerBean> bannedBeans = GetBannedStickerBeans();

        ArrayList<String> bannedPacks = new ArrayList<>();
        for(BannedStickerBean bean : bannedBeans){
            bannedPacks.add(bean.getPackId());
        }

        return bannedPacks;
    }

    public static List<BannedStickerBean> GetBannedStickerBeans() throws IOException {
        Path myPath = Paths.get(dataPath + "bannedStickers.csv");

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<BannedStickerBean> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(BannedStickerBean.class);

            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(BannedStickerBean.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<BannedStickerBean> bannedPacks = csvToBean.parse();

            return bannedPacks;
        }
    }

    public static void banStickers(List<String> packsToBan) {
        try{
            List<BannedStickerBean> currentlyBanned = GetBannedStickerBeans();

            for(String pack : packsToBan){
                currentlyBanned.add(new BannedStickerBean(pack));
            }

            Writer writer = new FileWriter(dataPath + "bannedStickers.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(currentlyBanned);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void banSticker(String packToBan) {
        try{
            List<BannedStickerBean> currentlyBanned = GetBannedStickerBeans();

            
            currentlyBanned.add(new BannedStickerBean(packToBan));

            Writer writer = new FileWriter(dataPath + "bannedStickers.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(currentlyBanned);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void unbanSticker(String packToUnban) {
        try{
            List<String> currentlyBanned = GetBannedStickers();

            currentlyBanned.remove(packToUnban);

            List<BannedStickerBean> bannedBeans = new ArrayList<BannedStickerBean>();
            for(String pack : currentlyBanned){
                bannedBeans.add(new BannedStickerBean(pack));
            }

            Writer writer = new FileWriter(dataPath + "bannedStickers.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(bannedBeans);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////
    // Approved Admin List                   //
    ///////////////////////////////////////////
    public static List<Long> getAdmins() throws IOException {
        ArrayList<Long> admins = new ArrayList<>();
        Path myPath = Paths.get(dataPath + "admins.csv");

        try (BufferedReader br = Files.newBufferedReader(myPath,
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

            return admins;
        }
    }

    public static void addAdmin(Long id) throws IOException {
        Path myPath = Paths.get(dataPath + "admins.csv");
        List<AdminBean> adminBeans = new ArrayList<>();
        List<Long> admins = new ArrayList<Long>();

        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.UTF_8)) {

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

                Writer writer = new FileWriter(dataPath + "admin.csv");
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                
                beanToCsv.write(adminBeans);
                writer.close();
            }
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeAdmin(Long id) throws IOException {
        Path myPath = Paths.get(dataPath + "admins.csv");
        List<AdminBean> adminBeans = new ArrayList<>();
        List<Long> admins = new ArrayList<Long>();

        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.UTF_8)) {

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
                // admin present in list, remove themelse 
                adminBeans.remove(id);
            } 

            Writer writer = new FileWriter(dataPath + "admin.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            
            beanToCsv.write(adminBeans);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }
}
