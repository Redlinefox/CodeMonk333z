package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAO {
    private final String QUERY = "SELECT FIRST_NAME, LAST_NAME FROM EMPLOYEES WHERE EMPLOYEE_ID=101";
    private final String QUERY2 = "SELECT FIRST_NAME, LAST_NAME, SALARY, JOB_TITLE FROM EMPLOYEES JOIN JOBS ON EMPLOYEES.JOB_ID=JOBS.JOB_ID WHERE SALARY = (SELECT MAX(SALARY) FROM EMPLOYEES)";
    private Connection connection;
    
    public Connection getConnection() {
        return connection;
    }
    
    public PreparedStatement runSQLQuery() {
        connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnectionManager.getConnection();
            statement = connection.prepareStatement(QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return statement;
    }
    
    
}
