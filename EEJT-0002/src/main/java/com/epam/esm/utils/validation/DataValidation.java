package com.epam.esm.utils.validation;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class DataValidation {
    public static boolean isValidCertificate(GiftCertificate giftCertificate) {
        return giftCertificate != null && isStringValid(giftCertificate.getName()) &&
                giftCertificate.getDuration() > 0 &&
                isStringValid(giftCertificate.getDescription()) &&
                giftCertificate.getPrice() > 0 && isCertificateConsistsTagsOptionalValid(giftCertificate.getTags());
    }

    public static boolean isCertificateConsistsTagsOptionalValid(List<Tag> tagList) {
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

    public static Optional<Map<String, String>> isGiftCertificateValidForUpdating(GiftCertificate giftCertificate) {
        Map<String, String> map = new HashMap<>();
        if (giftCertificate != null) {
            if (giftCertificate.getName() != null) {
                if (StringUtils.isBlank(giftCertificate.getName()) || StringUtils.isEmpty(giftCertificate.getName())) {
                    return Optional.empty();
                }
                map.put("name", giftCertificate.getName());
            }
            if (giftCertificate.getDescription() != null) {
                if (StringUtils.isBlank(giftCertificate.getDescription()) || StringUtils.isEmpty(giftCertificate.getDescription())) {
                    return Optional.empty();
                }
                map.put("description", giftCertificate.getDescription());
            }
            if (giftCertificate.getPrice() != null) {
                if (StringUtils.isBlank(giftCertificate.getPrice().toString()) || StringUtils.isEmpty(giftCertificate.getPrice().toString())) {
                    return Optional.empty();
                }
                map.put("price", giftCertificate.getPrice().toString());
            }
            if (giftCertificate.getDuration() != null) {
                if (StringUtils.isBlank(giftCertificate.getDuration().toString()) || StringUtils.isEmpty(giftCertificate.getDuration().toString())) {
                    return Optional.empty();
                }
                map.put("duration", giftCertificate.getDuration().toString());
            }
            if (giftCertificate.getTags() != null) {
                if (!isCertificateConsistsTagsOptionalValid(giftCertificate.getTags())) {
                    return Optional.empty();
                }
            }
        }
        return Optional.of(map);
    }

    public static boolean isStringValid(String obj) {
        return obj != null && !StringUtils.isBlank(obj) && !StringUtils.isEmpty(obj) && !StringUtils.isNumeric(obj);
    }

    public static boolean isSortingTypeContain(String method) {
        return List.of("ASC", "DESC").contains(method.toUpperCase(Locale.ROOT));
    }
}
