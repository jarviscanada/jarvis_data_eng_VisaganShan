package ca.jrvs.apps.jdbc.util;

import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dto.Position;
import ca.jrvs.apps.jdbc.dto.Quote;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class PositionService {

  private static final Logger logger = LoggerFactory.getLogger(PositionService.class);
  private PositionDao dao;


  //Create constructor to establish a connection to the database
  public PositionService(){
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "stock_quote", "postgres", "password");
    try {
      // Establish a connection to the database
      Connection connection = dcm.getConnection();
      dao = new PositionDao(connection);
    } catch(SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Position Service unable to establish database connection.");
    }
  }
  public PositionService(PositionDao posDao){
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "stock_quote", "postgres", "password");
    try {
      // Establish a connection to the database
      Connection connection = dcm.getConnection();
      this.dao = posDao;
    } catch(SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Position Service unable to establish database connection.");
    }
  }
  /**
   * Processes a buy order and updates the database accordingly
   * @param ticker
   * @param numberOfShares
   * @return The position in our database after processing the buy
   */
  public Position buy(String ticker, int numberOfShares) {
    // Create position object to pass to database
    Position pos = new Position();
    pos.setTicker(ticker);
    pos.setNumOfShares(numberOfShares);

    // Create quote object to retrieve maximum volume of given ticker
    QuoteService qs = new QuoteService();
    Optional<Quote> optionalQuote = qs.fetchQuoteDataFromAPI(ticker);

    //Check if value is not null, retrieve Quote object from Optional
    if (optionalQuote.isPresent()){
      Quote quote = optionalQuote.get();
      pos.setValuePaid(quote.getPrice());
      //Compare max volume of stock to number of shares bought.
      if(numberOfShares > quote.getVolume()){
        logger.error("Number of Shares being purchased exceeds max volume");
      }
    }

    // Pass object to DAO save function which will create/update position in the database.
    dao.save(pos);
    if(dao.findById(pos.getTicker()).isPresent()){
      return dao.findById(pos.getTicker()).get();
    } else {
      return null;
    }
  }

  /**
   * Sells all shares of the given ticker symbol
   * @param ticker
   */
  public void sell(String ticker) {
    dao.deleteById(ticker);
  }

}
