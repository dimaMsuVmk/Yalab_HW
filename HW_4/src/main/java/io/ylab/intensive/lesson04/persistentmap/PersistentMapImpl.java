package io.ylab.intensive.lesson04.persistentmap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * Класс, методы которого надо реализовать 
 */
public class PersistentMapImpl implements PersistentMap {
  
  private final DataSource dataSource;
  private String name;

  public PersistentMapImpl(DataSource dataSource) {

    this.dataSource = dataSource;
  }

  @Override
  public void init(String name) {
    String sql = "SELECT * FROM persistent_map WHERE map_name = \'" + name + "\'";
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(sql);
      if(resultSet.next()) throw new RuntimeException("Map with name " + name + " is EXIST!");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    this.name = name;
  }

  private void checkName(){
    if(this.name == null) throw new RuntimeException("Map without name cannot support any operations! Please,set name and try again");
  }

  @Override
  public boolean containsKey(String key) throws SQLException {
    checkName();
    String sql = "SELECT * FROM persistent_map WHERE map_name = \'" + name + "\' AND key = \'" + key +"\'";
    boolean containsKey;
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(sql);
      containsKey = resultSet.next();
    }
    return containsKey;
  }

  @Override
  public List<String> getKeys() throws SQLException {
    checkName();
    String sql = "SELECT key FROM persistent_map WHERE map_name = \'" + name + "\'";
    List<String> getKeys = new ArrayList<>();
    ResultSet resultSet = null;
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      resultSet = statement.executeQuery(sql);
      while (resultSet.next()){
        getKeys.add(resultSet.getString("key"));
      }
    }
    return getKeys;
  }

  @Override
  public String get(String key) throws SQLException {
    checkName();
    String sql = "SELECT value FROM persistent_map WHERE map_name = \'" + name + "\' AND key = \'" + key + "\'";
    String getValue = null;
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(sql);
      resultSet.next();
      getValue = resultSet.getString("value");
    }
    return getValue;
  }

  @Override
  public void remove(String key) throws SQLException {
    checkName();
    String sql = "DELETE FROM persistent_map WHERE map_name = \'" + name + "\' AND key = \'" + key + "\'";
    String getValue = null;
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(sql);
    }
  }

  @Override
  public void put(String key, String value) throws SQLException {
    checkName();
    String sqlGet = "SELECT value FROM persistent_map WHERE map_name = \'" + name + "\' AND key = \'" + key + "\'";
    String sql = "INSERT INTO persistent_map(map_name,key,value) VALUES (\'"+ name + "\', \'" + key + "\', \'" + value + "\')";
    String sqlUpdate = "UPDATE persistent_map "
                     + "SET value = " + "\'" + value + "\'"
                     + "WHERE map_name = \'" + name + "\' AND key = \'" + key + "\'";
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(sqlGet);
      if(!resultSet.next()) statement.executeUpdate(sql);//если в БД НЕ было этого ключа
      else statement.executeUpdate(sqlUpdate); //если ключ был - обновить значение
    }
  }

  @Override
  public void clear() throws SQLException {
    checkName();
    String sql = "DELETE FROM persistent_map "
               + "WHERE map_name = \'" + name + "\'";
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(sql);
    }
  }
}
