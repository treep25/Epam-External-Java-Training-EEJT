package com.epam.esm.giftcertficate.controller;


import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.model.GiftCertificateHateoas;
import com.epam.esm.giftcertficate.service.GiftCertificateService;
import com.epam.esm.tag.controller.TagController;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    private final PagedResourcesAssembler<GiftCertificate> representationModelAssembler;

    private final GiftCertificateHateoas giftCertificateHateoas = new GiftCertificateHateoas();

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {
        if (DataValidation.isValidCertificate(giftCertificate)) {

            GiftCertificate savedGiftCertificate = giftCertificateService.createGiftCertificate(giftCertificate);

            CollectionModel<GiftCertificate> collectionModelSavedGiftCertificate = giftCertificateHateoas
                    .getHateoasGiftCertificateForCreating(savedGiftCertificate);

            return new ResponseEntity<>(Map.of("gift-certificate", collectionModelSavedGiftCertificate), HttpStatus.CREATED);
        }
        throw new ServerException("Something went wrong during the request, check your fields");
    }


    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {

        DataValidation.validatePageAndSizePagination(page, size);

        Page<GiftCertificate> allGiftCertificates = giftCertificateService.getAll(page, size);

        PagedModel<GiftCertificate> allGiftCertificatesModel = giftCertificateHateoas.getHateoasGiftCertificateForGettingAll(allGiftCertificates, representationModelAssembler);

        return ResponseEntity.ok(Map.of("gift-certificates", allGiftCertificatesModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            GiftCertificate currentGiftCertificate = giftCertificateService.getOneGiftCertificateById(id);

            CollectionModel<GiftCertificate> giftCertificateCollectionModel = giftCertificateHateoas
                    .getHateoasGiftCertificateForGettingOne(currentGiftCertificate);

            return ResponseEntity.ok(Map.of("gift-certificate", giftCertificateCollectionModel));
        }
        throw new ServerException("The Gift Certificate ID is not valid: id = " + id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@RequestBody GiftCertificate giftCertificate, @PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            Optional<Map<String, String>> updatesMap = DataValidation.isGiftCertificateValidForUpdating(giftCertificate);

            if (updatesMap.isPresent()) {
                GiftCertificate updatedGiftCertificate = giftCertificateService.updateGiftCertificate(id, giftCertificate.getTags(), updatesMap.get());

                CollectionModel<GiftCertificate> giftCertificateCollectionModel = giftCertificateHateoas
                        .getHateoasGiftCertificateForUpdate(updatedGiftCertificate);

                return ResponseEntity.ok(Map.of("gift-certificate", giftCertificateCollectionModel));
            }
            throw new ServerException("There are no fields to update");
        }
        throw new ServerException("The ID is not valid: id = " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            giftCertificateService.deleteGiftCertificate(id);
            return ResponseEntity.noContent().build();
        }
        throw new ServerException("The Gift Certificate ID is not valid: id = " + id);
    }

    @PatchMapping("update-price/{id}")
    public ResponseEntity<?> updatePrice(@PathVariable("id") long id, @RequestBody int price) {
        if (DataValidation.moreThenZero(id)) {
            if (DataValidation.moreThenZero(price)) {
                GiftCertificate updatedGiftCertificate = giftCertificateService.updatePrice(id, price);

                CollectionModel<GiftCertificate> giftCertificateCollectionModel = giftCertificateHateoas
                        .getHateoasGiftCertificateForUpdatingPrice(updatedGiftCertificate);

                return ResponseEntity.ok(Map.of("gift-certificate", giftCertificateCollectionModel));
            }
            throw new ServerException("The PRICE is not valid: price = " + price);
        }
        throw new ServerException("The ID is not valid: id = " + id);
    }

    @GetMapping("search/tag-name")
    public ResponseEntity<?> getByTagName(@RequestParam("name") String tagName,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "20") int size) {

        if (DataValidation.isStringValid(tagName)) {

            DataValidation.validatePageAndSizePagination(page, size);

            Page<GiftCertificate> giftCertificatesByTagName = giftCertificateService.getGiftCertificatesByTagName(tagName, page, size);

            PagedModel<GiftCertificate> giftCertificatePagedModel = giftCertificateHateoas
                    .getHateoasGiftCertificateForGettingByTagName(giftCertificatesByTagName, representationModelAssembler);

            return ResponseEntity.ok(Map.of("gift-certificates", giftCertificatePagedModel));
        }
        throw new ServerException("tag name is not valid");
    }

    @GetMapping("search/gift-certificate-name")
    public ResponseEntity<?> getGiftCertificatesAndTagsByNameOrByPartOfName(@RequestParam("name") String partOfName, @RequestParam("page") int page, @RequestParam("size") int size) {
        if (DataValidation.isStringValid(partOfName)) {

            DataValidation.validatePageAndSizePagination(page, size);

            Page<GiftCertificate> giftCertificatesByName = giftCertificateService.getGiftCertificatesByNameOrByPartOfName(partOfName, page, size);

            PagedModel<GiftCertificate> giftCertificatePagedModel = giftCertificateHateoas
                    .getHateoasGiftCertificateForGettingGiftCertificatesByNameOrByPartOfName(giftCertificatesByName, representationModelAssembler);


            return ResponseEntity.ok(Map.of("gift-certificates", giftCertificatePagedModel));
        }
        throw new ServerException("gift-certificate name is not valid");
    }

    @GetMapping("search/sort-date")
    public ResponseEntity<?> getGiftCertificatesSortedByDate(@RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        if (DataValidation.isStringValid(sortDirection)) {
            if (DataValidation.isSortingTypeContains(sortDirection)) {

                DataValidation.validatePageAndSizePagination(page, size);

                Page<GiftCertificate> giftCertificatesSortedByDate = giftCertificateService.getGiftCertificatesSortedByDate(sortDirection, page, size);

                PagedModel<GiftCertificate> giftCertificatePagedModel = giftCertificateHateoas
                        .getHateoasGiftCertificateForGettingGiftCertificatesSortedByDate(giftCertificatesSortedByDate, representationModelAssembler);


                return ResponseEntity.ok(Map.of("gift-certificates", giftCertificatePagedModel));
            }
            throw new ServerException("type should be only DESC or ASC");
        }
        throw new ServerException("Incorrect data");
    }

    @GetMapping("search/tag-name/cost")
    public ResponseEntity<?> getGiftCertificatesByTagsAndPrice(@RequestParam("tag1") String firstTagName,
                                                               @RequestParam("tag2") String secondTagName,
                                                               @RequestParam("price") int price,
                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "20") int size) {
        if (DataValidation.isStringValid(firstTagName) && DataValidation.isStringValid(secondTagName)) {
            if (DataValidation.moreThenZero(price)) {

                DataValidation.validatePageAndSizePagination(page, size);

                Page<GiftCertificate> giftCertificatesByTagsAndPrice = giftCertificateService
                        .getGiftCertificatesByTagsAndPrice(firstTagName, secondTagName, price, page, size);

                PagedModel<GiftCertificate> giftCertificatePagedModel = giftCertificateHateoas
                        .getHateoasGiftCertificateForGettingGiftCertificatesByTagsAndPrice(giftCertificatesByTagsAndPrice, representationModelAssembler);


                return ResponseEntity.ok(Map.of("gift-certificates", giftCertificatePagedModel));
            }
            throw new ServerException("price is not valid (price = " + price + ")");
        }
        throw new ServerException("tag name is not valid");
    }

    @GetMapping("search/sort-name-date")
    public ResponseEntity<?> getGiftCertificatesSortedByDateAndByName(@RequestParam(value = "firstSortDirection", defaultValue = "ASC") String firstSortDirection,
                                                                      @RequestParam(value = "secondSortDirection", defaultValue = "ASC") String secondSortDirection,
                                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", defaultValue = "20") int size) {
        if (DataValidation.isStringValid(firstSortDirection) && DataValidation.isStringValid(secondSortDirection)) {
            if (DataValidation.isSortingTypeContains(firstSortDirection) && DataValidation.isSortingTypeContains(secondSortDirection)) {

                DataValidation.validatePageAndSizePagination(page, size);

                Page<GiftCertificate> giftCertificatesSortedByDateAndByName = giftCertificateService
                        .getGiftCertificatesSortedByDateAndByName(firstSortDirection, secondSortDirection, page, size);

                PagedModel<GiftCertificate> giftCertificatePagedModel = giftCertificateHateoas
                        .getHateoasGiftCertificateForGettingGiftCertificatesSortedByDateAndByName(giftCertificatesSortedByDateAndByName, representationModelAssembler);

                return ResponseEntity.ok(Map.of("gift-certificates", giftCertificatePagedModel));
            }
            throw new ServerException("type should be only DESC or ASC");
        }
        throw new ServerException("incorrect data");
    }
}
