package hacktomatest;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


class Migration {

    private Database database;
    private Map<String, String> directory;

    Migration(Map<String, String> directory) throws SQLException, ClassNotFoundException, IOException {

        this.directory = directory;
        database = new Database(this.directory.get("database.db"), new Json(new File(directory.get("config.app"))).get("app.project.database"));

    }

    List<String> getMigrations() throws IOException, SQLException, ClassNotFoundException {

        FilesFolders dir = new FilesFolders();

        database.createMigrationsTable(new Json(new File(directory.get("config.app"))).get("app.project.database"));

        File folder = new File(directory.get("migration"));
        File[] files = folder.listFiles();
        List<String> migrationFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (!fileName.equals("Table.java") && !fileName.equals("MigrationLoader.java")) {
                      migrationFiles.add(fileName);
                    }
                }
            }
        }
        List<String> new_migrations = database.newMigrations(migrationFiles);
        return new_migrations;

    }

    void migrate() throws IOException, SQLException, ClassNotFoundException {

        List<String> new_migrations = getMigrations();
        if (new_migrations.isEmpty()) {
            System.out.println("Nothing to migrate!");
        }
        else {
            database.migrate(new MigrationLoader().run());
        }

    }

}
