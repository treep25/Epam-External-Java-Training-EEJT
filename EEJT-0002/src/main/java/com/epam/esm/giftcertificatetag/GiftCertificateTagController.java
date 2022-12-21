package com.epam.esm.giftcertificatetag;

import com.epam.esm.utils.DataValidation;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "/tagWithCertificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateTagController {

    private final GiftCertificateTagService giftCertificateTagService;
    private final List<String> listOfSortingTypes = List.of("ASC", "DESC");

    @Autowired
    public GiftCertificateTagController(GiftCertificateTagService giftCertificateTagService) {
        this.giftCertificateTagService = giftCertificateTagService;
    }

    private boolean isSortingTypeContain(String method) {
        return listOfSortingTypes.contains(method.toUpperCase(Locale.ROOT));
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(giftCertificateTagService.getGiftCertificatesAndTags());
    }

    @GetMapping(value = "get/{partOfName}")
    public ResponseEntity<?> getByTagNameOrPartOfName(@PathVariable String partOfName) {
        if (DataValidation.isStringValid(partOfName)) {
            return ResponseEntity.ok(giftCertificateTagService.getGiftCertificatesAndTagsByNameOrByPartOfName(partOfName));
        }
        throw new IllegalArgumentException("tag name is nt valid");
    }

    @GetMapping(value = "sortDate/{method}")
    public ResponseEntity<?> sortingAscDescByDate(@PathVariable String method) {
        if (DataValidation.isStringValid(method)) {
            if (isSortingTypeContain(method)) {
                return ResponseEntity.ok(giftCertificateTagService.sortingAscDescByDate(method));
            }
            throw new IllegalArgumentException("type should be only DESC/ASC without register");
        }
        throw new IllegalArgumentException("Incorrect data ");
    }

    @GetMapping(value = "sortDateName/{method}")
    public ResponseEntity<?> sortingByDateByName(@PathVariable String method) {
        if (DataValidation.isStringValid(method)) {
            if (isSortingTypeContain(method)) {
                return ResponseEntity.ok(giftCertificateTagService.sortingAscDescByDateAndByName(method));
            }
            throw new IllegalArgumentException("type should be only DESC/ASC without register");
        }
        throw new IllegalArgumentException("Incorrect data ");
    }
}
