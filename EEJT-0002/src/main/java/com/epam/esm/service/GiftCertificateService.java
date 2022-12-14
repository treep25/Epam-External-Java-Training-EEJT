package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;


    @Autowired
    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    public List<GiftCertificate> getAllCertificates() {
        return giftCertificateRepository.getAllGiftCertificates();
    }

    public void createCertificate(GiftCertificate giftCertificate) {
        giftCertificateRepository.createCertificate(giftCertificate);
    }

    //TODO remake this cannot find solving
    public GiftCertificate getCertificateById(long id) {
        return giftCertificateRepository.getCertificateById(id);
    }


}
