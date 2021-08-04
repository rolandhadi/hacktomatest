package hacktomatest;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


class ErrorLogs {

    private String log_path = "";


    ErrorLogs(String log_path) {

        this.log_path = log_path;
        File log_file_path = new File(this.log_path + getLogFileName());
        log_file_path.getParentFile().mkdirs();

    }


    ErrorLogs() {

        this.log_path = new File("").getAbsoluteFile() + File.separator + "error_logs" + File.separator;
        File log_file_path = new File(this.log_path + getLogFileName());
        log_file_path.getParentFile().mkdirs();

    }


    private String getLogFileName() {

        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        return date_format.format(now) + ".log";

    }


    private void log(String log_text) {

        try {

            File log_file_path = new File(this.log_path + getLogFileName());
            log_file_path.getParentFile().mkdirs();
            Timestamper time_stamp = new Timestamper("yyyy-MMM-dd HH:mm:ss");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(this.log_path + getLogFileName(), true)));
            out.println(time_stamp.getTimestamp() + " - " + log_text);
            out.close();
        }
        catch (Exception e) {
            System.out.println(new Timestamper("yyyy-MMM-dd HH:mm:ss - - - - - ").getTimestamp() + (new StackTrace(e).error_text));
        }

    }

    void app_log(String log_text) {
        Timestamper time_stamp = new Timestamper("yyyy-MMM-dd HH:mm:ss - - - - - ");
        System.out.println(time_stamp.getTimestamp() + log_text);
        this.log(log_text);
    }
}
