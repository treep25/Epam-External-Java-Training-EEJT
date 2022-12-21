package com.epam.esm.giftcertificatetag;

import com.epam.esm.giftcertficate.GiftCertificate;
import com.epam.esm.tag.Tag;
import com.epam.esm.utils.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GiftCertificateTagService {

    private final GiftCertificateTagRepositoryImp giftCertificateTagRepositoryImp;

    @Autowired
    public GiftCertificateTagService(GiftCertificateTagRepositoryImp giftCertificateTagRepositoryImp) {
        this.giftCertificateTagRepositoryImp = giftCertificateTagRepositoryImp;
    }

    public List<?> getGiftCertificatesAndTags() {
        List<?> giftCertificatesAndTags = giftCertificateTagRepositoryImp.getGiftCertificatesAndTags();
        if (!giftCertificatesAndTags.isEmpty()) {
            return giftCertificatesAndTags;
        }
        throw new ItemNotFoundException("There are no tags");
    }

    public List<?> getGiftCertificatesAndTagsByNameOrByPartOfName(String partOfName) {
        List<?> giftCertificatesAndTagsByName = giftCertificateTagRepositoryImp.getGiftCertificatesAndTagsByNameOrByPartOfName(partOfName);
        if (!giftCertificatesAndTagsByName.isEmpty()) {
            return giftCertificatesAndTagsByName;
        }
        throw new ItemNotFoundException("There are no tags with name =" + partOfName);
    }

    public List<?> sortingAscDescByDate(String method) {
        List<?> sortingTagsAndCertificates = giftCertificateTagRepositoryImp.sortingAscDescByDate(method);
        if (!sortingTagsAndCertificates.isEmpty()) {
            return sortingTagsAndCertificates;
        }
        throw new ItemNotFoundException("There are no tags and certificates");
    }

    public List<?> sortingAscDescByDateAndByName(String method) {
        List<?> sortingTagsAndCertificates = giftCertificateTagRepositoryImp.sortingAscDescByDateAndByName(method);
        if (!sortingTagsAndCertificates.isEmpty()) {
            return sortingTagsAndCertificates;
        }
        throw new ItemNotFoundException("There are no tags and certificates");
    }
}
