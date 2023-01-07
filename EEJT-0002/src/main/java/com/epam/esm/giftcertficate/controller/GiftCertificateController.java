package com.epam.esm.giftcertficate.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.service.GiftCertificateService;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.utils.validation.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
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

    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody GiftCertificate giftCertificate) {

        if (DataValidation.isValidCertificate(giftCertificate)) {
            giftCertificateService.createCertificate(giftCertificate);

            return new ResponseEntity<>(Map.of("status", HttpStatus.CREATED), HttpStatus.CREATED);
        }
        throw new ServerException("Something went wrong during the request, check your fields");
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(Map.of("gift certificates", giftCertificateService.getAllCertificates()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            return ResponseEntity.ok(Map.of("gift certificate", giftCertificateService.getCertificateById(id)));
        }
        throw new ServerException("incorrect id=" + id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@RequestBody GiftCertificate giftCertificate, @PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {
            Optional<Map<String, String>> updatesMap = DataValidation.isGiftCertificateValidForUpdating(giftCertificate);
            if (!updatesMap.get().isEmpty()) {
                return ResponseEntity.ok(giftCertificateService.updateGiftCertificate(id, giftCertificate.getTags(), updatesMap.get()));
            }
            throw new ServerException("There are no fields to update");
        }
        throw new ServerException("Incorrect id=" + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {
            giftCertificateService.deleteGiftCertificate(id);

            return new ResponseEntity<>(Map.of("status", HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
        }
        throw new ServerException("incorrect id=" + id);
    }
}
