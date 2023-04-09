package messagefilter;

import javax.sql.DataSource;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("messagefilter")
public class Config {
  private String queueIn = "input";
  private String queueOut = "output";
  private String exhangeName = "exc";
  
  @Bean
  public ConnectionFactory connectionFactory() {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("localhost");
    connectionFactory.setPort(5672);
    connectionFactory.setUsername("guest");
    connectionFactory.setPassword("guest");
    connectionFactory.setVirtualHost("/");
    return connectionFactory;
  }
  
  @Bean
  public DataSource dataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setServerName("localhost");
    dataSource.setUser("postgres");
    dataSource.setPassword("postgres");
    dataSource.setDatabaseName("postgres");
    dataSource.setPortNumber(5432);
    return dataSource;
  }

  @Bean
  @SneakyThrows
  public Connection connection(@Autowired ConnectionFactory connectionFactory) {
    return connectionFactory.newConnection();
  }

  @Bean(name = "channelInput")
  @SneakyThrows
  public Channel channelInput(@Autowired Connection connection){
    Channel channelInput = connection.createChannel();
    channelInput.exchangeDeclare(exhangeName, BuiltinExchangeType.TOPIC);
    channelInput.queueDeclare(queueIn, true, false, false, null);
    channelInput.queueBind(queueOut, exhangeName, "in");
    return channelInput;
  }
  @Bean(name = "channelOutput")
  @SneakyThrows
  public Channel channelOutput(@Autowired Connection connection){
    Channel channelOutput = connection.createChannel();
    channelOutput.exchangeDeclare(exhangeName, BuiltinExchangeType.TOPIC);
    channelOutput.queueDeclare(queueOut, true, false, false, null);
    channelOutput.queueBind(queueOut, exhangeName, "out");
    return channelOutput;
  }
}
