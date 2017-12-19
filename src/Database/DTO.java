package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DTO {
    public List<List<String>> retrieveDTO() throws SQLException {
        List<List<String>> rows = new ArrayList<>();
        List<String> row;
        
        DAO dao = new DAO();
        PreparedStatement statement = dao.runSQLQuery();
        
        ResultSet resultSet = statement.executeQuery();
        ResultSet resultSetSize = statement.executeQuery();
        
        while(resultSet.next()) {
            row = new ArrayList<>();
            resultSetSize.last();
            for(int i = 1; i < resultSetSize.getRow(); i++) {
                row.add(resultSet.getString(i));
            }
            rows.add(row);
        }
        
        DBConnectionManager.closeConnection(dao.getConnection());
        return rows;
    }
}
