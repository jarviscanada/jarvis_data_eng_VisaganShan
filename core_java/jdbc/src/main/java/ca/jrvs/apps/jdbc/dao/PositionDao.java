package ca.jrvs.apps.jdbc.dao;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.jrvs.apps.jdbc.dto.Position;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {

  private Connection c;
  private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);


  private static final String INSERT = "INSERT INTO position (symbol, number_of_shares, value_paid)"
      + "VALUES (?, ?, ?)";

  private static final String GET_ONE = "SELECT * FROM position WHERE symbol=?";

  private static final String GET_ALL = "SELECT * FROM position";

  private static final String UPDATE = "UPDATE position SET number_of_shares=?, value_paid=?"
      + " WHERE symbol=?";

  private static final String DELETE = "DELETE FROM position WHERE symbol=?";

  private static final String DELETE_ALL = "DELETE FROM position";

  public PositionDao(Connection c) {
    this.c = c;
    BasicConfigurator.configure();

  }

  @Override
  public Position save(Position entity) throws IllegalArgumentException {
    //Check if entity state valid
    if(entity.getTicker() == null){
      throw new IllegalArgumentException("Invalid Symbol, Object can't be saved.");
    } else if(entity.getNumOfShares() < 1){
      throw new IllegalArgumentException("Invalid number of shares, Object can't be saved.");
    } else if(entity.getValuePaid() < 0.01){
      throw new IllegalArgumentException("Invalid price, Object can't be saved.");
    }
    logger.debug("If check: {}", findById(entity.getTicker()).isPresent());
    // if no existing object perform create else update
    if (!findById(entity.getTicker()).isPresent()) {
      //Create
      try(PreparedStatement statement = this.c.prepareStatement(INSERT);) {
        statement.setString(1, entity.getTicker());
        statement.setInt(2, entity.getNumOfShares());
        statement.setDouble(3, entity.getValuePaid());
        statement.execute();

        logger.debug("Create --Executing SQL statement: {}", statement.toString());

        return this.findById(entity.getTicker())
            .orElseThrow(() -> new RuntimeException("Failed to retrieve the created position." ));

      } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to create position.");
      }
    } else {
      //Update
      try(PreparedStatement statement = this.c.prepareStatement(UPDATE);) {
        statement.setInt(1, entity.getNumOfShares());
        statement.setDouble(2, entity.getValuePaid());
        statement.setString(3, entity.getTicker());
        statement.execute();
        logger.debug("Update --Executing SQL statement: {}", statement.toString());

        return this.findById(entity.getTicker())
            .orElseThrow(() -> new RuntimeException("Failed to retrieve the update position." ));

      } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to update position.");
      }

    }
  }

  @Override
  public Optional<Position> findById(String s) throws IllegalArgumentException {
    //Create new position object
    Position pos = new Position();

    // Execute sql statement and store results in result set
    try(PreparedStatement statement = this.c.prepareStatement(GET_ONE);){
      statement.setString(1, s);
      ResultSet rs = statement.executeQuery();
      //retrieve values from result set and store in object
      if(rs.next()){
        pos.setTicker(rs.getString("symbol"));
        pos.setNumOfShares(rs.getInt("number_of_shares"));
        pos.setValuePaid(rs.getDouble("value_paid"));
        return Optional.of(pos);
      } else {
        return Optional.empty();
      }
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Failed to find position by id.");
    }
  }

  @Override
  public Iterable<Position> findAll() {
    // Create List object to store Positions
    ArrayList<Position> posList = new ArrayList<Position>();
    try(PreparedStatement statement = this.c.prepareStatement(GET_ALL);){
      ResultSet rs = statement.executeQuery();
      //retrieve values from result set and store in object
      while(rs.next()){
        //Create a new quote object
        Position pos = new Position();

        //Populate quote object with database data
        pos.setTicker(rs.getString("symbol"));
        pos.setNumOfShares(rs.getInt("number_of_shares"));
        pos.setValuePaid(rs.getDouble("value_paid"));

        posList.add(pos);
      }
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Failed to find positions.");
    }
    return posList;
  }

  @Override
  public void deleteById(String s) throws IllegalArgumentException {
    try(PreparedStatement statement = this.c.prepareStatement(DELETE);){
      statement.setString(1, s);
      statement.execute();
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Failed to delete position by id.");
    }
  }

  @Override
  public void deleteAll() {
    try(PreparedStatement statement = this.c.prepareStatement(DELETE_ALL);){
      statement.execute();
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Failed to delete all positions.");
    }
  }

}
