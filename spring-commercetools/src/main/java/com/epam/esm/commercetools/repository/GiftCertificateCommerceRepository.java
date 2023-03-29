package com.epam.esm.commercetools.repository;

import com.epam.esm.commercetools.model.CommerceGiftCertificate;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface GiftCertificateCommerceRepository {
    CommerceGiftCertificate create(GiftCertificate giftCertificate);

    List<CommerceGiftCertificate> read(PageRequest pageRequest);

    CommerceGiftCertificate readById(String id);

    CommerceGiftCertificate updatePrice(String id, long version, int price);

    CommerceGiftCertificate updateGiftCertificate(String id, long version, GiftCertificate giftCertificate);

    void delete(String id, long version);

    void preDeleteActionSetPublishFalse(String id, long version);

    long getProductVersion(String id);


}
