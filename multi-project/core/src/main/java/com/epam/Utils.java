package com.epam;

import com.epam.StringUtils;

public class Utils {
    public static boolean isAllPositiveNumbers(String... str) {
        if (str.length > 0) {
            boolean flag = false;
            for (String number : str) {
                flag = StringUtils.isPositiveNumber(number);
            }
            return flag;
        }
        return false;
    }
}

