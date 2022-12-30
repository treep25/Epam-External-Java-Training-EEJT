package com.epam.esm.giftcertificatetag.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateTagRepository {
    List<GiftCertificate> getGiftCertificateTagsByTagName(String tagName);

    List<GiftCertificate> getGiftCertificatesAndTagsByNameOrByPartOfName(String partOfName);

    List<GiftCertificate> sortingAscDescByDate(String method);

    List<GiftCertificate> sortingAscDescByDateAndByName(String method1, String method2);
}
