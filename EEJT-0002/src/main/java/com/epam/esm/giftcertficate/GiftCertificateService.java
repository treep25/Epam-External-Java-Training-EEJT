package com.epam.esm.giftcertficate;

import com.epam.esm.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        return giftCertificateRepository.getAllGiftCertificates();
    }

    public void createCertificate(GiftCertificate giftCertificate) {
        giftCertificateRepository.createCertificate(giftCertificate);
        if (tagService.getTagByName(giftCertificate.getName())) {
            //create tag if false // else versa
        }

    }

    public GiftCertificate getCertificateById(long id) {
        GiftCertificate giftCertificate = giftCertificateRepository.getCertificateById(id);
        if (giftCertificate != null) {
            return giftCertificate;
        }
        throw new ItemNotFoundException("There are no gift certificate with id= " + id);
    }

    public void deleteCertificate(long id) {
        if (giftCertificateRepository.deleteCertificate(id) < 1) {
            throw new ItemNotFoundException("there is no gift certificate with id= " + id);
        }
    }

    public void updateGiftCertificate(long id, Map<String, ?> updatesMap) {
        if (giftCertificateRepository.updateGiftCertificate(id, updatesMap) < 1) {
            throw new ItemNotFoundException("there is no gift certificate to update with id= " + id);
        }
    }
}
