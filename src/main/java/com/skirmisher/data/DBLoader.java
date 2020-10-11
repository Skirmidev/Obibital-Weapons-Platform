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

public class DBLoader {
    public static List<ConfigBean> loadConfig() throws IOException {
        Path myPath = Paths.get("src/main/resources/config.csv");

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
            Writer writer = new FileWriter("src/main/resources/config.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(input);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }


    public static List<BannedStickerBean> GetBannedStickers() throws IOException {
        Path myPath = Paths.get("src/main/resources/bannedStickers.csv");

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
            Writer writer = new FileWriter("src/main/resources/bannedStickers.csv");
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(packsToBan);
            writer.close();
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
        }
    }
}
