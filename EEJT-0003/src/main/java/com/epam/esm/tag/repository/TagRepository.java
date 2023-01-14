package com.epam.esm.tag.repository;


import com.epam.esm.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("select case when count(t)> 0 then true else false end from Tag t where t.name = :name")
    boolean isTagExistsByName(@Param("name") String tagName);

    @Query("SELECT t.id FROM Tag t WHERE t.name = :name")
    long getIdByTagName(@Param("name") String tagName);

    @Query(value = "SELECT t.id,t.name FROM gift_certificate_tag gct  JOIN tag t  WHERE gct.gift_certificate_id = ?1 AND t.id = gct.tag_id"
            , nativeQuery = true)
    List<Tag> getAllTagsByGiftCertificateId(long id);
}
