package com.epam.esm.giftcertficate.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.giftcertficate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@EnableTransactionManagement
public class GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;
    private static final String TAGS = "tags";

    @Autowired
    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository, TagService tagService) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
    }


    public List<GiftCertificate> getAllCertificates() {

        return giftCertificateRepository.getAllGiftCertificates();
    }

    @Transactional
    public boolean createCertificate(GiftCertificate giftCertificate) {
        if (!giftCertificateRepository.isGiftCertificateExist(giftCertificate)) {

            boolean result = giftCertificateRepository.createCertificate(giftCertificate);

            if (giftCertificate.getTags() != null) {
                tagService.isTagsExistOrElseCreate(giftCertificate.getTags());

                List<Long> listTagsId = tagService.getTagsIdByTags(giftCertificate.getTags());
                giftCertificateRepository.createGiftCertificateTagRelationship(listTagsId, getGiftCertificateId(giftCertificate));
            }

            return result;

        } else {
            throw new ServerException("Such certificate has already existed");
        }

    }

    public List<GiftCertificate> getCertificateById(long id) {
        List<GiftCertificate> giftCertificate = giftCertificateRepository.getCertificateById(id);
        if (!giftCertificate.isEmpty()) {
            return giftCertificate;
        }
        throw new ItemNotFoundException("There are no gift certificate with id= " + id);
    }

    public boolean deleteGiftCertificate(long id) {
        if (giftCertificateRepository.deleteCertificate(id)) {
            return true;
        }
        throw new ItemNotFoundException("There is no gift certificate with id= " + id);
    }

    @Transactional
    public List<GiftCertificate> updateGiftCertificate(long id, List<Tag> tags, Map<String, String> updatesMap) {

        updatesMap.remove(TAGS);

        if (giftCertificateRepository.updateGiftCertificate(id, updatesMap)) {
            if (tags != null) {

                List<Tag> tagThatExistInThisGiftCertificate = tagService.getAllTagsByCertificateId(id);

                if (!tagThatExistInThisGiftCertificate.isEmpty()) {
                    giftCertificateRepository.deleteGiftCertificateTagRelationship(tagService.getTagsIdByTags(tagThatExistInThisGiftCertificate), id);
                }
                if (!tags.isEmpty()) {
                    tagService.isTagsExistOrElseCreate(tags);
                    giftCertificateRepository.createGiftCertificateTagRelationship(tagService.getTagsIdByTags(tags), id);
                }
            }
            return giftCertificateRepository.getCertificateById(id);
        } else {
            throw new ItemNotFoundException("There is no gift certificate to update with id= " + id);
        }
    }

    public long getGiftCertificateId(GiftCertificate giftCertificate) {
        return giftCertificateRepository.getIdByGiftCertificate(giftCertificate);
    }

}
