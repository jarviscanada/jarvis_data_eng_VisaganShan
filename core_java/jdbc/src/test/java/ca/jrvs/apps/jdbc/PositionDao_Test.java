package ca.jrvs.apps.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Position;
import ca.jrvs.apps.jdbc.dto.Quote;
import ca.jrvs.apps.jdbc.util.DatabaseConnectionManager;
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

public class PositionDao_Test {
  public static DatabaseConnectionManager dcm;
  public static Connection c;
  public PositionDao positionDao;
  public QuoteDao quoteDao;
  public Quote aaplQuote;
  public Quote tslaQuote;
  public Position aaplPos;
  public Position tslaPos;

  @BeforeAll
  public static void setupDatabaseConnection(){
    dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
    try {
      c = dcm.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Unable to establish database connection.");
    }
  }

  @BeforeEach
  public void init(){
    //Initialize DAOs
    positionDao = new PositionDao(c);
    quoteDao = new QuoteDao(c);

    //Initialize Quote objects
    aaplQuote = new Quote();
    tslaQuote = new Quote();
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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

    // Save quote objects to database in order to create position objects
    quoteDao.save(aaplQuote);
    quoteDao.save(tslaQuote);

    // Initialize Position objects
    aaplPos = new Position();
    aaplPos.setTicker("AAPL");
    aaplPos.setNumOfShares(1);
    aaplPos.setValuePaid(230.54);

    tslaPos = new Position();
    tslaPos.setTicker("TSLA");
    tslaPos.setNumOfShares(2);
    tslaPos.setValuePaid(248.23);

  }

  @Test
  public void testCreate() {
    Position returnAapl = positionDao.save(aaplPos);
    Position returnTsla = positionDao.save(tslaPos);

    //Check to see if object created with values from database is equal to object that passed
    // values.
    assertEquals(aaplPos.getTicker(), returnAapl.getTicker());
    assertEquals(aaplPos.getNumOfShares(), returnAapl.getNumOfShares());
    assertEquals(aaplPos.getValuePaid(), returnAapl.getValuePaid());

    assertEquals(tslaPos.getTicker(), returnTsla.getTicker());
    assertEquals(tslaPos.getNumOfShares(), returnTsla.getNumOfShares());
    assertEquals(tslaPos.getValuePaid(), returnTsla.getValuePaid());
  }

  @Test
  public void testUpdate() {
    //Update the aapl Position object
    aaplPos.setTicker("AAPL");
    aaplPos.setNumOfShares(300);
    aaplPos.setValuePaid(230.55);
    Position updatedObj = positionDao.save(aaplPos);

    //Check to see if object created with values from
    // database is equal to the updated object that passed values.
    assertEquals(updatedObj.getTicker(), aaplPos.getTicker());
    assertEquals(updatedObj.getNumOfShares(), aaplPos.getNumOfShares());
    assertEquals(updatedObj.getValuePaid(), aaplPos.getValuePaid());
  }

  @Test
  public void testReadAll() {
    //Store objects in database
    positionDao.save(aaplPos);
    positionDao.save(tslaPos);

    //Create arraylists
    ArrayList<Position> testList = new ArrayList<Position>();
    ArrayList<Position> iterList = new ArrayList<Position>();

    //Populate test list (expected answer) with objects
    testList.add(aaplPos);
    testList.add(tslaPos);

    // Retrieve Iterable object from findAll method and store objects in an arrayList
    Iterable<Position> returnedList = positionDao.findAll();
    for(Position pos : returnedList){
      iterList.add(pos);
    }
    // Check if size of both arrayLists are equal, if so, compare values of the objects within the lists.
    assertEquals(testList.size(), iterList.size());
    if(testList.size() == iterList.size()){
      for(int i = 0; i < testList.size(); i++){
        Position testListObj = testList.get(i);
        Position iterListObj = iterList.get(i);

        assertEquals(testListObj.getTicker(), iterListObj.getTicker());
        assertEquals(testListObj.getNumOfShares(), iterListObj.getNumOfShares());
        assertEquals(testListObj.getValuePaid(), iterListObj.getValuePaid());
      }
    }
  }

  @Test
  public void testFindById() {
    //Check if object found by Id matches test object.
    Optional <Position> objById = positionDao.findById(aaplPos.getTicker());
    if(objById.isPresent()) {
      Position foundObj = objById.get();
      //Check to see if object created with values from
      // database is equal to the updated object that passed values.
      assertEquals(foundObj.getTicker(), aaplPos.getTicker());
      assertEquals(foundObj.getNumOfShares(), aaplPos.getNumOfShares());
      assertEquals(foundObj.getValuePaid(), aaplPos.getValuePaid());
    } else {
      fail("Position with ticker " + aaplPos.getTicker() + " not found in the database.");
    }
  }
  @Test
  public void testDeleteById() {
    //Save objects to database which will be deleted
    positionDao.save(aaplPos);
    //Delete by id method
    positionDao.deleteById(aaplPos.getTicker());
    //Check to see if object still exists in database
    Optional<Position> doesAaplExists = positionDao.findById(aaplPos.getTicker());
    assertFalse(doesAaplExists.isPresent());

  }

  @Test
  public void testDeleteAll(){
    //Save objects to database which will be deleted
    positionDao.save(aaplPos);
    positionDao.save(tslaPos);
    //Delete all method
    positionDao.deleteAll();
    //Check to see if object still exists in database
    Optional<Position> doesTslaExists = positionDao.findById(tslaPos.getTicker());
    assertFalse(doesTslaExists.isPresent());
  }

  @Test
  public void testErrorHandling() {
    //Create invalid object
    Position errPos = new Position();
    errPos.setNumOfShares(-1);
    errPos.setValuePaid(-100.0);

    assertNull(positionDao.save(errPos));
  }
}
