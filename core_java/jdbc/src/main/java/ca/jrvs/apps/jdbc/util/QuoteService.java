package ca.jrvs.apps.jdbc.util;

import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Quote;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import okhttp3.OkHttpClient;

public class QuoteService {

  private final QuoteDao dao;
  private final QuoteHttpHelper httpHelper;

  public QuoteService(){
    // Establish a connection to the database and initialize httpHelper
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "stock_quote", "postgres", "password");
    httpHelper = new QuoteHttpHelper();
    try {
      Connection c = dcm.getConnection();
      dao = new QuoteDao(c);
    } catch(SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Quote Service unable to establish database connection.");
    }
  }
  public QuoteService(QuoteDao quoteDao, QuoteHttpHelper httpHelper){
    // Establish a connection to the database and initialize httpHelper
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "stock_quote", "postgres", "password");
    this.httpHelper = httpHelper;
    try {
      Connection c = dcm.getConnection();
      dao = quoteDao;
    } catch(SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Quote Service unable to establish database connection.");
    }
  }
  /**
   * Fetches latest quote data from endp oint
   *
   * @param ticker
   * @return Latest quote information or empty optional if ticker symbol not found
   */
  public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
    // Create a quote object using data retrieved from API
    Quote quote = httpHelper.fetchQuoteInfo(ticker);
    //Save object to database using DAO
    if (quote.getTicker() != null) {
      dao.save(quote);
      return dao.findById(quote.getTicker());
    } else {
      throw new IllegalArgumentException("Quote Service - No Quote Data found.");
    }
  }
}
