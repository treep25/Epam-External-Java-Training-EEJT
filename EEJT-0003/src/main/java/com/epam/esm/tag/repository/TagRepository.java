package com.epam.esm.tag.repository;


import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.query.SqlQuery;
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

    @Query(value = SqlQuery.Tag.GET_THE_MOST_WIDELY_USED_TAG_WITH_THE_HIGHEST_COST_OF_ALL_ORDERS, nativeQuery = true)
    Tag getTheMostWidelyUsedTag();
}
