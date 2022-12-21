package com.epam.esm.giftcertficate;

import com.epam.esm.utils.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping(value = "/createCertificate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCertificate(@RequestBody GiftCertificate giftCertificate) {
        if (DataValidation.isValidCertificate(giftCertificate)) {
            giftCertificateService.createCertificate(giftCertificate);
            return new ResponseEntity<>(Map.of("status", HttpStatus.CREATED), HttpStatus.CREATED);
        }
        throw new IllegalArgumentException("Something went wrong during the request, check your fields");
    }

    @GetMapping(value = "/getAll")
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

    @PatchMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCertificate(@RequestBody Map<String, ?> updatesMap, @PathVariable long id) {
        if (DataValidation.moreThenZero(id)) {
            if (DataValidation.isUpdatesMapValid(updatesMap)) {
                giftCertificateService.updateGiftCertificate(id, updatesMap);
                return ResponseEntity.ok(Map.of("status", HttpStatus.OK));
            }
            throw new IllegalArgumentException("Something went wrong during the request, check your fields");
        }
        throw new IllegalArgumentException("incorrect id=" + id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable long id) {
        if (DataValidation.moreThenZero(id)) {
            giftCertificateService.deleteGiftCertificate(id);
            return ResponseEntity.ok(Map.of("status", HttpStatus.OK));
        }
        throw new IllegalArgumentException("incorrect id=" + id);
    }
}
