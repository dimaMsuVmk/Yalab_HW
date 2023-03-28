package io.ylab.intensive.lesson04.filesort;

import io.ylab.intensive.lesson04.DbUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import static io.ylab.intensive.lesson04.filesort.FileSorterTest.initDb;

public class FileSortImpl implements FileSorter {
  private DataSource dataSource;
  private long BATCH_SIZE;

  public FileSortImpl(DataSource dataSource) {
    this.BATCH_SIZE = 1_000_000;
    this.dataSource = dataSource;
  }

  @Override
  public File sort(File data) {
    selectSort(data,"fileSortWithoutBatch",false);
    /**
     * "Сбросим" базу данных
     */
    try{
      String createSortTable = ""
              + "drop table if exists numbers;"
              + "CREATE TABLE if not exists numbers (\n"
              + "\tval bigint\n"
              + ");";
      DbUtil.applyDdl(createSortTable, dataSource);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return selectSort(data,"fileSortBatch",true);
  }

  /**
   * selectSort(File data,String targetFile,boolean sortBatch) принимает два новых параметра
   * targetFile - имя файла,куда положить отсортированые значения
   * sortBatch - true/false -  использовать batch-processing или нет
   */
  private File selectSort(File data,String targetFile,boolean sortBatch){
    long timeStart = System.currentTimeMillis();
    if(sortBatch) save(data); else saveWithoutBatch(data);
    String SELECT = "SELECT val FROM numbers ORDER BY val desc";
    File dataSortBatch = new File(targetFile);
    try(BufferedWriter bw = new BufferedWriter(new FileWriter(dataSortBatch));
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()){
      ResultSet resultSet = statement.executeQuery(SELECT);
      while (resultSet.next()){
        bw.write("" + resultSet.getLong("val"));
        bw.newLine();
      }

    } catch (SQLException | IOException e) {
      throw new RuntimeException(e);
    }
    long timeEnd = System.currentTimeMillis();
    long timeSort = timeEnd - timeStart;

    if(sortBatch) System.out.println("Sort With Batch: " + timeSort + " Millis");
    else System.out.println("Sort Without Batch: " + timeSort + " Millis");

    return dataSortBatch;
  }

  private void save(File data){
    try(BufferedReader br = new BufferedReader(new FileReader(data));
        Connection connection = dataSource.getConnection()){
      connection.setAutoCommit(false);
      Statement statement = connection.createStatement();
      String line = null;
      int i = 0;//counter Of Batch
      while ((line = br.readLine()) != null){
        i++;
        long buf = Long.parseLong(line);
        String sql = "INSERT INTO numbers(val) VALUES (\'" +buf +"\')";
        statement.addBatch(sql);
        if (i % BATCH_SIZE == 0) {
          statement.executeBatch();
          connection.commit();//надо ли комитить каждый блок данных? спорный вопрос
        }
      }
      statement.executeBatch();
      connection.commit();
      statement.close();
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void saveWithoutBatch(File data){
    try(BufferedReader br = new BufferedReader(new FileReader(data));
        Connection connection = dataSource.getConnection()){
      connection.setAutoCommit(false);
      Statement statement = connection.createStatement();
      String line = null;
      while ((line = br.readLine()) != null){
        long buf = Long.parseLong(line);
        String sql = "INSERT INTO numbers(val) VALUES (\'" +buf +"\')";
        statement.executeUpdate(sql);
      }
      connection.commit();
      statement.close();
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
