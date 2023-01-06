package com.epam.esm.giftcertificatetag.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
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


    @GetMapping("{name}")
    public ResponseEntity<?> getByTagName(@PathVariable("name") String name) {
        if (DataValidation.isStringValid(name)) {
            return ResponseEntity.ok(giftCertificateTagService.getGiftCertificatesByTagName(name));
        }
        throw new ServerException("tag name is not valid");
    }

    @GetMapping("gift-certificate/{partOfName}")
    public ResponseEntity<?> getByPartOfName(@PathVariable("partOfName") String partOfName) {
        if (DataValidation.isStringValid(partOfName)) {
            return ResponseEntity.ok(giftCertificateTagService.getGiftCertificatesAndTagsByNameOrByPartOfName(partOfName));
        }
        throw new ServerException("Gift certificate name is not valid");
    }

    @GetMapping("sort-date/{sortingType}")
    public ResponseEntity<?> sortingAscDescByDate(@PathVariable("sortingType") String sortingType) {
        if (DataValidation.isStringValid(sortingType)) {
            if (DataValidation.isSortingTypeContain(sortingType)) {
                return ResponseEntity.ok(giftCertificateTagService.sortingAscDescByDate(sortingType));
            }
            throw new ServerException("type should be only DESC or ASC without register");
        }
        throw new ServerException("Incorrect data");
    }

    @GetMapping(value = "sort-date-name/{sortingType1}/{sortingType2}")
    public ResponseEntity<?> sortingByDateByName(@PathVariable("sortingType1") String sortingType1, @PathVariable("sortingType2") String sortingType2) {
        if (DataValidation.isStringValid(sortingType1) && DataValidation.isStringValid(sortingType2)) {
            if (DataValidation.isSortingTypeContain(sortingType1) && DataValidation.isSortingTypeContain(sortingType2)) {
                return ResponseEntity.ok(giftCertificateTagService.sortingAscDescByDateAndByName(sortingType1, sortingType2));
            }
            throw new ServerException("type should be only DESC or ASC without register");
        }
        throw new ServerException("Incorrect data");
    }
}
