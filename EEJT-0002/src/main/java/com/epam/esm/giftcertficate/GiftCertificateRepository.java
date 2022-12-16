package com.epam.esm.giftcertficate;

import com.epam.esm.giftcertficate.GiftCertificate;

import java.util.List;
import java.util.Map;

public interface GiftCertificateRepository {
    List<GiftCertificate> getAllGiftCertificates();

    void createCertificate(GiftCertificate giftCertificate);

    GiftCertificate getCertificateById(long id);

    int deleteCertificate(long id);

    int updateGiftCertificate(long id, Map<String, ?> updatesMap);


}
