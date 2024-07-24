package ca.jrvs.apps.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dto.Position;
import ca.jrvs.apps.jdbc.dto.Quote;
import ca.jrvs.apps.jdbc.util.PositionService;
import ca.jrvs.apps.jdbc.util.QuoteService;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PositionService_UnitTest {
  // Define mock objects
  @Mock
  private PositionDao dao;

  @Mock
  private QuoteService quoteService;

  // Create the QuoteService object and inject the mock classes into it
  @InjectMocks
  private PositionService service;

  @BeforeEach
  public void setup(){
    // Initialize mocks
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testBuy() {
    String ticker = "AAPL";
    int numOfShares = 2;
    double price = 20.00;

    //Mock behaviour of external dependencies:
//
//    //Mock behaviour of quoteDao save and findById methods
//    when(dao.save(mockQuote)).thenReturn(mockQuote);
//    when(dao.findById(mockQuote.getTicker())).thenReturn(Optional.of(mockQuote));
//
//    //Test the method
//    Optional<Quote> result = service.fetchQuoteDataFromAPI(ticker);
//
//    // Verify interactions
//    verify(httpHelper).fetchQuoteInfo(ticker);
////    verify(dao).save(mockQuote);
////    verify(dao).findById(ticker);
//
//    // Assertion check to see if result object exists and if values are the same
//    assertTrue(result.isPresent());
//    assertEquals(ticker, result.get().getTicker());

  }
}
