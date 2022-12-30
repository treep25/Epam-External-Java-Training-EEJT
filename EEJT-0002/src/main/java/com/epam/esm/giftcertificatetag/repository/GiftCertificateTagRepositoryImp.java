package com.epam.esm.giftcertificatetag.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.query.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class GiftCertificateTagRepositoryImp implements GiftCertificateTagRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateTagRepositoryImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GiftCertificate> getGiftCertificateTagsByTagName(String tagName) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.GET_GIFT_CERTIFICATE_TAGS_BY_TAG_NAME, new String[]{tagName}, (resultSet, i) ->
                new GiftCertificate().setId(resultSet.getLong("id")).
                        setName(resultSet.getString("name")).
                        setTags(getAllTagsByCertificateIdOrderByTagName(resultSet.getLong("id"), tagName)).
                        setDescription(resultSet.getString("description")).
                        setPrice(resultSet.getInt("price")).
                        setDuration(resultSet.getInt("duration")).
                        setCreateDate(resultSet.getDate("create_date")).
                        setLastUpdateDate(resultSet.getDate("last_update_date")));
    }

    public List<GiftCertificate> getGiftCertificatesAndTagsByNameOrByPartOfName(String partOfName) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.GET_GIFT_CERTIFICATE_AND_TAGS_BY_NAME_OR_BY_PART_OF_NAME, new String[]{partOfName + "%"},
                (resultSet, i) -> new GiftCertificate().setId(resultSet.getLong("id")).
                        setName(resultSet.getString("name")).
                        setTags(getAllTagsByCertificate(resultSet.getLong("id"))).
                        setDescription(resultSet.getString("description")).
                        setPrice(resultSet.getInt("price")).
                        setDuration(resultSet.getInt("duration")).
                        setCreateDate(resultSet.getDate("create_date")).
                        setLastUpdateDate(resultSet.getDate("last_update_date")));
    }

    public List<GiftCertificate> sortingAscDescByDate(String method) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.getTypeOfSortingForDate(method),
                (resultSet, i) -> new GiftCertificate().setId(resultSet.getLong("id")).
                        setName(resultSet.getString("name")).
                        setTags(getAllTagsByCertificate(resultSet.getLong("id"))).
                        setDescription(resultSet.getString("description")).
                        setPrice(resultSet.getInt("price")).
                        setDuration(resultSet.getInt("duration")).
                        setCreateDate(resultSet.getDate("create_date")).
                        setLastUpdateDate(resultSet.getDate("last_update_date")));
    }

    public List<GiftCertificate> sortingAscDescByDateAndByName(String method1, String method2) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.getTypeOfSortingForDateAndName(method1, method2),
                (resultSet, i) -> new GiftCertificate().setId(resultSet.getLong("id")).
                        setName(resultSet.getString("name")).
                        setTags(getAllTagsByCertificate(resultSet.getLong("id"))).
                        setDescription(resultSet.getString("description")).
                        setPrice(resultSet.getInt("price")).
                        setDuration(resultSet.getInt("duration")).
                        setCreateDate(resultSet.getDate("create_date")).
                        setLastUpdateDate(resultSet.getDate("last_update_date")));
    }

    private List<Tag> getAllTagsByCertificateIdOrderByTagName(long certificateId, String tagName) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.GET_ALL_TAGS_BY_GIFT_CERTIFICATE_ID_ORDER_BY_TAG_NAME, new Object[]{certificateId, tagName}, (resultSet, i) ->
                new Tag().setId(resultSet.getLong("id")).setName(resultSet.getString("name")));
    }

    private List<Tag> getAllTagsByCertificate(long certificateId) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.GET_ALL_TAGS_BY_GIFT_CERTIFICATE_ID, new Long[]{certificateId}, (resultSet, i) ->
                new Tag().setId(resultSet.getLong("id")).setName(resultSet.getString("name")));
    }
}
