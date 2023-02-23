package com.epam.esm.utils;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.utils.validation.DataValidation;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class UpdateGiftCertificateMapper {
    public static Optional<Map<String, String>> buildMapForUpdating(GiftCertificate giftCertificate) {
        log.debug("Building map for updating of giftCertificate {}",giftCertificate);
        Map<String, String> map = new HashMap<>();

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
        if (giftCertificate.getTags() != null && DataValidation.isCertificateConsistsTagsOptionalValid(giftCertificate.getTags())) {

            map.put("tags", "some tags");
        }
        log.debug("Returns map with valid fields for updating");
        return Optional.of(map);
    }
}
