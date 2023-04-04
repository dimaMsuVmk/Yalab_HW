package sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder{
    @Autowired
    private DataSource dataSource;
    @Override
    public String queryForTable(String tableName) throws SQLException {
        //на случай если в tableName будет еще название схемы бд: tableName = "public.person"
        String schemaName = null;
        String[] arr = tableName.split("\\.");
        if(arr.length > 1){
            tableName = arr[1];
            schemaName = arr[0];
        }
        boolean isExists = false;
        String select = null;
        try(Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet setTables = metaData.getTables(null, schemaName, tableName, new String[] {"TABLE"});
            isExists = setTables.next();
            setTables.close();
            if(isExists) {
                List<String> listColumns = new ArrayList<>();
                ResultSet setColumns = metaData.getColumns(null,schemaName, tableName,null);
                while (setColumns.next()){
                    listColumns.add(setColumns.getString("COLUMN_NAME"));
                }
                setColumns.close();
                StringBuilder sb = new StringBuilder("");
                for (int i = 0; i < listColumns.size() - 1; i++) {
                    sb.append(listColumns.get(i)).append(", ");
                }
                if(listColumns.size() > 0) sb.append(listColumns.get(listColumns.size() - 1));
                select = "SELECT " + sb + " FROM "+ tableName;
            }
        }
        return select;
    }

    @Override
    public List<String> getTables() throws SQLException {
        ResultSet setOfName = null;
        List<String> list = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            setOfName = metaData.getTables(null, null, "%", new String[] {"TABLE"});
            while (setOfName.next()) {
                list.add(setOfName.getString("TABLE_NAME"));
            }
            setOfName.close();
        }
        return list;
    }
}
