package com.epam.esm.giftcertificatetag.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateTagRepository {
    List<GiftCertificate> getGiftCertificateTagsByTagName(String tagName);

    List<GiftCertificate> getGiftCertificatesAndTagsByNameOrByPartOfName(String partOfName);

    List<GiftCertificate> getGiftCertificatesSortedByDate(String sortDirection);

    List<GiftCertificate> getGiftCertificatesSortedByDateAndByName(String firstSortDirection, String secondSortDirection);
}
