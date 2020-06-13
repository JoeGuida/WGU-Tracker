package database;

import android.util.Log;
import androidx.room.TypeConverter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    @TypeConverter
    public static Date toDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        try { return formatter.parse(dateString); }
        catch (java.text.ParseException e) {
            Log.d("PARSE_EXCEPTION", e.getMessage());
            return null;
        }
    }

    @TypeConverter
    public static String toString(long timestamp) {
        Date date = new Date(timestamp);
        return date.toString().substring(0, 10) + date.toString().substring(date.toString().length() - 5, date.toString().length());
    }

}
