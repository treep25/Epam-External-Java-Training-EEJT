package com.epam.esm.commercetools.certificate.controller;

import com.epam.esm.commercetools.PagePaginationBuilder;
import com.epam.esm.commercetools.certificate.model.CommerceGiftCertificate;
import com.epam.esm.commercetools.certificate.service.GiftCertificateCommerceService;
import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/commerce/certs")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GiftCertificateCommerceController {

    private final GiftCertificateCommerceService giftCertificateCommerceService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {
        log.debug("Validation of request model of gift-certificate " + giftCertificate.toString());
        if (DataValidation.isValidCertificate(giftCertificate)) {

            return new ResponseEntity<>(giftCertificateCommerceService
                    .createGiftCertificate(giftCertificate), HttpStatus.CREATED);
        }
        log.error("Gift-certificate received from request is not correct (something went wrong during the request, check your fields)");
        throw new ServerException("Something went wrong during the request, check your fields");
    }


    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model fields " + page + " " + size);
        DataValidation.validatePageAndSizePagination(page, size);
        log.debug("Receive all gift-certificates");

        return ResponseEntity.ok(giftCertificateCommerceService
                .getAll(new PagePaginationBuilder(PageRequest.of(page, size))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable("id") String id) {
        log.debug("Validation of request model field ID " + id);
        if (DataValidation.isCommerceIdValid(id)) {

            return ResponseEntity.ok(giftCertificateCommerceService.getCertificateById(id));
        }
        log.error("The Gift Certificate ID is not valid: id = " + id);
        throw new ServerException("The Gift Certificate ID is not valid: id = " + id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@RequestBody GiftCertificate giftCertificate,
                                               @PathVariable("id") String id) {
        log.debug("Validation of request model field ID " + id);
        if (DataValidation.isCommerceIdValid(id)) {
            log.debug("Validation of request model fields" + giftCertificate.toString());
            Optional<Map<String, String>> updatesMap = DataValidation.isGiftCertificateValidForUpdating(giftCertificate);

            if (updatesMap.isPresent()) {
                log.debug("Receive updated gift-certificate");

                return ResponseEntity.ok(giftCertificateCommerceService
                        .updateCertificate(id, giftCertificate));
            }
            log.error("There are no fields to update gift-certificate");
            throw new ServerException("There are no fields to update");
        }
        log.error("The gift-certificate ID is not valid: id = " + id);
        throw new ServerException("The ID is not valid: id = " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        log.debug("Validation of request model fields " + id);
        if (DataValidation.isCommerceIdValid(id)) {
            giftCertificateCommerceService.deleteCertificate(id);
            log.debug("deleted - return noContent");

            return ResponseEntity.noContent().build();
        }
        log.error("The Gift Certificate ID is not valid: id = " + id);
        throw new ServerException("The Gift Certificate ID is not valid: id = " + id);
    }

    @PatchMapping("update-price/{id}")
    public ResponseEntity<?> updatePrice(@PathVariable("id") String id, @RequestBody int price) {
        log.debug("Validation of request model field ID " + id);
        if (DataValidation.isStringValid(id)) {

            log.debug("Validation of request model field price " + price);
            if (DataValidation.moreThenZero(price)) {
                log.debug("Receive updated gift-certificate");

                return ResponseEntity.ok(giftCertificateCommerceService
                        .updatePrice(id, price));
            }
            log.error("The PRICE is not valid: price = " + price);
            throw new ServerException("The PRICE is not valid: price = " + price);

        }
        log.error("The ID is not valid: id = " + id);
        throw new ServerException("The ID is not valid: id = " + id);
    }

    @GetMapping("search")
    public ResponseEntity<?> searchByGiftCertificateNameOrTagName(@RequestParam(value = "gift", required = false) String giftCertificateName,
                                                                  @RequestParam(value = "tag", required = false) String tagName,
                                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model of gift-certificate name " + giftCertificateName);
        log.debug("Validation of request model of gift-certificate tag " + tagName);

        log.debug("Validation of request model fields " + page + " " + size);
        DataValidation.validatePageAndSizePagination(page, size);

        if (DataValidation.isObjectNull(giftCertificateName) || DataValidation.isObjectNull(tagName)) {
            if (DataValidation.isStringValid(giftCertificateName)) {
                Page<CommerceGiftCertificate> byName = giftCertificateCommerceService.findByName(giftCertificateName,
                        new PagePaginationBuilder(PageRequest.of(page, size)));

                log.debug("Receive gift-certificates by name{}", giftCertificateName);

                return ResponseEntity.ok(byName);
            } else if (DataValidation.isStringValid(tagName)) {
                Page<CommerceGiftCertificate> byTagName = giftCertificateCommerceService.findByTagName(tagName,
                        new PagePaginationBuilder(PageRequest.of(page, size)));

                log.debug("Receive gift-certificates by tagName{}", tagName);

                return ResponseEntity.ok(byTagName);
            }
        }
        log.error("Not implemented both types of searching");
        throw new ItemNotFoundException("Not implemented both types of searching");
    }

}
