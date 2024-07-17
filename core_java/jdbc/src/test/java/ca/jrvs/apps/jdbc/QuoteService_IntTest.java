package ca.jrvs.apps.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import ca.jrvs.apps.jdbc.util.DatabaseConnectionManager;
import ca.jrvs.apps.jdbc.util.QuoteService;
import ca.jrvs.apps.jdbc.dto.Quote;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.util.QuoteHttpHelper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuoteService_IntTest {
  public QuoteService service;
  public QuoteDao dao;
  public static DatabaseConnectionManager dcm;
  public static Connection c;


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
    dao = new QuoteDao(c);
  }

  @Test
  public void testApiCall(){
    QuoteHttpHelper apiHelper = new QuoteHttpHelper();
    Quote retObj = apiHelper.fetchQuoteInfo("AAPL");

    assertEquals("AAPL", retObj.getTicker());
    assertNotNull(retObj.getOpen());
    assertNotNull(retObj.getLow());
    assertNotNull(retObj.getPrice());
    assertNotNull(retObj.getVolume());
    assertNotNull(retObj.getLatestTradingDay());
    assertNotNull(retObj.getPreviousClose());
    assertNotNull(retObj.getChange());
    assertNotNull(retObj.getChangePercent());
    assertNull(retObj.getTimestamp());
  }

  @Test
  public void testFetchQuoteServiceFromApi(){
    QuoteService service = new QuoteService();
    Optional<Quote> serviceObj = service.fetchQuoteDataFromAPI("AAPL");
    Quote expectedQuote = dao.findById("AAPL").get();
    if (serviceObj.isPresent()){
      Quote actualQuote = serviceObj.get();

      assertEquals(expectedQuote.getTicker(), actualQuote.getTicker());
      assertEquals(expectedQuote.getOpen(), actualQuote.getOpen());
      assertEquals(expectedQuote.getHigh(), actualQuote.getHigh());
      assertEquals(expectedQuote.getLow(), actualQuote.getLow());
      assertEquals(expectedQuote.getPrice(), actualQuote.getPrice());
      assertEquals(expectedQuote.getVolume(), actualQuote.getVolume());
      assertEquals(expectedQuote.getLatestTradingDay(), actualQuote.getLatestTradingDay());
      assertEquals(expectedQuote.getPreviousClose(), actualQuote.getPreviousClose());
      assertEquals(expectedQuote.getChange(), actualQuote.getChange());
      assertEquals(expectedQuote.getChangePercent(), actualQuote.getChangePercent());
    } else {
      throw new RuntimeException("testFetchQuoteServiceFromApi - Object"
          + " returned from api call is not present.");
    }
  }

}
