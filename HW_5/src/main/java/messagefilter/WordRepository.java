package messagefilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import util.DbUtil;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

@Repository
public class WordRepository {
    @Autowired
    private DataSource dataSource;

    public void initDb() throws SQLException, IOException {
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

    public boolean isExistInDb(String word){
        boolean isExist = false;
        String select = "SELECT * FROM filtertable WHERE word = ?";
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select)) {
            preparedStatement.setString(1,word.toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            isExist = resultSet.next();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isExist;
    }
}
