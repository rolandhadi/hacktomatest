package hacktomatest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class Timestamper {


    private String time_format = "yyyyMMdd_HH_mm_ss";


    Timestamper(String time_format) {

        this.time_format = time_format;

    }


    Timestamper() {

        this.time_format = "yyyyMMdd_HH_mm_ss";

    }


    String getTimestamp() {

        DateFormat date_format = new SimpleDateFormat(this.time_format);
        Date date = new Date();
        return date_format.format(date);

    }
}