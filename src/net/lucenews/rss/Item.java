package net.lucenews.rss;

import java.text.*;
import java.util.*;

public class Item {
    
    public static String asString (Calendar calendar) {
        return asString( calendar.getTime(), calendar.getTimeZone() );
    }
    
    public static String asString (Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return asString( calendar );
    }
    
    public static String asString (Date date, TimeZone timezone) {
        // Sat, 07 Sep 2002 09:42:31 GMT
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MM MMM yyyy HH:mm:ss z");
        
        StringBuffer buffer = new StringBuffer();
        
        formatter.format( date, buffer, new FieldPosition( DateFormat.TIMEZONE_FIELD ) );
        
        return buffer.toString();
    }
    
}
