package com.epam.esm.giftcertficate.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.utils.query.SqlQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    boolean existsByName(String name);
    @Query(value = SqlQuery.GiftCertificate.GET_ALL_GIFT_CERTIFICATES_BY_TAG_NAME, nativeQuery = true)
    List<GiftCertificate> getAllGiftCertificatesByTagName(String tagName, int begin, int end);

    @Query(value = SqlQuery.GiftCertificate.GET_ALL_GIFT_CERTIFICATES_BY_NAME_OR_PART_OF_NAME, nativeQuery = true)
    List<GiftCertificate> getAllGiftCertificateByPartOfName(String partOfName, int begin, int end);

    @Query(value = SqlQuery.GiftCertificate.GET_ALL_GIFT_CERTIFICATES_BY_TAGS_AND_PRICE, nativeQuery = true)
    List<GiftCertificate> getGiftCertificatesByTagsAndPrice(String tagName1, String tagName2, int price, int begin, int end);
}
