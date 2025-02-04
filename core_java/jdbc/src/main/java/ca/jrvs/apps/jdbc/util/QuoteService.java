package ca.jrvs.apps.jdbc.util;

import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Quote;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteService {

  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);
  private final QuoteDao dao;
  private final QuoteHttpHelper httpHelper;

  public QuoteService(){
    // Establish a connection to the database and initialize httpHelper
    DatabaseConnectionManager dcm = new DatabaseConnectionManager();
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
    DatabaseConnectionManager dcm = new DatabaseConnectionManager();
    this.httpHelper = httpHelper;
    this.dao = quoteDao;

  }
  /**
   * Fetches latest quote data from endpoint
   *
   * @param ticker
   * @return Latest quote information or empty optional if ticker symbol not found
   */
  public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
    // Create a quote object using data retrieved from API
    Quote quote = httpHelper.fetchQuoteInfo(ticker);
    //Save object to database using DAO
    if (quote != null) {
      dao.save(quote);
      return dao.findById(quote.getTicker());
    } else {
      logger.error("Quote Service - No Quote Data found.");
      return Optional.empty();
    }
  }

  public void removeStockQuote(String ticker){
    //Clean up unecessary remaining stock quotes when a position is sold
    dao.deleteById(ticker);
  }
}
