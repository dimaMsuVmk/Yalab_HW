package io.ylab.intensive.lesson04.eventsourcing.db;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;

public class DbApp {
  private static String queue = "queue";
  private static String exhangeName = "exc";
  private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
  public static void main(String[] args) throws Exception {
    DataSource dataSource = initDb();
    ConnectionFactory connectionFactory = initMQ();
    // тут пишем создание и запуск приложения работы с БД
    DataProcessor dataProcessor = new DataProcessor();
    dataProcessor.process(dataSource,connectionFactory);
  }
  
  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
  
  private static DataSource initDb() throws SQLException {
    String ddl = "" 
                     + "drop table if exists person; "
                     + "create table if not exists person (\n"
                     + "person_id bigint primary key,\n"
                     + "first_name varchar,\n"
                     + "last_name varchar,\n"
                     + "middle_name varchar\n"
                     + ")";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(ddl, dataSource);
    return dataSource;
  }
}
