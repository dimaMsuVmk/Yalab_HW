package messagefilter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import util.DbUtil;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class MessageFilterApp {
  public static void main(String[] args) throws Exception {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    DataSource dataSource = applicationContext.getBean(DataSource.class);
    //создание и инициализация таблицы "filtertable"
    initDb(dataSource);
    //Запустим обработку очередей в отдельном потоке
    //Можно вынести Слушатель очередей в отдельный класс
    DataProcessor dataProcessor = applicationContext.getBean(DataProcessor.class);
    Thread thread = new Thread(() -> dataProcessor.processQueue());
    thread.start();

    SaveApi inputApi = applicationContext.getBean(SaveApi.class);
    //пишем в очередь "input"
    inputApi.saveInput("Fuck you, уважаемый!");
    inputApi.saveInput("eбать , кто сервер уронил?");
    inputApi.saveInput("Эй, придурок, Микросервис пиши давай!");

  }



  public static void initDb(DataSource dataSource) throws SQLException, IOException {
    String createFilterTable = ""
            + "CREATE TABLE filtertable (\n"
            //+ "   id bigserial NOT NULL,\n"
            + "   word varchar\n"
            + ");";
    String deleteTable = "DELETE FROM filtertable;";
    String insert = "INSERT INTO filtertable (word) VALUES (?)";
    try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        BufferedReader bufferedReader = new BufferedReader(new FileReader("fileOfWordsFilter.txt"))) {
      DatabaseMetaData metaData = connection.getMetaData();
      ResultSet setTables = metaData.getTables(null, "public","filtertable", new String[]{"TABLE"});
      boolean exists = setTables.next();
      setTables.close();
      if(! exists) {
        DbUtil.applyDdl(createFilterTable, dataSource);
      } else {
        DbUtil.applyDdl(deleteTable, dataSource);
      }
      String line = null;
      while ((line = bufferedReader.readLine()) != null){
        preparedStatement.setString(1,line);
        preparedStatement.executeUpdate();
      }
    }

  }
}
