package com.epam.esm.giftcertficate.controller;


import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.service.GiftCertificateService;
import com.epam.esm.tag.controller.TagController;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {
        if (DataValidation.isValidCertificate(giftCertificate)) {

            GiftCertificate savedGiftCertificate = giftCertificateService.createGiftCertificate(giftCertificate);
            savedGiftCertificate
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .readById(savedGiftCertificate.getId()))
                            .withRel(() -> "get gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .read())
                            .withRel(() -> "get all gift-certificates"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .delete(savedGiftCertificate.getId()))
                            .withRel(() -> "delete gift-certificate"));

            generateRelForTags(giftCertificate.getTags());

            return ResponseEntity.ok(savedGiftCertificate);
        }
        throw new ServerException("Something went wrong during the request, check your fields");
    }


    @GetMapping
    public ResponseEntity<?> read() {
        List<GiftCertificate> allGiftCertificates = giftCertificateService.getAll();

        allGiftCertificates.forEach(giftCertificate -> {
            generateRelForTags(giftCertificate.getTags());
            giftCertificate
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .readById(giftCertificate.getId()))
                            .withRel(() -> "get gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .delete(giftCertificate.getId()))
                            .withRel(() -> "delete gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .create(new GiftCertificate()))
                            .withRel(() -> "create gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .updateCertificate(new GiftCertificate(), giftCertificate.getId()))
                            .withRel(() -> "update gift-certificates"));


        });
        return ResponseEntity.ok(Map.of("all gift-certificates", allGiftCertificates));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            GiftCertificate currentGiftCertificate = giftCertificateService.getOneGiftCertificateById(id);
            currentGiftCertificate
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .delete(currentGiftCertificate.getId()))
                            .withRel(() -> "delete gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .create(new GiftCertificate()))
                            .withRel(() -> "create gift-certificate"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .read())
                            .withRel(() -> "get all gift-certificates"))
                    .add(linkTo(methodOn(GiftCertificateController.class)
                            .updateCertificate(new GiftCertificate(), id))
                            .withRel(() -> "update gift-certificates"));

            generateRelForTags(currentGiftCertificate.getTags());

            return ResponseEntity.ok(Map.of("gift certificate", currentGiftCertificate));
        }
        throw new ServerException("The Gift Certificate ID is not valid: id = " + id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@RequestBody GiftCertificate giftCertificate, @PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            Optional<Map<String, String>> updatesMap = DataValidation.isGiftCertificateValidForUpdating(giftCertificate);

            if (updatesMap.isPresent()) {
                GiftCertificate updatedGiftCertificate = giftCertificateService.updateGiftCertificate(id, giftCertificate.getTags(), updatesMap.get());
                updatedGiftCertificate.
                        add(linkTo(methodOn(GiftCertificateController.class)
                                .delete(updatedGiftCertificate.getId()))
                                .withRel(() -> "delete gift-certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .create(new GiftCertificate()))
                                .withRel(() -> "create gift-certificate"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .read())
                                .withRel(() -> "get all gift-certificates"))
                        .add(linkTo(methodOn(GiftCertificateController.class)
                                .readById(id))
                                .withRel(() -> "get gift-certificates"));

                generateRelForTags(updatedGiftCertificate.getTags());

                return ResponseEntity.ok(updatedGiftCertificate);
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

    @GetMapping("search/tag-name/{name}")
    public ResponseEntity<?> getByTagName(@PathVariable("name") String name) {
        if (DataValidation.isStringValid(name)) {
            return ResponseEntity.ok(giftCertificateService.getGiftCertificatesByTagName(name));
        }
        throw new ServerException("tag name is not valid");
    }

    @GetMapping("search/gift-certificate-name/{partOfName}")
    public ResponseEntity<?> getGiftCertificatesAndTagsByNameOrByPartOfName(@PathVariable("partOfName") String partOfName) {
        if (DataValidation.isStringValid(partOfName)) {
            return ResponseEntity.ok(giftCertificateService.getGiftCertificatesByNameOrByPartOfName(partOfName));
        }
        throw new ServerException("Gift certificate name is not valid");
    }

    @GetMapping("search/sort-date/{sortDirection}")
    public ResponseEntity<?> getGiftCertificatesSortedByDate(@PathVariable("sortDirection") String sortDirection) {
        if (DataValidation.isStringValid(sortDirection)) {
            if (DataValidation.isSortingTypeContains(sortDirection)) {
                return ResponseEntity.ok(giftCertificateService.getGiftCertificatesSortedByDate(sortDirection));
            }
            throw new ServerException("type should be only DESC or ASC without register");
        }
        throw new ServerException("Incorrect data");
    }

    @GetMapping("search/sort-date-name/{firstSortDirection}/{secondSortDirection}")
    public ResponseEntity<?> getGiftCertificatesSortedByDateAndByName(@PathVariable("firstSortDirection") String firstSortDirection, @PathVariable("secondSortDirection") String secondSortDirection) {
        if (DataValidation.isStringValid(firstSortDirection) && DataValidation.isStringValid(secondSortDirection)) {

            if (DataValidation.isSortingTypeContains(firstSortDirection) && DataValidation.isSortingTypeContains(secondSortDirection)) {
                return ResponseEntity.ok(giftCertificateService.getGiftCertificatesSortedByDateAndByName(firstSortDirection, secondSortDirection));
            }
            throw new ServerException("type should be only DESC or ASC without register");
        }
        throw new ServerException("Incorrect data");
    }

    private void generateRelForTags(Set<Tag> tags) {
        if (tags != null) {
            tags.forEach(tag ->
                    tag.add(linkTo(methodOn(TagController.class)
                                    .delete(tag.getId()))
                                    .withRel(() -> "delete tag"))
                            .add(linkTo(methodOn(TagController.class)
                                    .create(new Tag()))
                                    .withRel(() -> "create tag"))
                            .add(linkTo(methodOn(TagController.class)
                                    .readById(tag.getId()))
                                    .withRel(() -> "get tag"))
                            .add(linkTo(methodOn(TagController.class)
                                    .read())
                                    .withRel(() -> "get tags")));
        }
    }
}
