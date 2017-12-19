package Database;

import java.sql.SQLException;
import java.util.List;

public class Starter {
    public static void main(String[] args) throws SQLException {
        DTO dto = new DTO();
        List<List<String>> dtoResult = dto.retrieveDTO();
        for(List<String> row:dtoResult) {
            for(String item: row) {
                System.out.print(item);
            }
            System.out.println();
        }
    }
}
