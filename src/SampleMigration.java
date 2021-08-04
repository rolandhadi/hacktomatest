package hacktomatest;

public class SampleMigration {

    private String migrationName;
    private Table table;

    SampleMigration() {
        this.migrationName = "<<MIGRATION_NAME>>";
    }

    void migrateUp() {
        table.create("test_table");
        table.increment("test_field");
        table.string("test").index();
        System.out.println(table.getSql());
    }

    void migrateDown() {
        table.drop();
    }

}
