package ca.jrvs.apps.jdbc.util;

import java.io.IOException;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
  private final String url;
  private final Properties properties;
  private static final String CONFIG_FILE = "config.properties";

  public DatabaseConnectionManager() throws IOException {
    this.properties = new Properties();
    loadProperties(CONFIG_FILE);
    String host = properties.getProperty("server");
    String databaseName = properties.getProperty("database");
    String username = properties.getProperty("username");
    String password = properties.getProperty("password");


    this.url = "jdbc:postgresql://"+host+"/"+databaseName;
    this.properties.setProperty("user", username);
    this.properties.setProperty("password", password);
  }

  public DatabaseConnectionManager(String host, String databaseName,
      String username, String password){
    this.properties = new Properties();
    this.url = "jdbc:postgresql://"+host+"/"+databaseName;
    this.properties.setProperty("user", username);
    this.properties.setProperty("password", password);
  }

  public Connection getConnection() throws SQLException{
    return DriverManager.getConnection(this.url, this.properties);
  }

  private void loadProperties(String filename) throws IOException {
    // Loads file into Properties object
    try(FileInputStream fs = new FileInputStream(filename)) {
      properties.load(fs);
    }
  }

}

