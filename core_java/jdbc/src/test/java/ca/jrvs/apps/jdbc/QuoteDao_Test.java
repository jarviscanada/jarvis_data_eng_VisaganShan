package ca.jrvs.apps.jdbc;

import static org.junit.jupiter.api.Assertions.*;


import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Quote;
import ca.jrvs.apps.jdbc.util.DatabaseConnectionManager;
import ca.jrvs.apps.jdbc.util.QuoteHttpHelper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuoteDao_Test {

  public static DatabaseConnectionManager dcm;
  public static Connection c;

  public QuoteDao dao;
  Quote aaplQuote;
  Quote tslaQuote;


  @BeforeAll
  public static void setupDatabaseConnection() throws IOException {
      dcm = new DatabaseConnectionManager();

    try {
      c = dcm.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Unable to establish database connection.");
    }
  }

  @BeforeEach
  public void init(){
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    dao = new QuoteDao(c);
    aaplQuote = new Quote();
    tslaQuote = new Quote();

    aaplQuote.setTicker("AAPL");
    aaplQuote.setOpen(228.9200);
    aaplQuote.setHigh(232.6400);
    aaplQuote.setLow(228.6800);
    aaplQuote.setPrice(230.5400);
    aaplQuote.setVolume(53046527);

    //Parse date from string to Date object and store it in quote class
    try {
      // Parse date string into Date java.util.Date object
      Date formattedDate = DATE_FORMAT.parse("2024-07-12");

      // Convert java.util.Date object to Java.sql.Date object
      java.sql.Date latestTradingDay = new java.sql.Date(formattedDate.getTime());

      aaplQuote.setLatestTradingDay(latestTradingDay);
    } catch (ParseException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }

    aaplQuote.setPreviousClose(227.5700);
    aaplQuote.setChange(2.9700);
    aaplQuote.setChangePercent("1.3051%");

    tslaQuote.setTicker("TSLA");
    tslaQuote.setOpen(255.9700);
    tslaQuote.setHigh(265.6000);
    tslaQuote.setLow(251.7300);
    tslaQuote.setPrice(252.6400);
    tslaQuote.setVolume(146912920);

    //Parse date from string to Date object and store it in quote class
    try {
      // Parse date string into Date java.util.Date object
      Date formattedDate = DATE_FORMAT.parse("2024-07-15");

      // Convert java.util.Date object to Java.sql.Date object
      java.sql.Date latestTradingDay = new java.sql.Date(formattedDate.getTime());

      tslaQuote.setLatestTradingDay(latestTradingDay);
    } catch (ParseException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }

    tslaQuote.setPreviousClose(248.2300);
    tslaQuote.setChange(4.4100);
    tslaQuote.setChangePercent("1.7766%");

  }

  @Test
  public void testCreate() {
    Quote fetchedAaplQuote = dao.save(aaplQuote);
    Quote fetchedTslaQuote = dao.save(tslaQuote);

    //Check to see if object created with values from database is equal to object that passed
    // values.
    assertEquals(aaplQuote.getTicker(), fetchedAaplQuote.getTicker());
    assertEquals(aaplQuote.getOpen(), fetchedAaplQuote.getOpen());
    assertEquals(aaplQuote.getHigh(), fetchedAaplQuote.getHigh());
    assertEquals(aaplQuote.getLow(), fetchedAaplQuote.getLow());
    assertEquals(aaplQuote.getPrice(), fetchedAaplQuote.getPrice());
    assertEquals(aaplQuote.getVolume(), fetchedAaplQuote.getVolume());
    assertEquals(aaplQuote.getLatestTradingDay(), fetchedAaplQuote.getLatestTradingDay());
    assertEquals(aaplQuote.getPreviousClose(), fetchedAaplQuote.getPreviousClose());
    assertEquals(aaplQuote.getChange(), fetchedAaplQuote.getChange());
    assertEquals(aaplQuote.getChangePercent(), fetchedAaplQuote.getChangePercent());
    assertNotNull(fetchedAaplQuote.getTimestamp());

    assertEquals(tslaQuote.getTicker(), fetchedTslaQuote.getTicker());
    assertEquals(tslaQuote.getOpen(), fetchedTslaQuote.getOpen());
    assertEquals(tslaQuote.getHigh(), fetchedTslaQuote.getHigh());
    assertEquals(tslaQuote.getLow(), fetchedTslaQuote.getLow());
    assertEquals(tslaQuote.getPrice(), fetchedTslaQuote.getPrice());
    assertEquals(tslaQuote.getVolume(), fetchedTslaQuote.getVolume());
    assertEquals(tslaQuote.getLatestTradingDay(), fetchedTslaQuote.getLatestTradingDay());
    assertEquals(tslaQuote.getPreviousClose(), fetchedTslaQuote.getPreviousClose());
    assertEquals(tslaQuote.getChange(), fetchedTslaQuote.getChange());
    assertEquals(tslaQuote.getChangePercent(), fetchedTslaQuote.getChangePercent());
    assertNotNull(fetchedTslaQuote.getTimestamp());
  }
  @Test
  public void testUpdate() {
    //Update the aapl Quote object
    aaplQuote.setVolume(30000);
    tslaQuote.setHigh(1000);
    Quote fetchedAaplQuote = dao.save(aaplQuote);
    Quote fetchedTslaQuote = dao.save(tslaQuote);

    //Check to see if object created with values from
    // database is equal to the updated object that passed values.
    assertEquals(30000, fetchedAaplQuote.getVolume());
    assertEquals(1000,fetchedTslaQuote.getHigh());
  }

  @Test
  public void testReadAll() {
    //Retrieve updated objects and store them in objects
    //If I didn't do this, assertEquals would fail because timestamps dont match
    Quote updatedAapl = dao.save(aaplQuote);
    Quote updatedTsla = dao.save(tslaQuote);

    //Create list and add objects to list
    ArrayList<Quote> quoteList = new ArrayList<>();
    quoteList.add(updatedAapl);
    quoteList.add(updatedTsla);

    //Get results of find all method
    Iterable<Quote> returnedIterable = dao.findAll();

    //Create an arraylist that will add objects from Iterable object using for each loop
    ArrayList<Quote> iterableList = new ArrayList<>();
     for (Quote obj : returnedIterable){
       iterableList.add(obj);
     }

     // Loop through arraylist and compare each object's state individually.
    assertEquals(quoteList.size(), iterableList.size());
     if(quoteList.size() == iterableList.size()){
       for(int i = 0; i < quoteList.size(); i++){
         // Access data from objects and compare
         Quote quoteListObj = quoteList.get(i);
         Quote iterableListObj = iterableList.get(i);

         assertEquals(quoteListObj.getTicker(), iterableListObj.getTicker());
         assertEquals(quoteListObj.getOpen(), iterableListObj.getOpen());
         assertEquals(quoteListObj.getHigh(), iterableListObj.getHigh());
         assertEquals(quoteListObj.getLow(), iterableListObj.getLow());
         assertEquals(quoteListObj.getPrice(), iterableListObj.getPrice());
         assertEquals(quoteListObj.getVolume(), iterableListObj.getVolume());
         assertEquals(quoteListObj.getLatestTradingDay(), iterableListObj.getLatestTradingDay());
         assertEquals(quoteListObj.getPreviousClose(), iterableListObj.getPreviousClose());
         assertEquals(quoteListObj.getChange(), iterableListObj.getChange());
         assertEquals(quoteListObj.getChangePercent(), iterableListObj.getChangePercent());
         assertEquals(quoteListObj.getTimestamp(), iterableListObj.getTimestamp());
       }
     }
  }

  @Test
  public void testFindById() {
    //Check if object found by Id matches test object.
    dao.save(aaplQuote);
    Optional <Quote> objById = dao.findById(aaplQuote.getTicker());
    if(objById.isPresent()) {
      Quote foundObj = objById.get();
      //Check to see if object created with values from
      // database is equal to the updated object that passed values.
      assertEquals(aaplQuote.getTicker(), foundObj.getTicker());
      assertEquals(aaplQuote.getOpen(), foundObj.getOpen());
      assertEquals(aaplQuote.getHigh(), foundObj.getHigh());
      assertEquals(aaplQuote.getLow(), foundObj.getLow());
      assertEquals(aaplQuote.getPrice(), foundObj.getPrice());
      assertEquals(aaplQuote.getVolume(), foundObj.getVolume());
      assertEquals(aaplQuote.getLatestTradingDay(), foundObj.getLatestTradingDay());
      assertEquals(aaplQuote.getPreviousClose(), foundObj.getPreviousClose());
      assertEquals(aaplQuote.getChange(), foundObj.getChange());
      assertEquals(aaplQuote.getChangePercent(), foundObj.getChangePercent());
      assertNotNull(foundObj.getTimestamp());
    } else {
      fail("Quote with ticker " + aaplQuote.getTicker() + " not found in the database.");
    }
  }

  @Test
  public void testDeleteById() {
    dao.save(aaplQuote);
    dao.deleteById(aaplQuote.getTicker());
    //Check to see if object still exists in database
    Optional<Quote> doesAaplExists = dao.findById(aaplQuote.getTicker());
    assertFalse(doesAaplExists.isPresent());
  }

  @Test
  public void testErrorHandling() {
    //Create invalid object
    Quote errPos = new Quote();
    assertThrows(IllegalArgumentException.class, () -> dao.save(errPos));
  }
}

