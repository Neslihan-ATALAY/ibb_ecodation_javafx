package com.neslihanatalay.ibb_ecodation_javafx.database;

import org.h2.tools.Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SingletonPropertiesDBConnection {

    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    private static SingletonPropertiesDBConnection instance;
    private Connection connection;

    private SingletonPropertiesDBConnection() {
        try {
            loadDatabaseConfig();
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("âœ… VeritabanÄ± baÄŸlantÄ±sÄ± baÅŸarÄ±lÄ±.");
            H2DbStarting();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("âŒ VeritabanÄ± baÄŸlantÄ±sÄ± baÅŸarÄ±sÄ±z!");
        }
    }
	
    private void H2DbStarting() {
        try {
            Server server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("H2 Web Console is running at: http://localhost:8082");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadDatabaseConfig() {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);

            URL = properties.getProperty("db.url", "jdbc:h2:./h2db/user_management");
            USERNAME = properties.getProperty("db.username", "sa");
            PASSWORD = properties.getProperty("db.password", "");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("âŒ VeritabanÄ± yapÄ±landÄ±rmasÄ± yÃ¼klenemedi!");
        }
    }

    public static synchronized SingletonPropertiesDBConnection getInstance() {
        if (instance == null) {
            instance = new SingletonPropertiesDBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
                System.out.println("ğŸ”’ VeritabanÄ± baÄŸlantÄ±sÄ± kapatÄ±ldÄ±.");
            } catch (SQLException e) {
                throw new RuntimeException("âŒ BaÄŸlantÄ± kapatÄ±lÄ±rken hata oluÅŸtu!", e);
            }
        }
    }

    public static void main(String[] args) throws SQLException {
	
    }
}
