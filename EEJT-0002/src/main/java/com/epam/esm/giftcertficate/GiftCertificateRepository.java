package com.epam.esm.giftcertficate;

import com.epam.esm.giftcertficate.GiftCertificate;
import com.epam.esm.tag.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

public interface GiftCertificateRepository {
    List<GiftCertificate> getAllGiftCertificates();

    void createCertificate(GiftCertificate giftCertificate);

    List<GiftCertificate> getCertificateById(long id);

    int deleteCertificate(long id);

    int updateGiftCertificate(long id, Map<String, ?> updatesMap);

    long getIdByGiftCertificate(GiftCertificate giftCertificate);

    void createGiftCertificateTag(long tagId, long giftCertificateId);

    boolean isGiftCertificateExist(GiftCertificate giftCertificate);

    int deleteGiftCertificateTag(long GiftCertificateId);

    Optional<String> getGiftCertificateNameById(long id);
}
