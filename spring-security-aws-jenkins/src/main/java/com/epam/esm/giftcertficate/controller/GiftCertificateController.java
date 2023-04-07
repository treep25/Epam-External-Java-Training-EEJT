package com.epam.esm.giftcertficate.controller;


import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.model.GiftCertificateHateoasBuilder;
import com.epam.esm.giftcertficate.service.GiftCertificateService;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    private final GiftCertificateHateoasBuilder giftCertificateHateoasBuilder;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {

        log.debug("Validation of request model of gift-certificate " + giftCertificate.toString());

        if (DataValidation.isValidCertificate(giftCertificate)) {

            GiftCertificate savedGiftCertificate = giftCertificateService.createGiftCertificate(giftCertificate);
            log.debug("Receive gift-certificate");

            CollectionModel<GiftCertificate> collectionModelSavedGiftCertificate = giftCertificateHateoasBuilder
                    .getHateoasGiftCertificateForCreating(savedGiftCertificate);
            log.debug("Return Hateoas model of current gift-certificate");

            return new ResponseEntity<>(Map.of("gift-certificate", collectionModelSavedGiftCertificate), HttpStatus.CREATED);
        }
        log.error("Gift-certificate received from request is not correct (something went wrong during the request, check your fields)");
        throw new ServerException("Something went wrong during the request, check your fields");
    }


    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model fields " + page + " " + size);
        DataValidation.validatePageAndSizePagination(page, size);
        Page<GiftCertificate> allGiftCertificates = giftCertificateService.getAll(page, size);
        log.debug("Receive all gift-certificates");

        CollectionModel<GiftCertificate> allGiftCertificatesModel = giftCertificateHateoasBuilder.getHateoasGiftCertificateForGettingAll(allGiftCertificates);
        log.debug("Return Hateoas model of all gift-certificates");

        return ResponseEntity.ok(Map.of("gift-certificates", allGiftCertificatesModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable("id") long id) {
        log.debug("Validation of request model field ID " + id);
        if (DataValidation.moreThenZero(id)) {
            GiftCertificate currentGiftCertificate = giftCertificateService.getCertificateById(id);
            log.debug("Receive gift-certificate");

            CollectionModel<GiftCertificate> giftCertificateCollectionModel = giftCertificateHateoasBuilder
                    .getHateoasGiftCertificateForGettingOne(currentGiftCertificate);
            log.debug("Return Hateoas model of all gift-certificates");

            return ResponseEntity.ok(Map.of("gift-certificate", giftCertificateCollectionModel));
        }
        log.error("The Gift Certificate ID is not valid: id = " + id);
        throw new ServerException("The Gift Certificate ID is not valid: id = " + id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@RequestBody GiftCertificate giftCertificate, @PathVariable("id") long id) {
        log.debug("Validation of request model field ID " + id);
        if (DataValidation.moreThenZero(id)) {

            log.debug("Validation of request model fields" + giftCertificate.toString());
            Optional<Map<String, String>> updatesMap = DataValidation.isGiftCertificateValidForUpdating(giftCertificate);

            if (updatesMap.isPresent()) {
                GiftCertificate updatedGiftCertificate = giftCertificateService.updateCertificate(id, giftCertificate.getTags(), updatesMap.get());
                log.debug("Receive updated gift-certificate");

                CollectionModel<GiftCertificate> giftCertificateCollectionModel = giftCertificateHateoasBuilder
                        .getHateoasGiftCertificateForUpdate(updatedGiftCertificate);
                log.debug("Return Hateoas model of gift-certificate");

                return ResponseEntity.ok(Map.of("gift-certificate", giftCertificateCollectionModel));
            }
            log.error("There are no fields to update gift-certificate");
            throw new ServerException("There are no fields to update");
        }
        log.error("The gift-certificate ID is not valid: id = " + id);
        throw new ServerException("The ID is not valid: id = " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        log.debug("Validation of request model fields " + id);
        if (DataValidation.moreThenZero(id)) {

            giftCertificateService.deleteCertificate(id);
            log.debug("deleted - return noContent");
            return ResponseEntity.noContent().build();
        }
        log.error("The Gift Certificate ID is not valid: id = " + id);
        throw new ServerException("The Gift Certificate ID is not valid: id = " + id);
    }

    @PatchMapping("update-price/{id}")
    public ResponseEntity<?> updatePrice(@PathVariable("id") long id, @RequestBody int price) {
        log.debug("Validation of request model field ID " + id);
        if (DataValidation.moreThenZero(id)) {

            log.debug("Validation of request model field price " + price);
            if (DataValidation.moreThenZero(price)) {

                GiftCertificate updatedGiftCertificate = giftCertificateService.updatePrice(id, price);
                log.debug("Receive updated gift-certificate");

                CollectionModel<GiftCertificate> giftCertificateCollectionModel = giftCertificateHateoasBuilder
                        .getHateoasGiftCertificateForUpdatingPrice(updatedGiftCertificate);
                log.debug("Return Hateoas model of gift-certificate");

                return ResponseEntity.ok(Map.of("gift-certificate", giftCertificateCollectionModel));
            }
            log.error("The PRICE is not valid: price = " + price);
            throw new ServerException("The PRICE is not valid: price = " + price);
        }
        log.error("The ID is not valid: id = " + id);
        throw new ServerException("The ID is not valid: id = " + id);
    }

    @GetMapping("search/tag-name")
    public ResponseEntity<?> readByTagName(@RequestParam("name") String tagName,
                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model of tag name " + tagName);
        if (DataValidation.isStringValid(tagName)) {

            log.debug("Validation of request model fields " + page + " " + size);
            DataValidation.validatePageAndSizePagination(page, size);

            Page<GiftCertificate> giftCertificatesByTagName = giftCertificateService.getCertificatesByTagName(tagName, page, size);
            log.debug("Receive gift-certificates");

            PagedModel<GiftCertificate> giftCertificatePagedModel = giftCertificateHateoasBuilder
                    .getHateoasGiftCertificateForGettingByTagName(giftCertificatesByTagName);
            log.debug("Return Hateoas model of gift-certificates");

            return ResponseEntity.ok(Map.of("gift-certificates", giftCertificatePagedModel));
        }
        log.error("tag name is not valid " + tagName);
        throw new ServerException("tag name is not valid " + tagName);
    }

    @GetMapping("search/gift-certificate-name")
    public ResponseEntity<?> readByCertificateNameOrByPartOfName(@RequestParam("name") String partOfName,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model of gift-certificate name " + partOfName);
        if (DataValidation.isStringValid(partOfName)) {
            log.debug("Validation of request model fields " + page + " " + size);
            DataValidation.validatePageAndSizePagination(page, size);

            Page<GiftCertificate> giftCertificatesByName = giftCertificateService.getCertificatesByNameOrByPartOfName(partOfName, page, size);
            log.debug("Receive gift-certificates");

            PagedModel<GiftCertificate> giftCertificatePagedModel = giftCertificateHateoasBuilder
                    .getHateoasGiftCertificateForGettingGiftCertificatesByNameOrByPartOfName(giftCertificatesByName);
            log.debug("Return Hateoas model of gift-certificates");

            return ResponseEntity.ok(Map.of("gift-certificates", giftCertificatePagedModel));
        }
        log.error("gift-certificate name is not valid " + partOfName);
        throw new ServerException("gift-certificate name is not valid " + partOfName);
    }

    @GetMapping("search/sort-date")
    public ResponseEntity<?> readSortedByDate(@RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection,
                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model sortDir " + sortDirection);
        if (DataValidation.isStringValid(sortDirection)) {
            if (DataValidation.isSortingTypeContains(sortDirection)) {

                log.debug("Validation of request model fields " + page + " " + size);
                DataValidation.validatePageAndSizePagination(page, size);

                Page<GiftCertificate> giftCertificatesSortedByDate = giftCertificateService.getCertificatesSortedByDate(sortDirection, page, size);
                log.debug("Receive gift-certificates");

                PagedModel<GiftCertificate> giftCertificatePagedModel = giftCertificateHateoasBuilder
                        .getHateoasGiftCertificateForGettingGiftCertificatesSortedByDate(giftCertificatesSortedByDate);
                log.debug("Return Hateoas model of gift-certificates");

                return ResponseEntity.ok(Map.of("gift-certificates", giftCertificatePagedModel));
            }
            log.error("type should be only DESC or ASC " + sortDirection);
            throw new ServerException("type should be only DESC or ASC " + sortDirection);
        }
        log.error("incorrect data " + sortDirection);
        throw new ServerException("incorrect data " + sortDirection);
    }

    @GetMapping("search/tag-name/cost")
    public ResponseEntity<?> readByTagsAndPrice(@RequestParam("tag1") String firstTagName,
                                                @RequestParam("tag2") String secondTagName,
                                                @RequestParam("price") int price,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model tagNames " + firstTagName + " " + secondTagName);
        if (DataValidation.isStringValid(firstTagName) && DataValidation.isStringValid(secondTagName)) {
            log.debug("Validation of request model price " + price);
            if (DataValidation.moreThenZero(price)) {

                log.debug("Validation of request model fields " + page + " " + size);
                DataValidation.validatePageAndSizePagination(page, size);

                Page<GiftCertificate> giftCertificatesByTagsAndPrice = giftCertificateService
                        .getCertificatesByTagsAndPrice(firstTagName, secondTagName, price, page, size);
                log.debug("Receive gift-certificates");

                PagedModel<GiftCertificate> giftCertificatePagedModel = giftCertificateHateoasBuilder
                        .getHateoasGiftCertificateForGettingGiftCertificatesByTagsAndPrice(giftCertificatesByTagsAndPrice);
                log.debug("Return Hateoas model of gift-certificates");

                return ResponseEntity.ok(Map.of("gift-certificates", giftCertificatePagedModel));
            }
            log.error("price is not valid (price = " + price + ")");
            throw new ServerException("price is not valid (price = " + price + ")");
        }
        log.error("tag name is not valid " + firstTagName + " and " + secondTagName);
        throw new ServerException("tag name is not valid " + firstTagName + " and " + secondTagName);
    }

    @GetMapping("search/sort-name-date")
    public ResponseEntity<?> readSortedByDateAndByName(@RequestParam(value = "firstSortDirection", defaultValue = "ASC") String firstSortDirection,
                                                       @RequestParam(value = "secondSortDirection", defaultValue = "ASC") String secondSortDirection,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model sortDirs " + firstSortDirection + " " + secondSortDirection);
        if (DataValidation.isStringValid(firstSortDirection) && DataValidation.isStringValid(secondSortDirection)) {
            if (DataValidation.isSortingTypeContains(firstSortDirection) && DataValidation.isSortingTypeContains(secondSortDirection)) {

                log.debug("Validation of request model fields " + page + " " + size);
                DataValidation.validatePageAndSizePagination(page, size);

                Page<GiftCertificate> giftCertificatesSortedByDateAndByName = giftCertificateService
                        .getGiftCertificatesSortedByDateAndByName(firstSortDirection, secondSortDirection, page, size);
                log.debug("Receive gift-certificates");

                PagedModel<GiftCertificate> giftCertificatePagedModel = giftCertificateHateoasBuilder
                        .getHateoasGiftCertificateForGettingGiftCertificatesSortedByDateAndByName(giftCertificatesSortedByDateAndByName);
                log.debug("Return Hateoas model of gift-certificates");

                return ResponseEntity.ok(Map.of("gift-certificates", giftCertificatePagedModel));
            }
            log.error("type should be only DESC or ASC " + firstSortDirection + " " + secondSortDirection);
            throw new ServerException("type should be only DESC or ASC " + firstSortDirection + " " + secondSortDirection);
        }
        log.error("incorrect data " + firstSortDirection + " " + secondSortDirection);
        throw new ServerException("incorrect data " + firstSortDirection + " " + secondSortDirection);
    }
}
