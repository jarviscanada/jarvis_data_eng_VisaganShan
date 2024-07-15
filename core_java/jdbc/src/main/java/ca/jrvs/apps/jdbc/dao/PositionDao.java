package ca.jrvs.apps.jdbc.dao;

import ca.jrvs.apps.jdbc.dto.Position;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {

  private Connection c;

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
  }

  @Override
  public Position save(Position entity) throws IllegalArgumentException {
    // if no existing object perform create else update
    if (findById(entity.getTicker()).isEmpty()) {
      //Create
      try(PreparedStatement statement = this.c.prepareStatement(INSERT);) {
        statement.setString(1, entity.getTicker());
        statement.setDouble(2, entity.getNumOfShares());
        statement.setDouble(3, entity.getValuePaid());
        statement.execute();
        return this.findById(entity.getTicker())
            .orElseThrow(() -> new RuntimeException("Failed to retrieve the created position." ));

      } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to create position.");
      }
    } else {
      //Update
      try(PreparedStatement statement = this.c.prepareStatement(UPDATE);) {
        statement.setDouble(1, entity.getNumOfShares());
        statement.setDouble(2, entity.getValuePaid());
        statement.setString(3, entity.getTicker());

        statement.execute();
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
      while(rs.next()){
        pos.setTicker(rs.getString("symbol"));
        pos.setNumOfShares(rs.getInt("num_of_shares"));
        pos.setValuePaid(rs.getDouble("value_paid"));
      }
    }catch (SQLException e){
      e.printStackTrace();
      throw new RuntimeException("Failed to find position by id.");
    }
    return Optional.of(pos);  }

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
        pos.setNumOfShares(rs.getInt("num_of_shares"));
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
