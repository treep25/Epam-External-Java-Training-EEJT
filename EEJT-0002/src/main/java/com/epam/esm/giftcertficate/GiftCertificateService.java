package com.epam.esm.giftcertficate;

import com.epam.esm.tag.Tag;
import com.epam.esm.tag.TagService;
import com.epam.esm.utils.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            Tag tag = new Tag().setName(giftCertificate.getName());

            if (!tagService.isTagByNameExists(tag.getName())) {
                tagService.createTag(tag);
            }
            giftCertificateRepository.createGiftCertificateTag(tagService.getTagIdByName(tag), getGiftCertificateId(giftCertificate));
        } else {
            throw new IllegalArgumentException("Such certificate has already existed");
        }
    }

    public GiftCertificate getCertificateById(long id) {
        List<GiftCertificate> giftCertificate = giftCertificateRepository.getCertificateById(id);
        if (!giftCertificate.isEmpty()) {
            return giftCertificate.get(0);
        }
        throw new ItemNotFoundException("There are no gift certificate with id= " + id);
    }

    public void deleteGiftCertificate(long id) {
        if (giftCertificateRepository.deleteCertificate(id) != 1) {
            throw new ItemNotFoundException("there is no gift certificate with id= " + id);
        }
    }

    public void updateGiftCertificate(long id, Map<String, ?> updatesMap) {
        if (giftCertificateRepository.updateGiftCertificate(id, updatesMap) == 1) {
            if (updatesMap.containsKey("name")) {

                if (!tagService.isTagByNameExists(updatesMap.get("name").toString())) {
                    tagService.createTag(new Tag().setName(updatesMap.get("name").toString()));
                }

                long tagId = tagService.getTagIdByName(new Tag().setName(updatesMap.get("name").toString()));
                giftCertificateRepository.deleteGiftCertificateTag(id);
                giftCertificateRepository.createGiftCertificateTag(tagId, id);
            }
        } else {
            throw new ItemNotFoundException("there is no gift certificate to update with id= " + id);
        }
    }

    public long getGiftCertificateId(GiftCertificate giftCertificate) {
        return giftCertificateRepository.getIdByGiftCertificate(giftCertificate);
    }
}
