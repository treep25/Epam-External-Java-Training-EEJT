package com.epam.esm.giftcertficate.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateRepository {
    List<GiftCertificate> getAllGiftCertificates();

    boolean createCertificate(GiftCertificate giftCertificate);

    List<GiftCertificate> getCertificateById(long id);

    boolean deleteCertificate(long id);

    boolean updateGiftCertificate(long id, Map<String, String> updatesMap);

    long getIdByGiftCertificate(GiftCertificate giftCertificate);

    boolean isGiftCertificateExist(GiftCertificate giftCertificate);

    boolean createGiftCertificateTagRelationship(List<Long> tagsId, long giftCertificateId);

    boolean deleteGiftCertificateTagRelationship(List<Long> tagsId, long giftCertificateId);
}
