package hacktomatest;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by roland.hadi on 24/8/2016.
 */
class Database {

    private Connection connection;

    Database(String db_path, String db_type) throws ClassNotFoundException, SQLException {

        database__(db_path, db_type);

    }

    Database(String database_filename) throws SQLException, ClassNotFoundException {

        database__(database_filename, "sqlite");

    }

    private void database__(String db_path, String db_type) throws ClassNotFoundException, SQLException {

        if (db_type.equals("sqlite")) {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + db_path.replace(File.separator, "/"));
        }
        else if (db_type.equals("mysql")) {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + db_path, "root", "");
        }

    }

    void executeStatement(String sql_statement) throws SQLException {

        Statement execute_statement = connection.createStatement();
        execute_statement.executeUpdate(sql_statement);
        execute_statement.close();

    }

    boolean tableExist(String table_name) throws SQLException {
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
            if (rs.getString(3).equals(table_name)) {
                return true;
            }
        }
        return false;
    }

    void createMigrationsTable(String database) throws SQLException {
        Statement sql_statement = connection.createStatement();
        String sql;
        Map<String, String> sqlParam = new HashMap<>();
        sqlParam.put("action", "create");
        sqlParam.put("table_name", "migrations");
        sqlParam.put("column_count", "0");
        Integer newColumnCount = Integer.valueOf(sqlParam.get("column_count") + 1);
        sqlParam.put("column_count", newColumnCount.toString());
        sqlParam.put("column_name_" + newColumnCount, "id");
        sqlParam.put("column_type_" + newColumnCount, "integer");
        sqlParam.put("column_params_" + newColumnCount, "PRIMARYKEY AUTOINCREMENT NOTNULL");
        newColumnCount = Integer.valueOf(sqlParam.get("column_count") + 1);
        sqlParam.put("column_count", newColumnCount.toString());
        sqlParam.put("column_name_" + newColumnCount, "migration");
        sqlParam.put("column_type_" + newColumnCount, "string");
        sqlParam.put("column_params_" + newColumnCount, "notnull");
        sql = new Table().Sqlser(sqlParam,database);
        sql_statement.executeUpdate(sql);
        sql_statement.close();
    }

    List<String> newMigrations(List<String> files) throws SQLException {
        List<String> migration_files = new ArrayList<>();
        List<String> new_migration_files = new ArrayList<>();
        Statement sql_statement = connection.createStatement();
        String sql = "SELECT * FROM migrations ORDER BY id";
        ResultSet recordset = sql_statement.executeQuery(sql);
        while (recordset.next()) {
            migration_files.add(recordset.getString("migration"));
        }
        for (String file : files) {
            boolean migration_found = false;
            for (String migration : migration_files) {
                if (file.equals(migration)) {
                    migration_found = true;
                }
            }
            if (!migration_found) {
                new_migration_files.add(file);
            }
        }
        recordset.close();
        sql_statement.close();
        return new_migration_files;
    }

    void migrate(List<String> migrationSqlCommands) throws SQLException {
      for (String migrationSqlCommand : migrationSqlCommands) {
        System.out.println(migrationSqlCommand);
        executeStatement(migrationSqlCommand);
      }
    }


}
