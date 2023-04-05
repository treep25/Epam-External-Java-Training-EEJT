package com.epam.esm.commercetools.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface GiftCertificateCommerceRepository<T> {
    T create(GiftCertificate giftCertificate);

    List<T> read(PageRequest pageRequest);

    T readById(String id);

    T updatePrice(String id, long version, int price);

    T updateGiftCertificate(String id, long version, GiftCertificate giftCertificate);

    void delete(String id, long version);

    void preDeleteActionSetPublishFalse(String id, long version);

    long getProductVersion(String id);

    List<T> findByName(String name);

    List<T> findByTagName(String tagName, PageRequest pageRequest);
}
