package ca.jrvs.apps.jdbc;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
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
  private PositionDao posDao;

  @Mock
  private QuoteService qs;

  // Create the PositionService object and inject the mock classes into it
  @InjectMocks
  private PositionService service;

  @BeforeEach
  public void setup() {
    // Initialize mocks
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testBuy() {
    String ticker = "AAPL";
    int numOfShares = 2;
    int volume = 3;
    double price = 1.00;

    // Create quote and position objects for testing
    Quote mockQuote = new Quote();
    mockQuote.setTicker(ticker);
    mockQuote.setVolume(volume);
    mockQuote.setPrice(price);

    Position mockPos = new Position();
    mockPos.setTicker(ticker);
    mockPos.setNumOfShares(numOfShares);
    mockPos.setValuePaid(price);

    //Mock behaviour of external dependencies:
    //Mock behaviour of quoteDao save and findById methods
    when(qs.fetchQuoteDataFromAPI(ticker)).thenReturn(Optional.of(mockQuote));
    when(posDao.findById(ticker)).thenReturn(Optional.empty());
    when(posDao.save(mockPos)).thenReturn(mockPos);

    //Test the method
    Position result = service.buy(ticker, numOfShares);

    // Verify interactions
    verify(qs).fetchQuoteDataFromAPI(ticker);
    verify(posDao).save(any(Position.class));

    // Assertion check to see if result object exists and if values are the same
    assertEquals(mockPos.getTicker(), result.getTicker());
    assertEquals(mockPos.getNumOfShares(), result.getNumOfShares());

  }

  @Test
  public void testSell(){
    String ticker = "AAPL";

    doNothing().when(posDao).deleteById(ticker);

    service.sell(ticker);

    verify(posDao).deleteById(ticker);

  }
}
