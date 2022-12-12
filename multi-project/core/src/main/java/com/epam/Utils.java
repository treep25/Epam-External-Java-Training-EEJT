package com.epam;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.stream.Stream;

public class Utils {
    public static boolean isAllPositiveNumbers(String... str) {
        if (ArrayUtils.isNotEmpty(str) && str.length > 0) {
            return Arrays.stream(str).allMatch(StringUtils::isPositiveNumber);
        }
        return false;
    }
}

