package com.epam.esm.controller;

import com.epam.esm.ResponseEntity;
import com.epam.esm.ResponseStatus;
import com.epam.esm.exception.BadRequestBody;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity getAll() {
        return new ResponseEntity(giftCertificateService.getAllCertificates(), HttpStatus.OK);
    }

    @PostMapping(value = "/createCertificate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseStatus createCertificate(@RequestBody GiftCertificate giftCertificate) {
        if (giftCertificate != null) {
            giftCertificateService.createCertificate(giftCertificate);
            return new ResponseStatus(HttpStatus.CREATED);
        }
        throw new BadRequestBody();
    }

    //TODO remake this cannot find solving
    @GetMapping(value = "/certificate/{id}")
    public ResponseEntity getById(@PathVariable long id) {
        if (id > 0) {
            return new ResponseEntity(giftCertificateService.getCertificateById(id), HttpStatus.OK);
        }
        throw new BadRequestBody();
    }
}
