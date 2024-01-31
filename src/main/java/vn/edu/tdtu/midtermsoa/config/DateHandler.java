package vn.edu.tdtu.midtermsoa.config;

import java.util.Date;
import java.util.TimeZone;

public class DateHandler {
    public static Date getCurrentDateTime(){
        TimeZone vietnamTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Date currentDateInVietnam = new Date(System.currentTimeMillis());

        // Set the time zone to Vietnam's time zone
        vietnamTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        currentDateInVietnam.setTime(currentDateInVietnam.getTime() + vietnamTimeZone.getRawOffset());
        return currentDateInVietnam;
    }
}
