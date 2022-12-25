package com.epam.esm.giftcertificatetag;

import com.epam.esm.giftcertficate.GiftCertificate;

import java.util.List;

public interface GiftCertificateTagRepository {
    List<GiftCertificate> getGiftCertificateTagsByTagName(String tagName);

    List<GiftCertificate> getGiftCertificatesAndTagsByNameOrByPartOfName(String partOfName);

    List<GiftCertificate> sortingAscDescByDate(String method);

    List<GiftCertificate> sortingAscDescByDateAndByName(String method1, String method2);
}
