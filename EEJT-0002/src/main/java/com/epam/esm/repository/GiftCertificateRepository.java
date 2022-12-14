package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface GiftCertificateRepository {
    List<GiftCertificate> getAllGiftCertificates();

    HttpStatus createCertificate(GiftCertificate giftCertificate);

    GiftCertificate getCertificateById(long id);
}
