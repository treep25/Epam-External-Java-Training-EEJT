package com.epam.esm.giftcertficate;

import com.epam.esm.giftcertficate.GiftCertificate;
import com.epam.esm.tag.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;

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
