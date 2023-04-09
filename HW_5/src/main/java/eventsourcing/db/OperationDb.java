package eventsourcing.db;

import eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OperationDb {
    @Autowired
    private DataSource dataSource;
    public OperationDb() {
    }

    public void delete(Long personId) throws SQLException {
        String deleteSql = "DELETE FROM person WHERE person_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
            preparedStatement.setLong(1,personId);
            int countDeleted = preparedStatement.executeUpdate();
            if(countDeleted == 0) System.out.println("Была попытка удаления, данные не найдены в БД");
        }
    }

    public void save(Person person) throws SQLException {
        String saveSql = "INSERT INTO person(person_id, first_name,last_name, middle_name) VALUES (?,?,?,?)";
        String selectSql = "SELECT * FROM person WHERE person_id = ?";
        String updateSql = "UPDATE person " +
                "SET first_name = ?,last_name = ?,middle_name = ? " +
                "WHERE person_id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statementSave = connection.prepareStatement(saveSql);
            PreparedStatement statementSelect = connection.prepareStatement(selectSql);
            PreparedStatement statementUpdate = connection.prepareStatement(updateSql)){
            statementSelect.setLong(1,person.getId());
            ResultSet resultSet = statementSelect.executeQuery();
            if(!resultSet.next()) {
                statementSave.setLong(1,person.getId());
                statementSave.setString(2,person.getName());
                statementSave.setString(3,person.getLastName());
                statementSave.setString(4,person.getMiddleName());
                statementSave.executeUpdate();
            } else{
                statementUpdate.setString(1,person.getName());
                statementUpdate.setString(2,person.getLastName());
                statementUpdate.setString(3,person.getMiddleName());
                statementUpdate.setLong(4,person.getId());
                statementUpdate.executeUpdate();
            }
        }
    }
}

