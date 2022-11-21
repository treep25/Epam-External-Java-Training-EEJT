package task2;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class InputValidation {
    public static boolean isNameValid(String name) {
        return name != null
                && name.matches("[^!@#$%^&*()+= ~]+");
    }

    public static boolean isExtValid(String ext) {
        return ext != null
                && ext.matches("[.]{1}[\\D]+");
    }

    public static boolean isSizeValid(String sizeMin, String sizeMax) {
        return (sizeMax != null && sizeMin != null)
                && (Integer.parseInt(sizeMax) - Integer.parseInt(sizeMin) > 0) && Integer.parseInt(sizeMin) > 0;
    }
}
