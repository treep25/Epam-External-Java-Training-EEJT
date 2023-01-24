package com.epam.esm.giftcertficate.service;


import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableTransactionManagement
public class GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;

    private static final String TAGS = "tags";
    private static final String NAME = "name";
    private static final String CREATE_DATE = "createDate";
    private static final String PERCENT_FOR_SEARCHING_BY_PART = "%";

    @Transactional
    public GiftCertificate createGiftCertificate(GiftCertificate giftCertificate) {
        log.info("Service receives gift-certificate for creating");
        log.info("Transaction has been started");

        log.debug("Checking that gift-certificate has not already exist");
        if (!giftCertificateRepository.isGiftCertificateExistByName(giftCertificate.getName())) {

            log.debug("Checking tags of gift-certificate");
            if (giftCertificate.getTags() != null) {

                log.debug("Verify is tag exist");
                tagService.verifyIsTagsExistWhenCreatingOrUpdatingGiftCertificate(giftCertificate.getTags());
            }
            log.info("Transaction has been ended");
            log.debug("Service returns representation of saved gift-certificate");
            return giftCertificateRepository.save(giftCertificate);
        }
        log.info("Transaction has been ended");
        log.error("Rollback");
        log.error("This gift certificate with (name = " + giftCertificate.getName() + ") has already existed");
        throw new ServerException("this gift certificate with (name = " + giftCertificate.getName() + ") has already existed");
    }

    public Page<GiftCertificate> getAll(int page, int size) {
        log.info("Service receives params for getting all");

        PageRequest pageRequest = PageRequest.of(page, size);

        log.debug("Service returns all gift-certificates");
        return giftCertificateRepository.findAll(pageRequest);
    }

    public GiftCertificate getOneGiftCertificateById(long id) {
        log.info("Service receives params for getting gift-certificate by id");
        log.debug("Service returns one gift-certificate");

        return giftCertificateRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("there are no gift certificate with (id = " + id + ")"));
    }

    @Transactional
    @Modifying
    public GiftCertificate updateGiftCertificate(long id, Set<Tag> tags, Map<String, String> updatesMap) {
        log.info("Service receives gift-certificate for updating");
        log.info("Transaction has been started");

        updatesMap.remove(TAGS);

        log.debug("Checking that gift-certificate has not already exist to update on new name");
        if (!giftCertificateRepository.isGiftCertificateExistByName(updatesMap.get(NAME))) {

            log.debug("Update");
            GiftCertificate updatedGiftCertificate = updatingGiftCertificateUsingMapOfUpdates(updatesMap,
                    getOneGiftCertificateById(id));

            log.debug("Checking tags of gift-certificate");
            if (tags != null) {

                log.debug("Updating tags of gift-certificate");
                if (!updatedGiftCertificate.getTags().isEmpty()) {
                    updatedGiftCertificate.setTags(null);
                }
                if (!tags.isEmpty()) {
                    updatedGiftCertificate.setTags(tags);
                    tagService.verifyIsTagsExistWhenCreatingOrUpdatingGiftCertificate(updatedGiftCertificate.getTags());
                }
            }
            log.info("Transaction has been ended");
            log.debug("Service returns updated gift-certificate");
            return giftCertificateRepository.save(updatedGiftCertificate);
        } else {
            log.info("Transaction has been ended");
            log.error("Rollback");
            log.error("The certificate with (name= " + updatesMap.get(NAME) + ") has already existed");
            throw new ServerException("the certificate with (name= " + updatesMap.get(NAME) + ") has already existed");
        }
    }

    @Modifying
    public GiftCertificate updatePrice(long id, int price) {
        log.info("Service receives gift-certificate`s params for updating price");

        GiftCertificate giftCertificate = giftCertificateRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("there are po gift certificate with (id = " + id + ")"));
        giftCertificate.setPrice(price);

        log.debug("Service returns updated gift-certificate");
        return giftCertificateRepository.save(giftCertificate);
    }

    public boolean deleteGiftCertificate(long id) {
        log.info("Service receives params for deleting");

        log.debug("Checking that gift-certificate exists");
        if (giftCertificateRepository.existsById(id)) {
            giftCertificateRepository.deleteById(id);

            log.debug("Service returns result od deleting");
            return true;
        }
        log.error("There are no gift certificate with (id = " + id + ")");
        throw new ItemNotFoundException("there are no gift certificate with (id = " + id + ")");
    }

    public Page<GiftCertificate> getGiftCertificatesByTagName(String tagName, int page, int size) {
        log.info("Service receives params for getting");

        List<GiftCertificate> allGiftCertificateByTagName = giftCertificateRepository
                .getAllGiftCertificatesByTagName(tagName, getPaginationBegin(page, size), getPaginationEnd(page, size));

        log.debug("Service returns gift-certificates");
        return new PageImpl<>(allGiftCertificateByTagName);
    }

    public Page<GiftCertificate> getGiftCertificatesByTagsAndPrice(String firstTagName, String secondTagName, int price, int page, int size) {
        log.info("Service receives params for getting");

        List<GiftCertificate> giftCertificatesByTagsAndPrice = giftCertificateRepository.
                getGiftCertificatesByTagsAndPrice(firstTagName, secondTagName, price,
                        getPaginationBegin(page, size),
                        getPaginationEnd(page, size));

        log.debug("Service returns gift-certificates");
        return new PageImpl<>(giftCertificatesByTagsAndPrice);
    }

    public Page<GiftCertificate> getGiftCertificatesByNameOrByPartOfName(String partOfName, int page, int size) {
        log.info("Service receives params for getting");

        List<GiftCertificate> allGiftCertificateByPartOfName = giftCertificateRepository.
                getAllGiftCertificateByPartOfName(partOfName + PERCENT_FOR_SEARCHING_BY_PART,
                        getPaginationBegin(page, size),
                        getPaginationEnd(page, size));

        log.debug("Service returns gift-certificates");
        return new PageImpl<>(allGiftCertificateByPartOfName);
    }

    public Page<GiftCertificate> getGiftCertificatesSortedByDate(String sortDirection, int page, int size) {
        log.info("Service receives params for getting");

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sortDirection.toUpperCase(Locale.ROOT)), CREATE_DATE));

        log.debug("Service returns gift-certificates");
        return giftCertificateRepository.findAll(pageRequest);
    }

    public Page<GiftCertificate> getGiftCertificatesSortedByDateAndByName(String firstSortDirection, String secondSortDirection, int page, int size) {
        log.info("Service receives params for getting");

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(firstSortDirection.toUpperCase(Locale.ROOT)), NAME)
                .and(Sort.by(Sort.Direction.valueOf(secondSortDirection.toUpperCase(Locale.ROOT)), CREATE_DATE)));

        log.debug("Service returns gift-certificates");
        return giftCertificateRepository.findAll(pageRequest);
    }

    private GiftCertificate updatingGiftCertificateUsingMapOfUpdates(Map<String, String> updatesMap, GiftCertificate gifCertificateForUpdating) {
        updatesMap.forEach((key, value) -> {
            if (key.equals("name")) {
                gifCertificateForUpdating.setName(value);
            } else if (key.equals("description")) {
                gifCertificateForUpdating.setDescription(value);
            } else if (key.equals("price")) {
                gifCertificateForUpdating.setPrice(Integer.parseInt(value));
            } else if (key.equals("duration")) {
                gifCertificateForUpdating.setDurationDays(Integer.parseInt(value));
            }
        });
        return gifCertificateForUpdating;
    }

    private int getPaginationBegin(int page, int size) {
        return (size * page);
    }

    private int getPaginationEnd(int page, int size) {
        return (size * page) + size;
    }
}
