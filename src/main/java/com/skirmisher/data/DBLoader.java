package com.skirmisher.data;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import com.skirmisher.data.beans.*;
import java.time.*;
import java.util.Map;
import java.util.HashMap;
import org.telegram.telegrambots.meta.api.objects.InputFile;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class DBLoader {

    static String url = "jdbc:postgresql://localhost:5432/obibital-db";
    static String user = "postgres";
    
    // ///////////////////////////////////////////
    // // Config                                //
    // ///////////////////////////////////////////
    // public static List<ConfigBean> loadConfig() {
    // }

    // public static void saveConfig(List<ConfigBean> input) {
    // }

    public static String configValue(String value){
        String query = "SELECT value FROM config WHERE element = '" + value + "'";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery(query);

            rs.next();
            String returnVal = rs.getString(1);
            
            st.close();
            return returnVal;
        } catch (SQLException ex) {
            System.out.println("Exceptioned: " + ex.getMessage());
            ex.printStackTrace();
        }

        return "FAILEDTOLOAD: " + value;
    }

    public static void updateConfigValue(String elementToUpdate, String value){
        String query = "UPDATE config SET value = ? WHERE element = ?";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            
            pst.setString(1, value);
            pst.setString(2, elementToUpdate);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ///////////////////////////////////////////
    // // Approved Admin List                   //
    // ///////////////////////////////////////////
    public static Map<String, Integer> getAdmins(){
        String query = "SELECT username, userid FROM users WHERE isadmin = 'true'";
        Map<String, Integer> admins = new HashMap<String, Integer>();

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                admins.put(rs.getString(1), rs.getInt(2));
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }

    // ///////////////////////
    // // Module Enablement //
    // ///////////////////////
    public static boolean enableModule(String moduleName){
        String query = "UPDATE modules SET enabled = 'true' WHERE modulename = ?";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            
            pst.setString(1, moduleName);
            int response = pst.executeUpdate();
            return (response != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean disableModule(String moduleName){
        String query = "UPDATE modules SET enabled = 'false' WHERE modulename = ?";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            
            pst.setString(1, moduleName);
            int response = pst.executeUpdate();
            return (response != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<ModuleValue> getModules(){
        String query = "SELECT module, enabled FROM modules";
        List<ModuleValue> modvals = new ArrayList<ModuleValue>();

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                ModuleValue mv = new ModuleValue(rs.getString(1), rs.getBoolean(2));
                modvals.add(mv);
            }
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modvals;

    }

    public static boolean getModuleStatus(String moduleName){
        String query = "SELECT enabled FROM modules WHERE module = '" + moduleName + "'";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery(query);

            if(rs.next()){
                return rs.getBoolean(1);
            }
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean addModule(String moduleName){
        String query = "INSERT INTO modules(module) VALUES(?)";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            
            pst.setString(1, moduleName);
            int response = pst.executeUpdate();
            return (response != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean removeModule(String moduleName){
        String query = "DELETE FROM modules WHERE module = ?";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            
            pst.setString(1, moduleName);
            int response = pst.executeUpdate();
            return (response != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ///////////////////////////////////////////
    // // Timers                                //
    // ///////////////////////////////////////////
    // static int timerId = 0;

    // public static List<TimerBean> getAllTimers() {
    // }

    public static ArrayList<TimerValue> getExpiredTimers() {
        String query = "SELECT id, action, args FROM timers WHERE expiry <= ?";
        ArrayList<TimerValue> expired = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            pst.setObject(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));

            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                expired.add(new TimerValue(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expired;
    }

    public static void addTimer(String action, String args, LocalDateTime time) {
        String query = "INSERT INTO timers(action, args, expiry) VALUES(?, ?, ?)";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            
            pst.setString(1, action);
            pst.setString(2, args);
            pst.setObject(3, java.sql.Timestamp.valueOf(time));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean removeTimer(int id) {
        String query = "DELETE FROM timers WHERE id = ?";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            
            pst.setInt(1, id);
            int response = pst.executeUpdate();
            return (response != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    // ///////////////////////////////////////////
    // // Statistics                            //
    // ///////////////////////////////////////////
    public static void logEvent(String event, int sourceUser, int affectedUser, String notes) {
        String query = "INSERT INTO logging(event, sourceuser, affecteduser, notes) VALUES(?, ?, ?, ?)";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            
            pst.setString(1, event);
            pst.setInt(2, sourceUser);
            pst.setInt(3, affectedUser);
            pst.setString(4, notes);

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // public static List<StatisticBean> getAllLogs() {
    // }

    public static List<LogValue> getLogs() { //by default returns last 5 events
        String query = "SELECT * FROM logging ORDER BY id DESC LIMIT 5";
        ArrayList<LogValue> logs = new ArrayList<LogValue>();

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                LogValue lv = new LogValue(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class));
                logs.add(lv);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    public static SendDocument getLogsFile() {
        String query = "SELECT * FROM logging ORDER BY id DESC";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery(query);

            BufferedWriter writer = new BufferedWriter(new FileWriter("/tmp" + "/getLogsFile.csv"));

            while(rs.next()){
                writer.write(rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) + "," + rs.getString(5) + "," + rs.getObject(6, LocalDateTime.class).toString());
            }

            writer.close();
            st.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        File statsFile = new File("/tmp" + "/getLogsFile.csv");
        SendDocument doc = new SendDocument();
        InputFile inFile = new InputFile(statsFile);

        doc.setDocument(inFile);
        doc.setCaption(LocalDateTime.now().toString());

        return doc;
    }

    public static SendDocument getLogsFileBySourceUser(int userId) {
        String query = "SELECT * FROM logging WHERE sourceuser = '" + userId + "' ORDER BY id DESC";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery(query);

            BufferedWriter writer = new BufferedWriter(new FileWriter("/tmp" + "/getLogsFileBySourceUser"));

            while(rs.next()){
                writer.write(rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) + "," + rs.getString(5) + "," + rs.getObject(6, LocalDateTime.class).toString());
            }

            writer.close();
            st.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        File statsFile = new File("/tmp" + "/getLogsFileBySourceUser.csv");
        SendDocument doc = new SendDocument();
        InputFile inFile = new InputFile(statsFile);

        doc.setDocument(inFile);
        doc.setCaption(LocalDateTime.now().toString());

        return doc;
    }

    public static List<LogValue> getLogsByAffectedUser(int userId) {
        String query = "SELECT * FROM logging WHERE affecteduser = '" + userId + "' ORDER BY id DESC";
        ArrayList<LogValue> logs = new ArrayList<LogValue>();

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                LogValue lv = new LogValue(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class));
                logs.add(lv);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    public static List<LogValue> getLogsByAffectedUserAndEvent(int userId, String event) {
        String query = "SELECT * FROM logging WHERE affecteduser = '" + userId + "' AND event = '" + event + "' ORDER BY id DESC";
        ArrayList<LogValue> logs = new ArrayList<LogValue>();

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                LogValue lv = new LogValue(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class));
                logs.add(lv);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    public static SendDocument getLogsFileByEvent(String event) {
        String query = "SELECT * FROM logging WHERE event = '" + event + "' ORDER BY id DESC";

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            Statement st = con.createStatement();
            
            ResultSet rs = st.executeQuery(query);

            BufferedWriter writer = new BufferedWriter(new FileWriter("/tmp" + "/getLogsFileByEvent.csv"));

            while(rs.next()){
                writer.write(rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) + "," + rs.getString(5) + "," + rs.getObject(6, LocalDateTime.class).toString());
            }

            writer.close();
            st.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        File statsFile = new File("/tmp" + "/getLogsFileByEvent.csv");
        SendDocument doc = new SendDocument();
        InputFile inFile = new InputFile(statsFile);

        doc.setDocument(inFile);
        doc.setCaption(LocalDateTime.now().toString());

        return doc;
    }

    public static SendDocument getLogsFileInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String query = "SELECT * FROM logging WHERE date >= ? AND date < ?";
        ArrayList<LogValue> logs = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            pst.setObject(1, java.sql.Timestamp.valueOf(startDate));
            pst.setObject(2, java.sql.Timestamp.valueOf(endDate));
            
            ResultSet rs = pst.executeQuery(query);

            BufferedWriter writer = new BufferedWriter(new FileWriter("/tmp" + "/getLogsFileInDateRange.csv"));

            while(rs.next()){
                writer.write(rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4) + "," + rs.getString(5) + "," + rs.getObject(6, LocalDateTime.class).toString());
            }
            rs.close();
            writer.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        File statsFile = new File("/tmp" + "/getLogsFileInDateRange.csv");
        SendDocument doc = new SendDocument();
        InputFile inFile = new InputFile(statsFile);

        doc.setDocument(inFile);
        doc.setCaption(LocalDateTime.now().toString());

        return doc;
    }
    
    public static List<LogValue> getLogsInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String query = "SELECT * FROM logging WHERE date >= ? AND date < ?";
        ArrayList<LogValue> logs = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(url, user, null);
            PreparedStatement pst = con.prepareStatement(query);
            pst.setObject(1, LocalDateTime.now());
            pst.setObject(1, java.sql.Timestamp.valueOf(startDate));
            pst.setObject(2, java.sql.Timestamp.valueOf(endDate));
            
            ResultSet rs = pst.executeQuery(query);

            while(rs.next()){
                LogValue lv = new LogValue(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getObject(6, LocalDateTime.class));
                logs.add(lv);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }
    
}
