package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.dto.Quote;
import ca.jrvs.apps.jdbc.util.DatabaseConnectionManager;
import ca.jrvs.apps.jdbc.util.PositionService;
import ca.jrvs.apps.jdbc.util.QuoteHttpHelper;
import ca.jrvs.apps.jdbc.util.QuoteService;
import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Position;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StockQuoteController {

  private QuoteService quoteService;
  private PositionService positionService;
  private QuoteHttpHelper httpHelper;
  private QuoteDao quoteDao;
  private PositionDao posDao;

  public StockQuoteController(QuoteService qs, PositionService ps, Connection c) throws IOException {
    this.quoteService = qs;
    this.positionService = ps;
    quoteDao = new QuoteDao(c);
    posDao = new PositionDao(c);
  }
  /**
   * User interface for our application
   */
  public void initClient() {
    //Initialize scanner to receive user inputs
  Scanner scanner = new Scanner(System.in);
  // Set up while loop for interface options
    while(true){
      System.out.println("Stock Trader");
      System.out.println("------------");
      System.out.println("1. Search for a stock quote");
      System.out.println("2. Buy stock");
      System.out.println("3. Sell stock");
      System.out.println("4. View position");
      System.out.println("5. View all saved stock quotes");
      System.out.println("6. View all positions");
      System.out.println("7. Exit");
      System.out.print("Please make a selection: ");
      String choice = scanner.nextLine();

      // Use switch case to determine flow of code based on user input
      switch(choice) {
        case "1":
          viewStockQuote(scanner);
          break;
        case "2":
          buyStockQuote(scanner);
          break;
        case "3":
          sellStockQuote(scanner);
          break;
        case "4":
          viewPosition(scanner);
          break;
        case "5":
          viewAllQuotes();
          break;
        case "6":
          viewAllPositions();
          break;
        case "7":
          System.out.println("Exiting... Goodbye!");
          scanner.close();
          return;
        default:
          System.out.println("Invalid choice, please try again.");
      }
    }
  }

  public void viewStockQuote(Scanner scanner){
    // Retrieve user input
    System.out.println("Please enter the stock symbol: ");
    String symbol = scanner.nextLine();
    //Retrieve data and output it to user
    try{
      if(quoteService.fetchQuoteDataFromAPI(symbol).isPresent()){
        Quote quote = quoteService.fetchQuoteDataFromAPI(symbol).get();
        System.out.println("\nStock: " + quote.getTicker());
        System.out.println("Open: " + quote.getOpen());
        System.out.println("High: " + quote.getHigh());
        System.out.println("Low: " + quote.getLow());
        System.out.println("Price: " + quote.getPrice());
        System.out.println("Volume: " + quote.getVolume());
        System.out.println("Latest Trading Day: " + quote.getLatestTradingDay());
        System.out.println("Previous close: " + quote.getPreviousClose());
        System.out.println("Change: " + quote.getChange());
        System.out.println("Change Percent: " + quote.getChangePercent() + "\n");
      } else {
        System.out.println("Cannot find stock quote details for \"" + symbol+"\". Please try again.\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to retrieve stock quote.");
    }

  }

  private void buyStockQuote(Scanner scanner) {
    //Retrieve user input
    System.out.println("Please enter the stock symbol to purchase: ");
    String symbol = scanner.nextLine();
    System.out.println("Please enter the number of shares you would like to purchase: ");
    int shares = Integer.parseInt(scanner.nextLine());

    //Buy stock
    try{
      positionService.buy(symbol, shares);
      if(posDao.findById(symbol).isPresent()){
        System.out.println("Successfully bought " + shares + " shares of " + symbol + "!\n");
      } else {
        System.out.println("Cannot purchase shares of \"" + symbol + "\". Please try again.\n");
      }
    } catch (Exception e){
      e.printStackTrace();
      throw new RuntimeException("Failed to buy stock");
    }
  }

  private void sellStockQuote(Scanner scanner){
    System.out.println("Please enter the stock symbol to sell: ");
    String symbol = scanner.nextLine();

    //sell stock
    if(posDao.findById(symbol).isPresent()){
      try{
        // Calculate profit / loss
        httpHelper = new QuoteHttpHelper();
        double currPrice = httpHelper.fetchQuoteInfo(symbol).getPrice();
        Position position = posDao.findById(symbol).get();
        double profits = (position.getValuePaid() - currPrice)*position.getNumOfShares();

        // Sell position
        System.out.println("Selling " + position.getNumOfShares() + " shares of " + symbol + "@ $" + currPrice);
        positionService.sell(symbol);
        System.out.println("Sold stock(s) for a return of " + profits + "dollars.");
      } catch (Exception e){
        e.printStackTrace();
        throw new RuntimeException("Failed to sell stock");
      }
    } else {
      System.out.println("No current positions found for \"" + symbol + "\". Please try again.\n");
    }
  }

  private void viewPosition(Scanner scanner){
    System.out.println("Please enter the stock symbol of the position: ");
    String symbol = scanner.nextLine();

    if(posDao.findById(symbol).isPresent()){
      Position position = posDao.findById(symbol).get();
      System.out.println("\nSymbol: " + position.getTicker());
      System.out.println("Number of Shares: " + position.getNumOfShares());
      System.out.println("Price: " + position.getValuePaid());
    } else {
      System.out.println("\"" + symbol + "\" not found. Please try again\n");
    }
  }

  private void viewAllQuotes(){
    Iterable<Quote> quotes = quoteDao.findAll();
    for(Quote quote: quotes){
      System.out.println("\nStock: " + quote.getTicker());
      System.out.println("Open: " + quote.getOpen());
      System.out.println("High: " + quote.getHigh());
      System.out.println("Low: " + quote.getLow());
      System.out.println("Price: " + quote.getPrice());
      System.out.println("Volume: " + quote.getVolume());
      System.out.println("Latest Trading Day: " + quote.getLatestTradingDay());
      System.out.println("Previous close: " + quote.getPreviousClose());
      System.out.println("Change: " + quote.getChange());
      System.out.println("Change Percent: " + quote.getChangePercent());
      System.out.println("--------------\n");
    }
  }

  private void viewAllPositions(){
    Iterable<Position> positions = posDao.findAll();
    for(Position pos: positions){
      System.out.println("\nSymbol: " + pos.getTicker());
      System.out.println("Number of Shares: " + pos.getNumOfShares());
      System.out.println("Price: " + pos.getValuePaid());
      System.out.println("--------------\n");
    }
  }
}
