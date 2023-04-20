package com.epam.esm.commercetools.certificate.service;

import com.epam.esm.commercetools.PagePaginationBuilder;
import com.epam.esm.commercetools.certificate.model.CommerceGiftCertificate;
import com.epam.esm.commercetools.certificate.repository.GiftCertificateCommerceRepository;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableTransactionManagement
public class GiftCertificateCommerceService {

    private final GiftCertificateCommerceRepository giftCertificateCommerceRepository;

    @Transactional
    public CommerceGiftCertificate createGiftCertificate(GiftCertificate giftCertificate) {
        log.info("Service receives gift-certificate for creating {} Transaction has been started"
                , giftCertificate.toString());
        return giftCertificateCommerceRepository.create(giftCertificate);
    }

    public Page<CommerceGiftCertificate> getAll(PagePaginationBuilder pageRequest) {
        log.info("Service receives params for getting all");

        log.debug("Service returns all gift-certificates");
        return new PageImpl<>(giftCertificateCommerceRepository.read(pageRequest));
    }

    public CommerceGiftCertificate getCertificateById(String id) {
        log.info("Service receives params for getting gift-certificate by id");
        log.debug("Service returns one gift-certificate");

        return giftCertificateCommerceRepository.readById(id);
    }


    @Transactional
    public CommerceGiftCertificate updateCertificate(String id, GiftCertificate giftCertificate) {
        log.info("Service receives gift-certificate`s params for updating");

        return giftCertificateCommerceRepository
                .updateGiftCertificate(id, giftCertificateCommerceRepository.getProductVersion(id), giftCertificate);
    }

    public CommerceGiftCertificate updatePrice(String id, int price) {
        log.info("Service receives gift-certificate`s params for updating price");

        return giftCertificateCommerceRepository
                .updatePrice(id, giftCertificateCommerceRepository.getProductVersion(id), price);
    }

    public void deleteCertificate(String id) {
        log.info("Service receives params for deleting");
        log.debug("Checking that gift-certificate exists");

        giftCertificateCommerceRepository
                .preDeleteActionSetPublishFalse(id, giftCertificateCommerceRepository.getProductVersion(id));

        giftCertificateCommerceRepository.delete(id, giftCertificateCommerceRepository.getProductVersion(id));
    }

    public Page<CommerceGiftCertificate> findByName(String name, PagePaginationBuilder pageRequest) {
        log.info("Service receives params for searching by name {}", name);

        return new PageImpl<>(giftCertificateCommerceRepository.findByName(name, pageRequest));
    }

    public Page<CommerceGiftCertificate> findByTagName(String name, PagePaginationBuilder pageRequest) {
        log.info("Service receives params for searching by tag name {}", name);
        return new PageImpl<>(giftCertificateCommerceRepository.findByTagName(name, pageRequest));
    }
}
