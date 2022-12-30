package com.epam.esm.giftcertficate.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateRepository {
    List<GiftCertificate> getAllGiftCertificates();

    void createCertificate(GiftCertificate giftCertificate);

    List<GiftCertificate> getCertificateById(long id);

    int deleteCertificate(long id);

    int updateGiftCertificate(long id, Optional<Map<String, String>> updatesMap);

    long getIdByGiftCertificate(GiftCertificate giftCertificate);

    boolean isGiftCertificateExist(GiftCertificate giftCertificate);

    void createGiftCertificateTagList(List<Long> tagsId, long giftCertificateId);
}
