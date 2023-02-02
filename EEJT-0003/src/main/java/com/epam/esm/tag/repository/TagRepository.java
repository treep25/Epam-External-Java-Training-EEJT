package com.epam.esm.tag.repository;


import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.query.SqlQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String tagName);

    @Query(SqlQuery.Tag.GET_ID_BY_TAG_NAME)
    long getIdByTagName(@Param("name") String tagName);

    @Query(value = SqlQuery.Tag.GET_THE_MOST_WIDELY_USED_TAG_WITH_THE_HIGHEST_COST_OF_ALL_ORDERS, nativeQuery = true)
    Tag getTheMostWidelyUsedTag();
}
