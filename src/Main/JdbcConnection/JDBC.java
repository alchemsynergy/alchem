package Main.JdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBC {

    public static Connection databaseConnect() {
        Connection dbConnection = null;
        try {
            Class.forName("org.postgresql.Driver");
            dbConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/AlchemDB",
                    "postgres", "prince123");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbConnection;
    }

}
