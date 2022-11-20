package task2;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class InputValidation {
    public static boolean isNameValid(String name){
        if(name != null
                && name.matches("[^!@#$%^&*()+= ~]+")) {
            return true;
        }
        return false;
    }
    public static boolean isExtValid(String ext){
        if(ext != null
                &&ext.matches("[.]{1}[\\D]+")){
            return true;
        }
        return false;
    }
    public static boolean isSizeValid(String sizeMin , String sizeMax){
        if((sizeMax != null&&sizeMin!=null)
                && (Integer.parseInt(sizeMax)-Integer.parseInt(sizeMin) >0)&& Integer.parseInt(sizeMin)>0){
            return true;
        }
        return false;
    }
}
