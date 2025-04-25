package ca.jrvs.apps.jdbc.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseConnectionManager {
  private final String url;
  private final Properties properties;

  public DatabaseConnectionManager(){
    Map<String, String> propertiesMap = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] tokens = line.split(":");
        propertiesMap.put(tokens[0], tokens[1]);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.properties = new Properties();
    this.url = "jdbc:postgresql://"+propertiesMap.get("server")+":"+propertiesMap.get("port")+"/"+propertiesMap.get("database");

    this.properties.setProperty("user", propertiesMap.get("username"));
    this.properties.setProperty("password", propertiesMap.get("password"));
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

}

