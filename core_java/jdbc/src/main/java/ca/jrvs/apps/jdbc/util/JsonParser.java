package ca.jrvs.apps.jdbc.util;

import ca.jrvs.apps.jdbc.dto.Quote;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonParser {
  /**
   * Convert an object to an input string
   * @param object input object
   * @return JSON string
   * @throw JSONProcessingException
   */
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);


  public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
      throws JsonProcessingException {
    ObjectMapper m = new ObjectMapper();
    if (!includeNullValues) {
      m.setSerializationInclusion(Include.NON_NULL);
    }
    if(prettyJson) {
      m.enable(SerializationFeature.INDENT_OUTPUT);
    }
    return m.writeValueAsString(object);
  }
  /**
   * Parse JSON string to an Object
   * @param json JSON str
   * @return Object
   * @throws java.io.IOException
   */

  public static Quote toObjectFromJson(String json) throws IOException, IllegalArgumentException {
    // Initialize mapper and jsonNode for operations
    ObjectMapper m = new ObjectMapper();
    JsonNode rootNode = m.readTree(json);
    JsonNode globalQuoteNode = rootNode.path("Global Quote");

    // if returned Json String is not Global Quote (ex. root is an error message).
    if (globalQuoteNode.isMissingNode() || globalQuoteNode.isEmpty()){
      logger.error("No data was given by API.");
      return null;
    }

    // Create object to pass information to
    Quote quote = new Quote();

    //Retrieve values and store to new object
    quote.setTicker(globalQuoteNode.get("01. symbol").asText());
    quote.setOpen(globalQuoteNode.get("02. open").asDouble());
    quote.setHigh(globalQuoteNode.get("03. high").asDouble());
    quote.setLow(globalQuoteNode.get("04. low").asDouble());
    quote.setPrice(globalQuoteNode.get("05. price").asDouble());
    quote.setVolume(globalQuoteNode.get("06. volume").asInt());
    String dateStr = globalQuoteNode.get("07. latest trading day").asText();

    try {
      // Parse date string into java.util.Date object
      Date formattedDate = DATE_FORMAT.parse(dateStr);

      // Convert java.util.Date object to java.sql.Date object
      java.sql.Date latestTradingDay = new java.sql.Date(formattedDate.getTime());

      quote.setLatestTradingDay(latestTradingDay);
    } catch (ParseException e) {
      e.printStackTrace();
      throw new RuntimeException("Unable to parse JSON string to object.");
    }

    quote.setPreviousClose(globalQuoteNode.get("08. previous close").asDouble());
    quote.setChange(globalQuoteNode.get("09. change").asDouble());
    quote.setChangePercent(globalQuoteNode.get("10. change percent").asText());

    // Return object
    return quote;
  }
}

