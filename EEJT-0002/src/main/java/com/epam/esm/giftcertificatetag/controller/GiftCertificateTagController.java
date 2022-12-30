package com.epam.esm.giftcertificatetag.controller;

import com.epam.esm.giftcertificatetag.service.GiftCertificateTagService;
import com.epam.esm.utils.validation.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/tag-with-certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateTagController {

    private final GiftCertificateTagService giftCertificateTagService;

    @Autowired
    public GiftCertificateTagController(GiftCertificateTagService giftCertificateTagService) {
        this.giftCertificateTagService = giftCertificateTagService;
    }


    @GetMapping(value = "{name}")
    public ResponseEntity<?> getByTagName(@PathVariable String name) {
        if (DataValidation.isStringValid(name)) {
            return ResponseEntity.ok(giftCertificateTagService.getGiftCertificatesByTagName(name));
        }
        throw new IllegalArgumentException("tag name is not valid");
    }

    @GetMapping(value = "gift-certificate/{partOfName}")
    public ResponseEntity<?> getByPartOfName(@PathVariable String partOfName) {
        if (DataValidation.isStringValid(partOfName)) {
            return ResponseEntity.ok(giftCertificateTagService.getGiftCertificatesAndTagsByNameOrByPartOfName(partOfName));
        }
        throw new IllegalArgumentException("Gift certificate name is not valid");
    }

    @GetMapping(value = "sort-date/{method}")
    public ResponseEntity<?> sortingAscDescByDate(@PathVariable String method) {
        if (DataValidation.isStringValid(method)) {
            if (DataValidation.isSortingTypeContain(method)) {
                return ResponseEntity.ok(giftCertificateTagService.sortingAscDescByDate(method));
            }
            throw new IllegalArgumentException("type should be only DESC/ASC without register");
        }
        throw new IllegalArgumentException("Incorrect data");
    }

    @GetMapping(value = "sort-date-name/{method1}/{method2}")
    public ResponseEntity<?> sortingByDateByName(@PathVariable String method1, @PathVariable String method2) {
        if (DataValidation.isStringValid(method1) && DataValidation.isStringValid(method1)) {
            if (DataValidation.isSortingTypeContain(method1) && DataValidation.isStringValid(method1)) {
                return ResponseEntity.ok(giftCertificateTagService.sortingAscDescByDateAndByName(method1, method2));
            }
            throw new IllegalArgumentException("type should be only DESC/ASC without register");
        }
        throw new IllegalArgumentException("Incorrect data ");
    }
}
