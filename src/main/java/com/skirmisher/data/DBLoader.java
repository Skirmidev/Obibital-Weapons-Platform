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

public class DBLoader {

    ///////////////////////////////////////////
    // Config                                //
    ///////////////////////////////////////////
    public static List<ConfigBean> loadConfig() throws IOException {
        Path myPath = Paths.get("src/main/data/config.csv");

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
            Writer writer = new FileWriter("src/main/data/config.csv");
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
    public static List<BannedStickerBean> GetBannedStickers() throws IOException {
        Path myPath = Paths.get("src/main/data/bannedStickers.csv");

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

    public static void updateBannedStickers(List<BannedStickerBean> packsToBan) {
        try{
            Writer writer = new FileWriter("src/main/data/bannedStickers.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(packsToBan);
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
        Path myPath = Paths.get("src/main/data/admins.csv");

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
        Path myPath = Paths.get("src/main/data/admins.csv");
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
            } else 
            adminBeans.add(new AdminBean(id));

            Writer writer = new FileWriter("src/main/data/admin.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            
            beanToCsv.write(adminBeans);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeAdmin(Long id) throws IOException {
        Path myPath = Paths.get("src/main/data/admins.csv");
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
                adminBeans.remove(adminBeans.indexOf(id));
            } 

            Writer writer = new FileWriter("src/main/data/admin.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            
            beanToCsv.write(adminBeans);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }
}
