package eventsourcing.db;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

//@Component
public class DbApp {
  public static void main(String[] args) throws Exception {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    DataProcessor dataProcessor  = applicationContext.getBean(DataProcessor.class);
    dataProcessor.process();
    // тут пишем создание и запуск приложения работы с БД
  }
}
