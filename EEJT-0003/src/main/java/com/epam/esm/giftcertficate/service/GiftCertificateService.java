package com.epam.esm.giftcertficate.service;


import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        if (!giftCertificateRepository.isGiftCertificateExistByName(giftCertificate.getName())) {
            if (giftCertificate.getTags() != null) {

                tagService.verifyIsTagsExistWhenCreatingOrUpdatingGiftCertificate(giftCertificate.getTags());
            }
            return giftCertificateRepository.save(giftCertificate);
        }
        throw new ServerException("This gift certificate with (name = " + giftCertificate.getName() + ") has already existed");
    }

    public Page<GiftCertificate> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return Optional.of(giftCertificateRepository.findAll(pageRequest)).orElse(Page.empty(pageRequest));
    }

    public GiftCertificate getOneGiftCertificateById(long id) {
        return giftCertificateRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("There are no gift certificate with (id = " + id + ")"));
    }

    @Transactional
    public GiftCertificate updateGiftCertificate(long id, Set<Tag> tags, Map<String, String> updatesMap) {

        updatesMap.remove(TAGS);

        if (!giftCertificateRepository.isGiftCertificateExistByName(updatesMap.get(NAME))) {
            GiftCertificate updatedGiftCertificate = updatingGiftCertificateUsingMapOfUpdates(updatesMap,
                    getOneGiftCertificateById(id));

            if (tags != null) {

                if (!updatedGiftCertificate.getTags().isEmpty()) {
                    updatedGiftCertificate.setTags(null);
                }
                if (!tags.isEmpty()) {
                    updatedGiftCertificate.setTags(tags);
                    tagService.verifyIsTagsExistWhenCreatingOrUpdatingGiftCertificate(updatedGiftCertificate.getTags());
                }
            }
            return giftCertificateRepository.save(updatedGiftCertificate);
        } else {
            throw new ServerException("The certificate with (name= " + updatesMap.get(NAME) + ") has already existed");
        }
    }

    public GiftCertificate updatePrice(long id, int price) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id).orElseThrow(
                () -> new ServerException("There are no gift certificate with (id = " + id + ")"));
        giftCertificate.setPrice(price);
        return giftCertificateRepository.save(giftCertificate);
    }

    public boolean deleteGiftCertificate(long id) {
        if (giftCertificateRepository.existsById(id)) {
            giftCertificateRepository.deleteById(id);

            return true;
        }
        throw new ItemNotFoundException("There are no gift certificate with (id = " + id + ")");
    }

    public Page<GiftCertificate> getGiftCertificatesByTagName(String tagName, int page, int size) {
        List<GiftCertificate> allGiftCertificateByTagName = giftCertificateRepository
                .getAllGiftCertificateByTagName(tagName, getPaginationBegin(page, size), getPaginationEnd(page, size));

        return new PageImpl<>(allGiftCertificateByTagName);
    }

    public Page<GiftCertificate> getGiftCertificatesByTagsAndPrice(String firstTagName, String secondTagName, int price, int page, int size) {
        List<GiftCertificate> giftCertificatesByTagsAndPrice = giftCertificateRepository.
                getGiftCertificatesByTagsAndPrice(firstTagName, secondTagName, price,
                        getPaginationBegin(page, size),
                        getPaginationEnd(page, size));

        return new PageImpl<>(giftCertificatesByTagsAndPrice);
    }

    public Page<GiftCertificate> getGiftCertificatesByNameOrByPartOfName(String partOfName, int page, int size) {
        List<GiftCertificate> allGiftCertificateByPartOfName = giftCertificateRepository.
                getAllGiftCertificateByPartOfName(partOfName + PERCENT_FOR_SEARCHING_BY_PART,
                        getPaginationBegin(page, size),
                        getPaginationEnd(page, size));

        return new PageImpl<>(allGiftCertificateByPartOfName);
    }

    public Page<GiftCertificate> getGiftCertificatesSortedByDate(String sortDirection, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sortDirection.toUpperCase(Locale.ROOT)), CREATE_DATE));
        return giftCertificateRepository.findAll(pageRequest);
    }

    public Page<GiftCertificate> getGiftCertificatesSortedByDateAndByName(String firstSortDirection, String secondSortDirection, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(firstSortDirection.toUpperCase(Locale.ROOT)), NAME)
                .and(Sort.by(Sort.Direction.valueOf(secondSortDirection.toUpperCase(Locale.ROOT)), CREATE_DATE)));

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
