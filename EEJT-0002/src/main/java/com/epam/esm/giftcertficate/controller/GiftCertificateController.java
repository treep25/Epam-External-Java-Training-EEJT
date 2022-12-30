package com.epam.esm.giftcertficate.controller;

import com.epam.esm.giftcertficate.service.GiftCertificateService;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.utils.validation.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCertificate(@RequestBody GiftCertificate giftCertificate) {

        if (DataValidation.isValidCertificate(giftCertificate)) {
            giftCertificateService.createCertificate(giftCertificate);

            return new ResponseEntity<>(Map.of("status", HttpStatus.CREATED), HttpStatus.CREATED);
        }
        throw new IllegalArgumentException("Something went wrong during the request, check your fields");
    }

    @GetMapping()
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(Map.of("gift certificates", giftCertificateService.getAllCertificates()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        if (DataValidation.moreThenZero(id)) {

            return ResponseEntity.ok(Map.of("gift certificate", giftCertificateService.getCertificateById(id)));
        }
        throw new IllegalArgumentException("incorrect id=" + id);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCertificate(@RequestBody GiftCertificate giftCertificate, @PathVariable long id) {
        if (DataValidation.moreThenZero(id)) {
            Optional<Map<String, String>> updatesMap = DataValidation.isGiftCertificateValidForUpdating(giftCertificate);
            if (updatesMap.isPresent()) {

                return ResponseEntity.ok(Map.of("gift certificate", giftCertificateService.updateGiftCertificate(id, giftCertificate.getTags(), updatesMap)));
            }
            throw new IllegalArgumentException("Something went wrong during the request, check your fields");
        }
        throw new IllegalArgumentException("incorrect id=" + id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable long id) {
        if (DataValidation.moreThenZero(id)) {
            giftCertificateService.deleteGiftCertificate(id);

            return new ResponseEntity<>(Map.of("status", HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
        }
        throw new IllegalArgumentException("incorrect id=" + id);
    }
}
