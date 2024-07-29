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
  private final PositionDao posDao;
  private final QuoteService qs;

  //Create constructor to establish a connection to the database
  public PositionService(){
    DatabaseConnectionManager dcm = new DatabaseConnectionManager();
    this.qs = new QuoteService();
    try {
      // Establish a connection to the database
      Connection c = dcm.getConnection();
      posDao = new PositionDao(c);
    } catch(SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Position Service unable to establish database connection.");
    }
  }
  public PositionService(PositionDao posDao, QuoteService quoteService){
    this.qs = quoteService;
    this.posDao = posDao;
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

    // Retrieve maximum volume of given ticker using quote service
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
    posDao.save(pos);
    // Pass object to DAO save function which will create/update position in the database.
    return pos;
  }

  /**
   * Sells all shares of the given ticker symbol
   * @param ticker
   */
  public void sell(String ticker) {
    posDao.deleteById(ticker);
  }

}
