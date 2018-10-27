import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private final static DateFormat sDateFormatter = new SimpleDateFormat("dd.MM.yyyy");

    public static String formatDate(Date date) {
        return sDateFormatter.format(date);
    }

    public static Date parseDate(String string) throws ParseException {
        return sDateFormatter.parse(string);
    }
}
