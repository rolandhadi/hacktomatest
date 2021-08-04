package hacktomatest;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


public class Hacktomatest {

    private static Map<String, String> directory = new HashMap<>();
    private static String version = "1.0";
    private static String built = "August 25, 2016";
    private static boolean debug = false;


    public static void main(String[] args) {

        if (args.length > 0) {
            initialize();
            print();
            if (debug) {
                initialize("my-first-project");
            }
            switch (args[0]) {
                case "make:project":
                    try {
                        print(makeProject(args));
                    } catch (Exception e) {
                        new ErrorLogs().app_log(new StackTrace(e).error_text);
                    }
                    break;
                case "make:model":
                    try {
                        print(makeModel(args));
                    } catch (Exception e) {
                        new ErrorLogs().app_log(new StackTrace(e).error_text);
                    }
                    break;
                case "make:controller":
                    try {
                        print(makeController(args));
                    } catch (Exception e) {
                        new ErrorLogs().app_log(new StackTrace(e).error_text);
                    }
                    break;
                case "make:migration":
                    try {
                        print(makeMigration(args));
                    } catch (Exception e) {
                        new ErrorLogs().app_log(new StackTrace(e).error_text);
                    }
                    break;
                case "build:migration":
                    try {
                        print(buildMigration(args));
                    } catch (Exception e) {
                        new ErrorLogs().app_log(new StackTrace(e).error_text);
                    }
                    break;
                case "migrate":
                    try {
                        migrate();
                    } catch (Exception e) {
                        new ErrorLogs().app_log(new StackTrace(e).error_text);
                    }
                    break;
                case "--version":
                    print("Hacktomatest " + version + " (built: " + built + ")");
                    print("Copyright (c) 2010-2015 Hacktomatest");
                    break;
                case "--help":
                case "-h":
                    print("<[yellow]>Usage: <[default]>");
                    print();
                    print("    command [options] [arguments]");
                    print();
                    print("<[yellow]>Commands: <[default]>");
                    print();
                    print("<[green]>   make:project            <[default]>" + "Make new automation project");
                    print("<[green]>   make:controller         <[default]>" + "Make new automation controller");
                    print("<[green]>   make:model              <[default]>" + "Make new automation model");
                    print("<[green]>   make:migration          <[default]>" + "Make new database migration");
                    print("<[green]>   migrate                 <[default]>" + "Run new database migration(s)");
                    print("<[green]>   migrate:refresh         <[default]>" + "Refresh database migration(s)");
                    break;
                default:
                    print("Invalid command!");
                    break;
            }
        }
        else {
            print("     _   _            _    _                        _            _     ");
            print("    | | | |          | |  | |                      | |          | |    ");
            print("    | |_| | __ _  ___| | _| |_ ___  _ __ ___   __ _| |_ ___  ___| |_   ");
            print("    |  _  |/ _` |/ __| |/ / __/ _ \\| '_ ` _ \\ / _` | __/ _ \\/ __| __|  ");
            print("    | | | | (_| | (__|   <| || (_) | | | | | | (_| | ||  __/\\__ \\ |_   ");
            print("    \\_| |_/\\__,_|\\___|_|\\_\\\\__\\___/|_| |_| |_|\\__,_|\\__\\___||___/\\__|  ");
            print("         <[cyan]>A Hackable Test Automation Framework for the 21st Century");
            print("               <[cyan]>by Test Automation Artisan Roland Ross Hadi");
            print();
            print("<[green]>hacktomatest <[yellow]> version <[default]> (built: <[yellow]>" + built + "<[default]>)");
            print();
            print("<[yellow]>Usage: <[default]>");
            print();
            print("    command [options] [arguments]");
            print();
            print("<[yellow]>Options: <[default]>");
            print();
            print("<[green]>   -h, --help          <[default]>" + "Display this help message");
            print("<[green]>   --version           <[default]>" + "Display this application version");

        }

    }

    private static void initialize() {

        initialize("");

    }

    private static void initialize(String project_name) {

        String initialize_var = "";
        if (!project_name.isEmpty()) {
            initialize_var = File.separator + project_name;
        }

        directory.put("host", System.getenv("HACKTOMATEST_HOME"));
        directory.put("root", System.getProperty("user.dir") + initialize_var + File.separator);
        directory.put("env", directory.get("root") + ".env");
        directory.put("app", directory.get("root") + "app" + File.separator);
        directory.put("database", directory.get("root") + "database" + File.separator);
        directory.put("config", directory.get("root") + "config" + File.separator);
        directory.put("config.app", directory.get("config") + "app.json");
        directory.put("vendor", directory.get("root") + "vendor" + File.separator);
        directory.put("model", directory.get("app") + "model" + File.separator);
        directory.put("controller", directory.get("app") + "controller" + File.separator);
        directory.put("seed", directory.get("database") + "seed" + File.separator);
        directory.put("migration", directory.get("database") + "migration" + File.separator);
        directory.put("database.db", directory.get("database") + "db");
        directory.put("resource", directory.get("host") + "hacktomatest" + File.separator + "resource" + File.separator);
        directory.put("resource.env", directory.get("resource") + ".env");
        directory.put("resource.config.app", directory.get("resource") + "config" + File.separator + "app.json");
        directory.put("resource.model.template", directory.get("resource") + "model" + File.separator + "template");
        directory.put("resource.controller.template", directory.get("resource") + "controller" + File.separator + "template");
        directory.put("resource.migration.template", directory.get("resource") + "migration" + File.separator + "template.java");
        directory.put("resource.migration.loader", directory.get("resource") + "migration" + File.separator + "loader.java");

    }

    private static String makeProject(String[] args) throws IOException {

        FilesFolders dir = new FilesFolders();
        String output = "";
        Paramerser param = new Paramerser(args);
        if (param.isHelp()) {
            print("<[yellow]>Usage: <[default]>");
            print();
            print("    make:project [tool] [project-name] [options]");
            print();
            print("<[green]>   [tool]                  <[default]>" + "vbs | java | c# | uft | selenium | seetest");
            print("<[green]>   [project-name]          <[default]>" + "New project name");
            print("<[green]>   [options]               <[default]>" + "Optional arguments");
            print("<[default]>       --sqlite            <[default]>" + "SQLite project database");
            print("<[default]>       --mysql             <[default]>" + "MySql project database");
            print("<[default]>       --access            <[default]>" + "MS Access project database");
            print("<[default]>       --csv               <[default]>" + "CSV project database (CRUD only)");
            return "";
        }
        if (param.makeProjectCheck()) {
            String new_project_folder = directory.get("root") + param.get(2);
            if (!dir.directoryExist(new_project_folder)) {
                try {
                    dir.makeFolders(new_project_folder);
                    initialize(args[2]);
                    dir.makeFolders(directory.get("config"));
                    dir.makeFolders(directory.get("vendor"));
                    dir.makeFolders(directory.get("model"));
                    dir.makeFolders(directory.get("controller"));
                    dir.makeFolders(directory.get("seed"));
                    dir.makeFolders(directory.get("migration"));
                    dir.fileCopy(directory.get("resource.env"), directory.get("env"));
                    dir.fileCopy(directory.get("resource.config.app"), directory.get("config.app"));
                    Json new_project = new Json(new File(directory.get("config.app")));
                    new_project.set("app.project.author", System.getProperty("user.name"));
                    new_project.set("app.project.name", args[2]);
                    new_project.set("app.project.created_at", new Timestamper("yyyy-MMM-dd HH:mm:ss").getTimestamp());
                    new_project.set("app.hacktomatest.version", version);
                    switch (param.get(1)) {
                        case "vbs":
                            new_project.set("app.project.tool", "vbs");
                            new_project.set("app.project.script", "vbs");
                            break;
                        case "java":
                            new_project.set("app.project.tool", "java");
                            new_project.set("app.project.script", "java");
                            break;
                        case "c#":
                            new_project.set("app.project.tool", "c#");
                            new_project.set("app.project.script", "c#");
                            break;
                        case "uft":
                            new_project.set("app.project.tool", "uft");
                            new_project.set("app.project.script", "vbs");
                            break;
                        case "selenium":
                            new_project.set("app.project.tool", "selenium");
                            new_project.set("app.project.script", "java");
                            break;
                        case "seetest":
                            new_project.set("app.project.tool", "seetest");
                            new_project.set("app.project.script", "java");
                            break;
                        default:
                            break;
                    }
                    Environment env = new Environment(directory.get("env"));
                    if (param.exist("--access")) {
                        new_project.set("app.project.database", "access");
                        env.set("DB_HOST", directory.get("database"));
                        env.set("DB_DATABASE", "database.mdb");
                        env.set("DB_USERNAME", "");
                        env.set("DB_PASSWORD", "");
                    }
                    else if (param.exist("--c")) {
                        new_project.set("app.project.database", "mysql");
                        env.set("DB_HOST", "localhost");
                        env.set("DB_DATABASE", "");
                        env.set("DB_USERNAME", "root");
                        env.set("DB_PASSWORD", "");
                    }
                    else {
                        new_project.set("app.project.database", "sqlite");
                        env.set("DB_HOST", directory.get("database"));
                        env.set("DB_DATABASE", "database.sql");
                        env.set("DB_USERNAME", "");
                        env.set("DB_PASSWORD", "");
                    }
                    new_project.save();
                    output = "New project <[yellow]>" + param.get(2) + "<[default]> created!";
                } catch (Exception e) {
                    new ErrorLogs().app_log(new StackTrace(e).error_text);
                }
            }
            else {
                output = "Project already exists!";
            }
        }
        else {
            output = "Invalid create:project syntax! -h for help";
        }
        return output;
    }


    private static String makeModel(String[] args) throws IOException {
        FilesFolders dir = new FilesFolders();
        String output;
        Paramerser param = new Paramerser(args);
        if (param.isHelp()) {
            print("<[yellow]>Usage: <[default]>");
            print();
            print("    make:model [ModelName] [options]");
            print();
            print("<[green]>   [ModelName]         <[default]>" + "New model name");
            print("<[green]>   [options]           <[default]>" + "Optional arguments");
            print("<[default]>       -m              <[default]>" + "Create database migration");
            return "";
        }
        if (!inProject()) {
            return "";
        }
        if (param.makeModelCheck()) {
            String model_name = camelCase(param.get(1));
            Json cur_project = new Json(new File(directory.get("config.app")));
            String model_extension = "." + cur_project.get("app.project.script");
            String new_model_file = directory.get("model") + model_name + model_extension;
            if (!dir.directoryExist(new_model_file)) {
                dir.fileCopy(directory.get("resource.model.template") + model_extension, new_model_file);
                output = "Model <[yellow]>" + model_name + "<[default]> created!";
            }
            else {
                output = "Model <[yellow]>" + model_name + "<[default]> already exists!";
            }
            if (param.exist("-m")) {
                String cmd = "make:migration create_" + snakeCase(model_name) + "_table --model=" + param.get(1);
                String[] mig_args = cmd.split(" ");
                makeMigration(mig_args);
            }
        }
        else {
            output = "Invalid make:model syntax! -h for help";
        }
        return output;
    }

    private static String makeController(String[] args) throws IOException {
        FilesFolders dir = new FilesFolders();
        String output;
        Paramerser param = new Paramerser(args);
        if (param.isHelp()) {
            print("<[yellow]>Usage: <[default]>");
            print();
            print("    make:controller [ControllerName]");
            print();
            print("<[green]>   [ControllerName]            <[default]>" + "New model name");
            return "";
        }
        if (!inProject()) {
            return "";
        }
        if (param.makeControllerCheck()) {
            String controller_name = camelCase(param.get(1));
            Json cur_project = new Json(new File(directory.get("config.app")));
            String model_extension = "." + cur_project.get("app.project.script");
            String new_controller_file = directory.get("controller") + controller_name + model_extension;
            if (!dir.directoryExist(new_controller_file)) {
                dir.fileCopy(directory.get("resource.controller.template") + model_extension, new_controller_file);
                output = "Controller <[yellow]>" + controller_name + "<[default]> created!";
            }
            else {
                output = "Controller <[yellow]>" + controller_name + "<[default]> already exists!";
            }
        }
        else {
            output = "Invalid make:controller syntax! -h for help";
        }
        return output;
    }

    private static String makeMigration(String[] args) throws IOException {
        FilesFolders dir = new FilesFolders();
        String output;
        Paramerser param = new Paramerser(args);
        if (param.isHelp()) {
            print("<[yellow]>Usage: <[default]>");
            print();
            print("    make:migration [migration_name]");
            print();
            print("<[green]>   [migration_name]            <[default]>" + "New migration name");
            return "";
        }
        if (!inProject()) {
            return "";
        }
        if (param.makeMigrationCheck()) {
            String migration_name = snakeCase(param.get(1));
            Json cur_project = new Json(new File(directory.get("config.app")));
            String model_extension = "." + cur_project.get("app.project.script");
            String new_migration_name = "M_" + new Timestamper("yyyy_MM_dd_HH_mm_ss_").getTimestamp() + migration_name;
            String new_migration_file = directory.get("migration") + new_migration_name + ".java";
            dir.fileCopy(directory.get("resource.migration.template"), new_migration_file);
            dir.writeFile(new_migration_file, dir.read_file(new_migration_file).replace("MIGRATION_TABLE_NAME", snakeCase(param.get("--model", "table_name"))).replace("MIGRATION_NAME", new_migration_name));
            output = "Migration <[yellow]>" + new_migration_name + "<[default]> created!";
        }
        else {
            output = "Invalid make:migration syntax! -h for help";
        }
        return output;
    }

    private static String buildMigration(String[] args) throws IOException, SQLException, ClassNotFoundException {
        String output;
        FilesFolders dir = new FilesFolders();
        if (!inProject()) {
            return "";
        }
        String projectMigrations = directory.get("host") + "hacktomatest" + File.separator;
        String sourceFolder = directory.get("host") + "hacktomatest" + File.separator + "src" + File.separator;
        String loaderFile = directory.get("migration") + "MigrationLoader.java";
        dir.fileCopy(sourceFolder + "Table.java", directory.get("migration") + "Table.java");
        String migrationCodeRun = "";
        String migrationCodeRollback = "";
        for (String mirationName : new Migration(directory).getMigrations()) {
          migrationCodeRun += "out = new " + mirationName.replace(".java", "") + "().run();\n";
          migrationCodeRollback += "out = new " + mirationName.replace(".java", "") + "().rollback();\n";
          System.out.println(mirationName);
        }
        dir.fileCopy(directory.get("resource.migration.loader"), loaderFile);
        dir.writeFile(loaderFile, dir.read_file(loaderFile).replace("MIGRATION_CODE_RUN", migrationCodeRun).replace("MIGRATION_CODE_ROLLBACK", migrationCodeRollback));
        try {
            Process cmdProcess = Runtime.getRuntime().exec("javac " + directory.get("migration") + "*.java");
            int errorCode = cmdProcess.waitFor();
            BufferedReader errinput = new BufferedReader(new InputStreamReader(
                    cmdProcess.getErrorStream()));
            "".isEmpty()
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        for (final File fileEntry : new File(directory.get("migration")).listFiles()) {
            String filename = fileEntry.getName();
            if (filename.contains(".class") && !filename.equals("Table.class")) {
                dir.fileCopy(directory.get("migration") + filename, projectMigrations + filename);
                dir.deleteFile(directory.get("migration") + filename);
            }
        }
        dir.deleteFile(directory.get("migration") + "Table.java");
        dir.deleteFile(directory.get("migration") + "Table.class");
        output = "<[default]> Migration sucessfully compiled! <[default]>";
        return output;
    }

    private static void migrate() throws SQLException, ClassNotFoundException, IOException {

        if (!inProject()) {
            return;
        }
        Json cur_database = new Json(new File(directory.get("config.app")));
        String database_type = cur_database.get("app.project.database");
        Environment env = new Environment(directory.get("env"));
        if (database_type.equals("mysql")) {
            directory.put("database.db", env.get("DB_HOST") + "/" + env.get("DB_DATABASE") + "?user=" + env.get("DB_USERNAME") + "&password=" + env.get("DB_PASSWORD"));
        }
        else {
            directory.put("database.db", env.get("DB_HOST") + File.separator + env.get("DB_DATABASE"));
        }
        Migration migration = new Migration(directory);
        migration.migrate();

    }

    private static String snakeCase(String text) {
        return text.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }

    private static String camelCase(String text) {
        while (text.contains("__")) {
            text = text.replace("__", "_");
        }
        StringBuffer sb = new StringBuffer();
        for (String s : text.split("_")) {
            sb.append(Character.toUpperCase(s.charAt(0)));
            if (s.length() > 1) {
                sb.append(s.substring(1, s.length()).toLowerCase());
            }
        }
        return String.valueOf(sb);
    }

    private static boolean inProject() {
        boolean output = false;
        try {
            if (new FilesFolders().directoryExist(directory.get("config.app"))) {
                output = true;
            }
        } catch (IOException e) {
            output = false;
        }
        if (!output) {
            print("The current folder is not a valid hacktomatest project!");
        }
        return output;
    }

    private static void print(String text) {
        System.out.println(new PrintColored(text).get());
    }

    private static void print() {
        System.out.println();
    }

}


/*
  Add folder of hacktomatest.bat to environment variables Path
  Add hacktomatest.bat to folder

COMPILE SRC
  cd c:\hacktomatest\hacktomatest\
  javac -cp lib\* -d "\hacktomatest" *.java

    @echo off
    set HACKTOMATEST_HOME=d:\roland.hadi\hacktomatest\out\artifacts\hacktomatest_jar\
    java -cp "d:/roland.hadi/hacktomatest/out/artifacts/hacktomatest_jar/hacktomatest.jar;D:/roland.hadi/hacktomatest/lib/*" hacktomatest.Hacktomatest %*

 * KEYWORDS
 *
 * make:project uft my-first-project
 * make:model User  -m
 * make:controller UserController
 * make:migration create_model_table
 * migrate
 *
 */
