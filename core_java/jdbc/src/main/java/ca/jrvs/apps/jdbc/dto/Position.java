package ca.jrvs.apps.jdbc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Position {

  private String ticker; //id
  private int numOfShares;
  private double valuePaid; //total amount paid for shares

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public int getNumOfShares() {
    return numOfShares;
  }

  public void setNumOfShares(int numOfShares) {
    this.numOfShares = numOfShares;
  }

  public double getValuePaid() {
    return valuePaid;
  }

  public void setValuePaid(double valuePaid) {
    this.valuePaid = valuePaid;
  }

  @Override
  public String toString() {
    return "Position{" +
        "ticker='" + ticker + '\'' +
        ", numOfShares=" + numOfShares +
        ", valuePaid=" + valuePaid +
        '}';
  }
}
