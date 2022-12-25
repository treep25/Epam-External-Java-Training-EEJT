package com.epam.esm.giftcertficate;

import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagService;
import com.epam.esm.utils.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository, TagService tagService) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
    }


    public List<GiftCertificate> getAllCertificates() {
        List<GiftCertificate> giftCertificateList = giftCertificateRepository.getAllGiftCertificates();
        if (!giftCertificateList.isEmpty()) {
            return giftCertificateList;
        }
        throw new ItemNotFoundException("There are no gift certificates");
    }

    public void createCertificate(GiftCertificate giftCertificate) {
        if (!giftCertificateRepository.isGiftCertificateExist(giftCertificate)) {

            giftCertificateRepository.createCertificate(giftCertificate);

            if (giftCertificate.getTags() != null) {
                tagService.isTagsExistOrElseCreate(giftCertificate.getTags());

                List<Long> listTagsId = tagService.getTagsIdByNames(giftCertificate.getTags());
                giftCertificateRepository.createGiftCertificateTagList(listTagsId, getGiftCertificateId(giftCertificate));
            }
        } else {
            throw new IllegalArgumentException("Such certificate has already existed");
        }
    }

    public List<GiftCertificate> getCertificateById(long id) {
        List<GiftCertificate> giftCertificate = giftCertificateRepository.getCertificateById(id);
        if (!giftCertificate.isEmpty()) {
            return giftCertificate;
        }
        throw new ItemNotFoundException("There are no gift certificate with id= " + id);
    }

    public void deleteGiftCertificate(long id) {
        if (giftCertificateRepository.deleteCertificate(id) != 1) {
            throw new ItemNotFoundException("there is no gift certificate with id= " + id);
        }
    }

    public List<GiftCertificate> updateGiftCertificate(long id, List<Tag> tags, Optional<Map<String, String>> updatesMap) {
        if (giftCertificateRepository.updateGiftCertificate(id, updatesMap) == 1) {
            if (tags != null) {
                List<Tag> tagThatExistInThisGiftCertificate = tagService.getAllTagsByCertificateId(id);
                if (!tagThatExistInThisGiftCertificate.isEmpty()) {
                    for (Tag tag : tagThatExistInThisGiftCertificate) {
                        tags.removeIf(x -> x.getName().equals(tag.getName()));
                    }
                }
                tagService.isTagsExistOrElseCreate(tags);
                giftCertificateRepository.createGiftCertificateTagList(tagService.getTagsIdByNames(tags), id);
            }
            return giftCertificateRepository.getCertificateById(id);
        } else {
            throw new ItemNotFoundException("there is no gift certificate to update with id= " + id);
        }
    }

    public long getGiftCertificateId(GiftCertificate giftCertificate) {
        return giftCertificateRepository.getIdByGiftCertificate(giftCertificate);
    }

}
