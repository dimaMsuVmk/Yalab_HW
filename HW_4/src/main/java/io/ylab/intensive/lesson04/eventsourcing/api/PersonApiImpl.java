package io.ylab.intensive.lesson04.eventsourcing.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
  private ConnectionFactory connectionFactory;
  private String queue = "queue";
  private String exhangeName = "exc";
  private DataSource dataSource;
  public PersonApiImpl(ConnectionFactory connectionFactory,DataSource dataSource) {
    this.connectionFactory = connectionFactory;
    this.dataSource = dataSource;
  }
  private Channel getChannel(Connection connection) throws IOException {
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(exhangeName, BuiltinExchangeType.TOPIC);
    channel.queueDeclare(queue, true, false, false, null);
    channel.queueBind(queue, exhangeName, "save");
    channel.queueBind(queue, exhangeName, "delete");
    return channel;
  }

  @Override
  public void deletePerson(Long personId) {
    try(Connection connection = connectionFactory.newConnection();
        Channel channel = getChannel(connection)) {
      channel.basicPublish(exhangeName,"delete",null,personId.toString().getBytes(StandardCharsets.UTF_8));

    } catch (IOException | TimeoutException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void savePerson(Long personId, String firstName, String lastName, String middleName)  {
    Person person = new Person(personId,firstName,lastName,middleName);
    ObjectMapper mapper = new ObjectMapper();
    try(Connection connection = connectionFactory.newConnection();
        Channel channel = getChannel(connection)) {
      String json = mapper.writeValueAsString(person);
      channel.basicPublish(exhangeName,"save",null,json.getBytes(StandardCharsets.UTF_8));

    } catch (IOException | TimeoutException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public Person findPerson(Long personId) {
    String selectSql = "SELECT * FROM person WHERE person_id = ?";
    Person person = null;
    try(java.sql.Connection connection = dataSource.getConnection();
        PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
      selectStatement.setLong(1,personId);
      ResultSet resultSet = selectStatement.executeQuery();
      if(resultSet.next()) {
        person = new Person();
        person.setId(resultSet.getLong("person_id"));
        person.setLastName(resultSet.getString("last_name"));
        person.setName(resultSet.getString("first_name"));
        person.setMiddleName(resultSet.getString("middle_name"));
      }
      resultSet.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return person;
  }

  @Override
  public List<Person> findAll() {
    String selectSql = "SELECT * FROM person";
    List<Person> list = new ArrayList<>();
    try(java.sql.Connection connection = dataSource.getConnection();
        Statement selectStatement = connection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery(selectSql)) {
      while (resultSet.next()) {
        Person person = new Person();
        person.setId(resultSet.getLong("person_id"));
        person.setLastName(resultSet.getString("last_name"));
        person.setName(resultSet.getString("first_name"));
        person.setMiddleName(resultSet.getString("middle_name"));
        list.add(person);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return list;
  }
}
