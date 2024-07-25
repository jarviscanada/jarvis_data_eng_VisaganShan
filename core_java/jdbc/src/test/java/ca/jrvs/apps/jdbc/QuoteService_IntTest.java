package ca.jrvs.apps.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import ca.jrvs.apps.jdbc.util.QuoteService;
import ca.jrvs.apps.jdbc.dto.Quote;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.util.QuoteHttpHelper;
import java.util.Optional;

public class QuoteService_IntTest {
  // Define mock objects
  @Mock
  private QuoteDao dao;

  @Mock
  private QuoteHttpHelper httpHelper;

  @Mock
  private Quote mockQuote;
  // Create the QuoteService object and inject the mock classes into it
  @InjectMocks
  private QuoteService service;

  @BeforeEach
  public void setup(){
    // Initialize mocks
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testFetchQuoteServiceFromApi(){
    String ticker = "AAPL";
    mockQuote = new Quote();
    mockQuote.setTicker(ticker);

    //Mock behaviour of external dependencies:
    //Mock behaviour of QuoteHttpHelper
    when(httpHelper.fetchQuoteInfo(ticker)).thenReturn(mockQuote);

    //Mock behaviour of quoteDao save and findById methods
    when(dao.save(mockQuote)).thenReturn(mockQuote);
    when(dao.findById(mockQuote.getTicker())).thenReturn(Optional.of(mockQuote));

    //Test the method
    Optional<Quote> result = service.fetchQuoteDataFromAPI(ticker);

    // Verify interactions
    verify(httpHelper).fetchQuoteInfo(ticker);
//    verify(dao).save(mockQuote);
//    verify(dao).findById(ticker);


    // Assertion check to see if result object exists and if values are the same
    assertTrue(result.isPresent());
    assertEquals(ticker, result.get().getTicker());
  }

}
