package ca.jrvs.apps.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import ca.jrvs.apps.jdbc.util.DatabaseConnectionManager;
import java.sql.Connection;
import ca.jrvs.apps.jdbc.util.PositionService;
import ca.jrvs.apps.jdbc.dto.Position;
import ca.jrvs.apps.jdbc.dao.PositionDao;
import java.sql.SQLException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PositionService_IntTest {
  public PositionService service;
  public PositionDao dao;
  public Position pos;
  public static DatabaseConnectionManager dcm;
  public static Connection c;


  @BeforeAll
  public static void setupDatabaseConnection(){
    dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
    try {
      c = dcm.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Unable to establish database connection.");
    }
  }

  @BeforeEach
  public void init(){
    dao = new PositionDao(c);
    service = new PositionService();
    pos = new Position();
    pos.setTicker("AAPL");
    pos.setNumOfShares(2);
    pos.setValuePaid(30);
  }

  @Test
  public void testBuy(){

    Position retObj = service.buy(pos.getTicker(), pos.getNumOfShares(), pos.getValuePaid());

    assertEquals(pos.getTicker(), retObj.getTicker());
    assertEquals(pos.getNumOfShares(), retObj.getNumOfShares());
    assertEquals(pos.getValuePaid(), retObj.getValuePaid());

  }

  @Test
  public void testSell(){
    // Ensure that there is a record in the database to sell
    Position retObj = service.buy(pos.getTicker(), pos.getNumOfShares(), pos.getValuePaid());
    //Sell position
    service.sell(pos.getTicker());
    //Check if record remains in database
    Optional<Position> doesPosExist = dao.findById(pos.getTicker());
    assertFalse(doesPosExist.isPresent());
  }

}
