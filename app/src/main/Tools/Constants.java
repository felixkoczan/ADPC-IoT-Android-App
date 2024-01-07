import java.text.DecimalFormat;

// Class to hold application-wide constant values.
public class Constants {
    // Constant for a DecimalFormat. This format will round numbers to two decimal places.
    // For example, formatting 123.456 will result in "123.46".
    public static final DecimalFormat DOUBLE_TWO_DIGIT_ACCURACY = new DecimalFormat("#.##");

    // Constant for a date and time format. This can be used wherever consistent
    // date-time formatting is needed.
    // The format "yyyy-MM-dd HH:mm:ss" represents a date and time in the format:
    // 4-digit year, 2-digit month, 2-digit day, 24-hour, minute, and second.
    // Example: "2021-12-31 15:30:45".
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
