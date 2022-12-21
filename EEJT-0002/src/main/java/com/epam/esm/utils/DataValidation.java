package com.epam.esm.utils;

import com.epam.esm.giftcertficate.GiftCertificate;
import com.epam.esm.tag.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;

public class DataValidation {
    public static boolean isValidCertificate(GiftCertificate giftCertificate) {
        return giftCertificate != null && ObjectUtils.isNotEmpty(giftCertificate.getName()) &&
                !StringUtils.isEmpty(giftCertificate.getName()) &&
                giftCertificate.getDuration() > 0 &&
                giftCertificate.getDescription() != null &&
                !StringUtils.isEmpty(giftCertificate.getDescription()) &&
                giftCertificate.getPrice() > 0;
    }

    public static boolean moreThenZero(long id) {
        return id > 0;
    }

    public static boolean isValidTag(Tag tag) {
        return tag != null && !StringUtils.isBlank(tag.getName()) && !StringUtils.isEmpty(tag.getName());
    }

    public static boolean isUpdatesMapValid(Map<String, ?> updatesMap) {
        boolean isFit = false;
        List<String> listOfConstantsFields = List.of("name", "description", "price", "duration");
        for (Map.Entry<String, ?> entry : updatesMap.entrySet()) {
            isFit = listOfConstantsFields.contains(entry.getKey()) && entry.getValue() != null
                    && !StringUtils.isEmpty(entry.getValue().toString())
                    && !StringUtils.isBlank(entry.getValue().toString());

            if (entry.getKey().equals("price") || entry.getKey().equals("duration")) {
                isFit = StringUtils.isNumeric(entry.getValue().toString());
            }
        }
        return isFit;
    }

    public static boolean isStringValid(String obj) {
        return obj != null && !StringUtils.isBlank(obj) && !StringUtils.isEmpty(obj);
    }
}
