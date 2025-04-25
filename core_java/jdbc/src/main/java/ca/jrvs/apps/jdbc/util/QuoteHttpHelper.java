package ca.jrvs.apps.jdbc.util;

import static ca.jrvs.apps.jdbc.util.JsonParser.toObjectFromJson;

import ca.jrvs.apps.jdbc.dto.Quote;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteHttpHelper {

  private String apiKey;
  private OkHttpClient client;
  private static final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);

  public QuoteHttpHelper(){
    Map<String, String> properties = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] tokens = line.split(":");
        properties.put(tokens[0], tokens[1]);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.apiKey = properties.get("api-key");
    this.client = new OkHttpClient();
  }

  public QuoteHttpHelper(String apikey, OkHttpClient client){
    //Initialize state to use http methods.
    this.apiKey = apikey;
    this.client = client;
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
    logger.info(request.toString());
    try {
      //Execute request call
      Response response = client.newCall(request).execute();
      String jsonString = response.body().string();

      //Parse data from Json string to an object
      return toObjectFromJson(jsonString);

    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Could not fetch data from API.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}