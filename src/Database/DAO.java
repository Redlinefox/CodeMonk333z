package Database;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO {
   // private final String QUERY = "ALTER SESSION SET current_schema = REFDATA";
    private final String QUERY1 = "SELECT NAME FROM REFDATA.INSTRUMENT WHERE ID=1";
//    private final String QUERY = "SELECT * FROM DEPARTMENT_TAMPA08";
private Logger log = Logger.getLogger(DAO.class.getName());
    
    /*
    Method to run a SQL query
     */
    public void runSQLQuery() {
        Connection connection = null;
        try {
            connection = DBConnectionManager.getConnection();
           // PreparedStatement statement = connection.prepareStatement(QUERY);
            PreparedStatement statement1 = connection.prepareStatement(QUERY1);
//            statement.setInt(1,1);
//            runQueryAlter(statement);
            runQuery(statement1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnectionManager.closeConnection(connection);
        }
    }

    private void runQueryAlter(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
    }

    private void runQuery(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            log.info("Name " + resultSet.getString(1));
        }
    }
}