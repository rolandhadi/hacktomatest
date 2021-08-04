package hacktomatest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Table {

    private List<String> migrationCommands = new ArrayList<>();
    private String currentTableName;
    private String currentFieldName;
    private Map<String, String> sqlParam = new HashMap<>();


    Table create(String tableName) {
        this.currentTableName = tableName;
        sqlParam.put("action", "create");
        sqlParam.put("table_name", tableName);
        sqlParam.put("column_count", "0");
        return this;
    }


    Table increment(String fieldName) {
        this.currentFieldName = fieldName;
        Integer newColumnCount = Integer.valueOf(sqlParam.get("column_count") + 1);
        sqlParam.put("column_count", newColumnCount.toString());
        sqlParam.put("column_name_" + newColumnCount, fieldName);
        sqlParam.put("column_type_" + newColumnCount, "integer");
        sqlParam.put("column_params_" + newColumnCount, "PRIMARYKEY AUTOINCREMENT NOTNULL");
        return this;
    }


    Table string(String fieldName) {
        this.currentFieldName = fieldName;
        Integer newColumnCount = Integer.valueOf(sqlParam.get("column_count") + 1);
        sqlParam.put("column_count", newColumnCount.toString());
        sqlParam.put("column_name_" + newColumnCount, fieldName);
        sqlParam.put("column_type_" + newColumnCount, "string");
        sqlParam.put("column_params_" + newColumnCount, "notnull");
        return this;
    }

    Table integer(String fieldName) {
        this.currentFieldName = fieldName;
        Integer newColumnCount = Integer.valueOf(sqlParam.get("column_count") + 1);
        sqlParam.put("column_count", newColumnCount.toString());
        sqlParam.put("column_name_" + newColumnCount, fieldName);
        sqlParam.put("column_type_" + newColumnCount, "integer");
        sqlParam.put("column_params_" + newColumnCount, "notnull");
        return this;
    }


    Table index() {
        Integer newColumnCount = Integer.valueOf(sqlParam.get("column_count"));
        sqlParam.put("column_params_" + newColumnCount, "index");
        return this;
    }

    Table unique() {
        Integer newColumnCount = Integer.valueOf(sqlParam.get("column_count"));
        sqlParam.put("column_params_" + newColumnCount, "unique");
        return this;
    }

    Table nullable() {
        int newColumnCount = Integer.parseInt(sqlParam.get("column_count"));
        String newValue = sqlParam.get("column_params_" + newColumnCount) + "";
        System.out.println(newValue);
        newValue = newValue.replace("notnull", "nullable");
        System.out.println(newValue);
        sqlParam.put("column_params_" + newColumnCount, newValue);
        return this;

        "".
    }

    Table drop(String tableName) {
        sqlParam.put("action", "drop");
        sqlParam.put("table_name", tableName);
        sqlParam.put("column_count", "0");
        return this;
    }


    List<String> getSql(String sqlType) {
        migrationCommands.add(Sqlser(sqlParam, sqlType));
        return migrationCommands;
    }


    void run() {

    }

    String Sqlser(Map<String, String> sqlParam, String sqlType) {
        String outSql = "";
        String sqlAction = sqlParam.get("action") + "";
        if (sqlAction.equalsIgnoreCase("create")) {
            if (sqlType.equalsIgnoreCase("sqlite")) {
                String sqlLines = getSqlLine(sqlParam, sqlType);
                sqlLines = sqlLines.substring(1, sqlLines.length() - 1);
                outSql = "BEGIN TRANSACTION; CREATE TABLE IF NOT EXISTS " + sqlParam.get("table_name") +
                        "(" +
                        sqlLines +
                        "); COMMIT;";
            }
            else if (sqlType.equalsIgnoreCase("mysql")) {

            }
        }
        else if (sqlAction.equalsIgnoreCase("drop")) {
            if (sqlType.equalsIgnoreCase("sqlite")) {
                outSql = "BEGIN TRANSACTION; DROP TABLE IF NOT EXISTS " + sqlParam.get("table_name") +
                        "); COMMIT;";
            }
            else if (sqlType.equalsIgnoreCase("mysql")) {

            }
        }
        else {

        }

        return outSql;
    }

    private String getSqlLine(Map<String, String> sqlParam, String sqlType) {

        Integer totalColumns = Integer.valueOf(sqlParam.get("column_count"));
        String outSql = "";
        if (sqlType.equalsIgnoreCase("sqlite")) {
            if (sqlParam.get("action").equalsIgnoreCase("create")) {
                for (int i = 1;
                     i <= totalColumns;
                     i++) {
                    outSql += sqlParam.get("column_name_" + i);
                    String column_type = sqlParam.get("column_type_" + i);
                    String column_params = sqlParam.get("column_params_" + i);
                    if (column_type.equalsIgnoreCase("integer")) {
                        outSql += " INTEGER ";
                    }
                    else if (column_type.equalsIgnoreCase("string")) {
                        outSql += " TEXT ";
                    }
                    if (column_params.contains("PRIMARYKEY")) {
                        outSql += " PRIMARY KEY ";
                    }
                    if (column_params.contains("AUTOINCREMENT")) {
                        outSql += " AUTOINCREMENT ";
                    }
                    if (column_params.contains("NOTNULL")) {
                        outSql += " NOT NULL ";
                    }
                    if (column_params.contains("NULL")) {
                        outSql += " NULL ";
                    }
                    outSql += ",";
                }
            }
        }
        return outSql;

    }


}
