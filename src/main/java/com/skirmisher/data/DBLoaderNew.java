package com.skirmisher.data;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import com.skirmisher.data.beans.*;
import java.time.*;

public class DBLoaderNew {
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
    }

    public static void saveConfig(List<ConfigBean> input) {
    }

    public static String configValue(String value){
    }

    public static void updateConfigValue(String elementToUpdate, String value){
    }

    ///////////////////////////////////////////
    // Banned Sticker Packs                  //
    ///////////////////////////////////////////
    public static List<String> GetBannedStickers() {
    }

    public static List<BannedStickerBean> GetBannedStickerBeans(){
    }

    public static boolean banStickers(List<String> packsToBan) {
    }

    public static boolean banSticker(String packToBan) {
    }

    public static boolean unbanSticker(String packToUnban) {
    }

    ///////////////////////////////////////////
    // Approved Admin List                   //
    ///////////////////////////////////////////
    public static List<Long> getAdmins(){
    }

    public static boolean addAdmin(Long id) {
    }

    public static boolean removeAdmin(Long id) {
    }

    ///////////////////////
    // Module Enablement //
    ///////////////////////
    public static String enableModule(String moduleToEnable){
    }

    public static String disableModule(String moduleToDisable){
    }

    public static List<ModuleBean> getModules(){
    }

    public static boolean getModuleStatus(String moduleToCheck){
    }

    public static boolean addModule(String moduleToAdd){
    }

    public static boolean removeModule(String moduleToRemove){
    }

    ///////////////////////////////////////////
    // Timers                                //
    ///////////////////////////////////////////
    static int timerId = 0;

    public static List<TimerBean> getAllTimers() {
    }

    public static List<TimerBean> getExpiredTimers() {
    }

    public static void addTimer(String action, String args, LocalDateTime time) {
    }

    public static boolean removeTimer(int id) {
    }

    public static void updateTimerId() {
    }

    
    ///////////////////////////////////////////
    // Statistics                            //
    ///////////////////////////////////////////
    public static void logEvent(String event, String sourceUser, String affectedUser, String notes) {
    }

    public static List<StatisticBean> getAllLogs() {
    }

    public static List<StatisticBean> getLogs() {
    }

    public static SendDocument getLogsFile() {
    }

    public static SendDocument getLogsFileBySourceUser(String userId) {
    }

    public static List<StatisticBean> getLogsByAffectedUser(String userId) {
    }

    public static List<StatisticBean> getLogsByAffectedUserAndEvents(String userId, String[] event) {
    }

    public static SendDocument getLogsFileByEvent(String event) {
    }

    public static SendDocument getLogsFileInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    }
    
    public static List<StatisticBean> getLogsInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    }
    
}
