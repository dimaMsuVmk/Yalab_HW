package messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javax.sql.DataSource;

public class MessageFilterApp {
  public static void main(String[] args) throws Exception {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    DataSource dataSource = applicationContext.getBean(DataSource.class);
    //создание и инициализация таблицы "filtertable"
    WordRepository wordRepository = applicationContext.getBean(WordRepository.class);
    wordRepository.initDb();
    //Запустим обработку очередей в отдельном потоке
    //Можно вынести Слушатель очередей в отдельный класс
    DataProcessor dataProcessor = applicationContext.getBean(DataProcessor.class);
//    Thread thread = new Thread(() -> dataProcessor.processQueue());
//    thread.start();
    Thread thread1 = new Thread(() -> dataProcessor.inputConsume());
    thread1.start();
    Thread thread2 = new Thread(() -> dataProcessor.outputConsume());
    thread2.start();

    SaveApi inputApi = applicationContext.getBean(SaveApi.class);
    //пишем в очередь "input"
    inputApi.saveInput("Fuck you, уважаемый!");
    inputApi.saveInput("eбать , кто сервер уронил?");
    inputApi.saveInput("Эй, придурок, Микросервис пиши давай!");

    Thread.sleep(5000);
    Channel inputChannel = (Channel) applicationContext.getBean("channelInput");
    inputChannel.close();
    Channel outputChannel = (Channel) applicationContext.getBean("channelOutput");
    outputChannel.close();
    Connection connection = applicationContext.getBean(Connection.class);
    connection.close();
  }
}
