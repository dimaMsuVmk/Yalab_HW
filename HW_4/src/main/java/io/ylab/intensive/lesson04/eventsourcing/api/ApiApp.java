package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;

import javax.sql.DataSource;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    ConnectionFactory connectionFactory = initMQ();
    DataSource dataSource = DbUtil.buildDataSource();
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    PersonApi personApi = new PersonApiImpl(connectionFactory,dataSource);
    personApi.savePerson(1L,"Ivan","Ivanov","Ivanovich");
    personApi.savePerson(2L,"Foma","Fomov","Fomovich");
    personApi.savePerson(3L,"Lena","Lena","Lena");
    personApi.savePerson(4L,"Lena","Lena","Lena");
    personApi.savePerson(4L,"Lena","Lena","Lena");
//    Thread.sleep(3000);
//    System.out.println(personApi.findPerson(2L));
//    personApi.savePerson(2L,"igor","igor","igor");
//    System.out.println(personApi.findAll());
//    personApi.deletePerson(2L);
//    System.out.println(personApi.findAll());
//    personApi.deletePerson(111L);
//    System.out.println(personApi.findAll());


  }

  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
}
