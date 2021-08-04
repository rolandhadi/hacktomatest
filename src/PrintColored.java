package hacktomatest;

/**
 * Created by roland.hadi on 26/8/2016.
 */
class PrintColored {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static String text = "";

    PrintColored(String text) {

        PrintColored.text = text;

    }

    PrintColored() {


    }

    String get() {

        text = text.replace("<[default]>", ANSI_RESET);
        text = text.replace("<[black]>", ANSI_BLACK);
        text = text.replace("<[red]>", ANSI_RED);
        text = text.replace("<[green]>", ANSI_GREEN);
        text = text.replace("<[yellow]>", ANSI_YELLOW);
        text = text.replace("<[blue]>", ANSI_BLUE);
        text = text.replace("<[purple]>", ANSI_PURPLE);
        text = text.replace("<[cyan]>", ANSI_CYAN);
        text = text.replace("<[white]>", ANSI_WHITE);
        return text;


    }
}
