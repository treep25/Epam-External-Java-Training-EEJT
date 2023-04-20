package com.epam.esm.commercetools.certificate.repository;

import com.epam.esm.commercetools.PagePaginationBuilder;
import com.epam.esm.commercetools.certificate.model.CommerceGiftCertificate;
import com.epam.esm.giftcertficate.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateCommerceRepository {
    CommerceGiftCertificate create(GiftCertificate giftCertificate);

    List<CommerceGiftCertificate> read(PagePaginationBuilder pageRequest);

    CommerceGiftCertificate readById(String id);

    CommerceGiftCertificate updatePrice(String id, long version, int price);

    CommerceGiftCertificate updateGiftCertificate(String id, long version, GiftCertificate giftCertificate);

    void delete(String id, long version);

    void preDeleteActionSetPublishFalse(String id, long version);

    long getProductVersion(String id);

    List<CommerceGiftCertificate> findByName(String name, PagePaginationBuilder pageRequest);

    List<CommerceGiftCertificate> findByTagName(String tagName, PagePaginationBuilder pageRequest);
}
