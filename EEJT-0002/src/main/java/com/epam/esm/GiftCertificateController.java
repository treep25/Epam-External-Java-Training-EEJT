package com.epam.esm;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {
    private final GiftCertificateRepository giftCertificateRepository;
    ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    public GiftCertificateController(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @GetMapping(value = "/getAll")
    public String getAll() {
        try {
            return jsonMapper.writeValueAsString(giftCertificateRepository.getAllGiftCertificates());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO incorrect type
    @PostMapping(value = "/createCertificate", headers = "content-type=text/json")
    public HttpStatus getById(@RequestBody String jsonCertificate) {
        GiftCertificate giftCertificate;
        try {
            giftCertificate = jsonMapper.readValue(jsonCertificate, GiftCertificate.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return giftCertificateRepository.createCertificate(giftCertificate);
    }
}
