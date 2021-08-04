package hacktomatest;


class Paramerser {
    private String args[];

    Paramerser(String args[]) {

        this.args = args;

    }

    boolean makeProjectCheck() {
        return (args.length >= 3);
    }

    boolean makeModelCheck() {
        return (args.length >= 1);
    }

    boolean makeControllerCheck() {
        return (args.length >= 1);
    }

    boolean makeMigrationCheck() {
        return (args.length >= 1);
    }

    String get(Integer index) {
        return args[index];
    }

    String get(String name, String default_value) {
        String output = "";
        for (String arg : args) {
            if (arg.contains(name)) {
                output = arg.split("=")[1];
            }
        }
        if (output.isEmpty()) {
            return default_value;
        }
        else {
            return output;
        }
    }

    String get(String name) {
        String output = "";
        for (String arg : args) {
            if (name.equals(arg)) {
                output = arg.split("=")[1];
            }
        }
        return output;
    }

    boolean exist(String name) {
        boolean output = false;
        for (String arg : args) {
            if (name.equals(arg)) {
                output = true;
            }
        }
        return output;
    }

    boolean isHelp() {
        return args.length == 1 || args[1].equals("-h");
    }

}
