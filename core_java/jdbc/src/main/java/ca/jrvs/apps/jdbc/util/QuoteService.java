package ca.jrvs.apps.jdbc.util;

import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.dto.Quote;
import java.util.Optional;

public class QuoteService {

  private QuoteDao dao;
  private QuoteHttpHelper httpHelper;

  /**
   * Fetches latest quote data from endpoint
   *
   * @param ticker
   * @return Latest quote information or empty optional if ticker symbol not found
   */
  public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
    // Create a quote object using data retrieved from API, save quote object to
    // database and return quote
    Quote quote;
    //Initialize values
    dao = new QuoteDao();
    quote = httpHelper.fetchQuoteInfo(ticker);
    //Save object to database using DAO
    if (quote.getTicker() != null) {
      dao.save(quote);
    }
    //Return object
    return Optional.of(quote);
  }
}
