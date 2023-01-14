package com.epam.esm.giftcertficate.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    @Query("select case when count(gc)> 0 then true else false end from GiftCertificate gc where gc.name = :name")
    boolean isGiftCertificateExistByName(@Param("name") String name);

    @Query(value = "SELECT * FROM gift_certificate_tags gct  JOIN tag t JOIN gift_certificate gc  WHERE gc.id = gct.gift_certificate_id AND t.id = gct.tags_id AND t.name = ?1", nativeQuery = true)
    List<GiftCertificate> getAllGiftCertificateByTagName(String tagName);

    @Query(value = "SELECT * FROM  gift_certificate gc WHERE gc.name LIKE ?1", nativeQuery = true)
    List<GiftCertificate> getAllGiftCertificateByPartOfName(String partOfName);


}
