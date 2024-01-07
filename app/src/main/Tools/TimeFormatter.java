import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class to handle date-time formatting in ISO 8601 format.
 */
public class TimeFormatter {
    // ISO 8601 date format string.
    private final static String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";
    
    // SimpleDateFormat instance for ISO 8601 format, with Locale set to US.
    private final static SimpleDateFormat ISO_FORMATTER = new UtcDateFormatter(ISO_FORMAT, Locale.US);

    /**
     * Formats the given Date object into an ISO 8601 string.
     *
     * @param date The Date object to format.
     * @return A string representation of the date in ISO 8601 format.
     */
    public static String getIsoDateTime(final Date date) {
        return ISO_FORMATTER.format(date);
    }

    /**
     * Formats a date-time given in milliseconds since the Unix epoch into an ISO 8601 string.
     *
     * @param millis The milliseconds since January 1, 1970, 00:00:00 GMT.
     * @return A string representation of the date-time in ISO 8601 format.
     */
    public static String getIsoDateTime(final long millis) {
        return getIsoDateTime(new Date(millis));
    }
}
