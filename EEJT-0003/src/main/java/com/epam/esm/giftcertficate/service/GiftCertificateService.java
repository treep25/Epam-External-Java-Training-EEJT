package com.epam.esm.giftcertficate.service;


import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

    public List<GiftCertificate> getAll() {
        return giftCertificateRepository.findAll();
    }

    public GiftCertificate getOneGiftCertificateById(long id) {
        return giftCertificateRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("There are no gift certificate with (id = " + id + ")"));
    }

    @Transactional
    public GiftCertificate updateGiftCertificate(long id, Set<Tag> tags, Map<String, String> updatesMap) {

        updatesMap.remove(TAGS);

        if ((!giftCertificateRepository.isGiftCertificateExistByName(updatesMap.get(NAME)))) {
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

    public boolean deleteGiftCertificate(long id) {
        if (giftCertificateRepository.existsById(id)) {
            giftCertificateRepository.deleteById(id);

            return true;
        }
        throw new ItemNotFoundException("There are no gift certificate with (id = " + id + ")");
    }

    public List<GiftCertificate> getGiftCertificatesByTagName(String tagName) {
        return giftCertificateRepository.getAllGiftCertificateByTagName(tagName);
    }

    public List<GiftCertificate> getGiftCertificatesByNameOrByPartOfName(String partOfName) {
        return giftCertificateRepository.getAllGiftCertificateByPartOfName(partOfName + PERCENT_FOR_SEARCHING_BY_PART);
    }

    public List<GiftCertificate> getGiftCertificatesSortedByDate(String sortDirection) {
        return giftCertificateRepository.findAll(Sort.by(Sort.Direction.valueOf(sortDirection.toUpperCase(Locale.ROOT)), CREATE_DATE));
    }

    public List<GiftCertificate> getGiftCertificatesSortedByDateAndByName(String firstSortDirection, String secondSortDirection) {
        return giftCertificateRepository.findAll(Sort.by(Sort.Direction.valueOf(firstSortDirection.toUpperCase(Locale.ROOT)), NAME)
                .and(Sort.by(Sort.Direction.valueOf(secondSortDirection.toUpperCase(Locale.ROOT)), CREATE_DATE)));
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
}
