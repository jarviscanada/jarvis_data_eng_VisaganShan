package ca.jrvs.apps.jdbc.util;

import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dto.Position;
import ca.jrvs.apps.jdbc.dto.Quote;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class PositionService {

  private PositionDao dao;

  /**
   * Processes a buy order and updates the database accordingly
   * @param ticker
   * @param numberOfShares
   * @param price
   * @return The position in our database after processing the buy
   */
  public Position buy(String ticker, int numberOfShares, double price) {
    // Set up connection to database
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "stock_quote", "postgres", "password");

    // Create position object to pass to database
    Position pos = new Position();
    pos.setTicker(ticker);
    pos.setNumOfShares(numberOfShares);
    pos.setValuePaid(price);

    // Create quote object to retrieve maximum volume of given ticker
    QuoteService qs = new QuoteService();
    Optional<Quote> optionalQuote = qs.fetchQuoteDataFromAPI(ticker);

    //Check if value is not null, retrieve Quote object from Optional
    if (optionalQuote.isPresent()){
      Quote quote = optionalQuote.get();
      //Compare max volume of stock to number of shares bought.
      if(numberOfShares > quote.getVolume()){
        throw new RuntimeException("Number of Shares being purchased exceeds max volume");
      }
    }

      //Store position in database
    try {
      // Establish a connection to the database
      Connection connection = dcm.getConnection();
      dao = new PositionDao();
      dao.setConnection(connection);

      // Pass object to DAO save function which will create/update position in the database.
      dao.save(pos);

      return dao.findById(pos.getTicker())
          .orElseThrow(() -> new RuntimeException("Failed to retrieve the created position." ));

    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Failed to process buying of order.");
    }
  }

  /**
   * Sells all shares of the given ticker symbol
   * @param ticker
   */
  public void sell(String ticker) {
    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "stock_quote", "postgres", "password");

    try {
      // Establish a connection to the database
      Connection connection = dcm.getConnection();
      PositionDao posDao = new PositionDao();
      posDao.setConnection(connection);

      // Pass id to DAO deleteById function which will delete position in the database.
      posDao.deleteById(ticker);

    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Failed to process selling of order.");
    }
  }

}
