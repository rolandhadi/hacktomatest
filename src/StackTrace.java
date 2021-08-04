package hacktomatest;

import java.io.PrintWriter;
import java.io.StringWriter;

class StackTrace {

    String error_text = "";

    StackTrace(Exception e) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        this.error_text = sw.toString();

    }

}