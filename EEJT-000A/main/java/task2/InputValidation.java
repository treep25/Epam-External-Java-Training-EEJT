package task2;



import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class InputValidation {
    public static boolean isNameValid(String name){
        if(name != null
                && Pattern.matches("[^!@#$%^&*()+= ~]+", name)){
            return true;
        }
        return false;
    }
    public static boolean isExtValid(String ext){
        if(ext != null
                && Pattern.matches("[.]{1}[\\D]+", ext)){
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
//     if (!Pattern.matches("(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))", customer.getLogin()))
//    isTrue = false;
//        if (!Pattern.matches("(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?", customer.getPhone()))
//    isTrue = false;
//        if (!Pattern.matches("[0-9a-zA-Z!@#$%^&*]{6,}", customer.getPassword()))
//    isTrue = false;
//        if (!Pattern.matches("[0-9a-zA-Z!@#$%^&*]{6,}", passRep))
//    isTrue = false;
//        if(!Objects.equals(customer.getPassword(), passRep)) isTrue = false;
//        return isTrue;
}
