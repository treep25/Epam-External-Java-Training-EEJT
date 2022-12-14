package com.epam.esm.giftcertificatetag.service;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.giftcertificatetag.repository.GiftCertificateTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftCertificateTagService {

    private final GiftCertificateTagRepository giftCertificateTagRepository;

    @Autowired
    public GiftCertificateTagService(GiftCertificateTagRepository giftCertificateTagRepository) {
        this.giftCertificateTagRepository = giftCertificateTagRepository;
    }

    public List<GiftCertificate> getGiftCertificatesByTagName(String tagName) {
        List<GiftCertificate> listGiftCertificateTags = giftCertificateTagRepository.getGiftCertificateTagsByTagName(tagName);

        if (!listGiftCertificateTags.isEmpty()) {
            return listGiftCertificateTags;
        }
        throw new ItemNotFoundException("There are no certificates with tag name = " + tagName);
    }

    public List<GiftCertificate> getGiftCertificatesAndTagsByNameOrByPartOfName(String partOfName) {
        List<GiftCertificate> giftCertificatesAndTagsByName = giftCertificateTagRepository.getGiftCertificatesAndTagsByNameOrByPartOfName(partOfName);

        if (!giftCertificatesAndTagsByName.isEmpty()) {
            return giftCertificatesAndTagsByName;
        }
        throw new ItemNotFoundException("There are no gift certificates with name or starting with  =" + partOfName);
    }

    public List<GiftCertificate> getGiftCertificatesSortedByDate(String sortDirection) {
        List<GiftCertificate> sortingTagsAndCertificates = giftCertificateTagRepository.getGiftCertificatesSortedByDate(sortDirection);

        if (!sortingTagsAndCertificates.isEmpty()) {
            return sortingTagsAndCertificates;
        }
        throw new ItemNotFoundException("There are no tags and certificates");
    }

    public List<GiftCertificate> getGiftCertificatesSortedByDateAndByName(String firstSortDirection, String secondSortDirection) {
        List<GiftCertificate> sortingTagsAndCertificates = giftCertificateTagRepository.getGiftCertificatesSortedByDateAndByName(firstSortDirection, secondSortDirection);

        if (!sortingTagsAndCertificates.isEmpty()) {
            return sortingTagsAndCertificates;
        }
        throw new ItemNotFoundException("There are no tags and certificates");
    }
}
