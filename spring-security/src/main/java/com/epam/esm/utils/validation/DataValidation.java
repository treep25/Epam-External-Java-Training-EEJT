package com.epam.esm.utils.validation;

import com.epam.esm.authentication.model.AuthenticationRequest;
import com.epam.esm.authentication.model.RegisterRequest;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.UpdateGiftCertificateMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Slf4j
public class DataValidation {
    private static final UpdateGiftCertificateMapper updateGiftCertificateMapper = new UpdateGiftCertificateMapper();

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
        String answer = validateGiftCertificateForUpdating(giftCertificate);

        if (answer.isEmpty()) {

            return updateGiftCertificateMapper.buildMapForUpdating(giftCertificate);
        }
        log.error("Error during mapping gift-certificate Something went wrong, check this fields " + answer);
        throw new ServerException("Something went wrong, check this fields " + answer);
    }

    public static boolean isStringValid(String obj) {
        return StringUtils.isNotBlank(obj) && !StringUtils.isNumeric(obj);
    }

    public static boolean isSortingTypeContains(String method) {
        return List.of(SortingTypes.ASC.name(), SortingTypes.DESC.name()).contains(method.toUpperCase(Locale.ROOT));
    }

    public static void validatePageAndSizePagination(int page, int size) {
        if (page >= 0) {
            if (size >= 1) {
                return;
            }
            log.error("Page size must not be less than one size = " + size);
            throw new ServerException("page size must not be less than one size = " + size);
        }
        log.error("Page index must not be less than zero page = " + page);
        throw new ServerException("page index must not be less than zero page = " + page);
    }
    public static boolean validateAuthenticationRequest(AuthenticationRequest request){
        return isStringValid(request.getUsername()) && request.getPassword().matches(".{6,}");
    }

    public static boolean validateRegisterRequest(RegisterRequest request){
        return isStringValid(request.getUsername()) && request.getPassword().matches(".{6,}");

    }
}
