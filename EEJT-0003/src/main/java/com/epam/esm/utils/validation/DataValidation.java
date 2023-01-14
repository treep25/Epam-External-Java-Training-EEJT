package com.epam.esm.utils.validation;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class DataValidation {
    public static boolean isValidCertificate(GiftCertificate giftCertificate) {
        return isStringValid(giftCertificate.getName()) &&
                giftCertificate.getDurationDays() != null && giftCertificate.getDurationDays() >= 0 &&
                isStringValid(giftCertificate.getDescription()) &&
                giftCertificate.getPrice() != null &&
                giftCertificate.getPrice() >= 0 && isCertificateConsistsTagsOptionalValid(giftCertificate.getTags());
    }

    public static boolean isCertificateConsistsTagsOptionalValid(Set<Tag> tagList) {
        if (tagList != null) {
            for (Tag tag : tagList) {
                if (!isValidTag(tag)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean moreThenZero(long id) {
        return id > 0;
    }

    public static boolean isValidTag(Tag tag) {
        return tag != null && isStringValid(tag.getName());
    }

    private static String validateGiftCertificateForUpdating(GiftCertificate giftCertificate) {
        StringJoiner answer = new StringJoiner(",");
        if (giftCertificate != null) {
            if (giftCertificate.getName() != null && StringUtils.isNumeric(giftCertificate.getName())) {
                answer.add("name " + giftCertificate.getName());
            }
            if (!isCertificateConsistsTagsOptionalValid(giftCertificate.getTags())) {
                answer.add("tags" + giftCertificate.getTags().toString());
            }
            return answer.toString();
        }
        return "Gift certificate cannot be null for updating";
    }

    public static Optional<Map<String, String>> isGiftCertificateValidForUpdating(GiftCertificate giftCertificate) {

        Map<String, String> map = new HashMap<>();
        String answer = validateGiftCertificateForUpdating(giftCertificate);

        if (answer.isEmpty()) {
            if (giftCertificate.getName() != null) {

                map.put("name", giftCertificate.getName());
            }
            if (giftCertificate.getDescription() != null) {

                map.put("description", giftCertificate.getDescription());
            }
            if (giftCertificate.getPrice() != null && giftCertificate.getPrice() >= 0) {

                map.put("price", String.valueOf(giftCertificate.getPrice()));
            }
            if (giftCertificate.getDurationDays() != null && giftCertificate.getDurationDays() >= 0) {

                map.put("duration", String.valueOf(giftCertificate.getDurationDays()));
            }
            if (giftCertificate.getTags() != null && isCertificateConsistsTagsOptionalValid(giftCertificate.getTags())) {

                map.put("tags", "some tags");
            }
            return Optional.of(map);
        }
        throw new ServerException("Something went wrong, check this fields " + answer);
    }

    public static boolean isStringValid(String obj) {
        return StringUtils.isNotBlank(obj) && !StringUtils.isNumeric(obj);
    }

    public static boolean isSortingTypeContains(String method) {
        return List.of(SortingTypes.ASC.name(), SortingTypes.DESC.name()).contains(method.toUpperCase(Locale.ROOT));
    }
}
