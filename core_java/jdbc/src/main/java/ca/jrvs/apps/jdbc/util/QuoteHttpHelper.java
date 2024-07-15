package ca.jrvs.apps.jdbc.util;

import static ca.jrvs.apps.jdbc.util.JsonParser.toObjectFromJson;

import ca.jrvs.apps.jdbc.dto.Quote;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuoteHttpHelper {

  private String apiKey;
  private OkHttpClient client;

  public QuoteHttpHelper(){
    //Initialize state to use http methods.
    apiKey = "NZ3IE12TDMP2ZGUP"; //remove this afterwards and set it up as an env variable
    client = new OkHttpClient();
  }
  /**
   * Fetch latest quote data from Alpha Vantage endpoint
   * @param symbol
   * @return Quote with latest data
   * @throws IllegalArgumentException - if no data was found for the given symbol
   */
  public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
    // Build an api request call
    Request request = new Request.Builder()
        .url("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey + "&datatype=json")
        .header("X-RapidAPI-Key", apiKey)
        .header("X-RapidAPI-Host", "alphavantage.co")
        .build();

    try {
      //Execute request call
      Response response = client.newCall(request).execute();
      String jsonString = response.body().string();

      //Parse data from Json string to an object
//      return toObjectFromJson(jsonString, Quote.class);
      return toObjectFromJson(jsonString);

    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Could not fetch data from API.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}