package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.PositionService;
import ca.jrvs.apps.jdbc.util.QuoteService;
import java.util.Scanner;

public class StockQuoteController {

  private QuoteService quoteService;
  private PositionService positionService;


  public StockQuoteController(QuoteService qs, PositionService ps){
    this.quoteService = qs;
    this.positionService = ps;
  }
  /**
   * User interface for our application
   */
  public void initClient() {
    System.out.println("");
  }

}
