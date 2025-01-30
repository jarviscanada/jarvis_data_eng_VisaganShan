package ca.jrvs.apps.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import okhttp3.OkHttpClient;
import java.sql.Connection;
import java.sql.DriverManager;
import ca.jrvs.apps.jdbc.dao.*;
import ca.jrvs.apps.jdbc.util.QuoteService;
import ca.jrvs.apps.jdbc.util.PositionService;
import ca.jrvs.apps.jdbc.util.QuoteHttpHelper;

public class Main {

  public static void main(String[] args) {
    Map<String, String> properties = new HashMap<>();

    String filePath = System.getenv("CONFIG_PATH");
    if (filePath == null || filePath.isEmpty()) {
      filePath = "src/main/resources/properties.txt";
    }

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] tokens = line.split(":");
        properties.put(tokens[0], tokens[1]);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      Class.forName(properties.get("db-class"));
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    OkHttpClient client = new OkHttpClient();
    String url = "jdbc:postgresql://"+properties.get("server")+":"+properties.get("port")+"/"+properties.get("database");
    try (Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
      QuoteDao qRepo = new QuoteDao(c);
      PositionDao pRepo = new PositionDao(c);
      QuoteHttpHelper rcon = new QuoteHttpHelper(properties.get("api-key"), client);
      QuoteService sQuote = new QuoteService(qRepo, rcon);
      PositionService sPos = new PositionService(pRepo, sQuote);
      StockQuoteController con = new StockQuoteController(sQuote, sPos, c);
      con.initClient();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
