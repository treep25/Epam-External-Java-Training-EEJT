package com.epam.esm.utils;

import com.epam.esm.giftcertficate.GiftCertificate;
import com.epam.esm.tag.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

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
        return tag != null && StringUtils.isBlank(tag.getName()) && StringUtils.isEmpty(tag.getName());
    }
}
