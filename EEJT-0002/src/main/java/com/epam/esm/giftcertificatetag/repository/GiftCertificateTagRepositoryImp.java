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
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";

    @Autowired
    public GiftCertificateTagRepositoryImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GiftCertificate> getGiftCertificateTagsByTagName(String tagName) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.GET_GIFT_CERTIFICATE_TAGS_BY_TAG_NAME, new String[]{tagName},
                (resultSet, i) ->
                        new GiftCertificate().setId(resultSet.getLong(ID)).
                                setName(resultSet.getString(NAME)).
                                setTags(getAllTagsByCertificateIdOrderByTagName(resultSet.getLong(ID), tagName)).
                                setDescription(resultSet.getString(DESCRIPTION)).
                                setPrice(resultSet.getInt(PRICE)).
                                setDuration(resultSet.getInt(DURATION)).
                                setCreateDate(resultSet.getDate(CREATE_DATE)).
                                setLastUpdateDate(resultSet.getDate(LAST_UPDATE_DATE)));
    }

    public List<GiftCertificate> getGiftCertificatesAndTagsByNameOrByPartOfName(String partOfName) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.GET_GIFT_CERTIFICATE_AND_TAGS_BY_NAME_OR_BY_PART_OF_NAME,
                new String[]{partOfName + "%"},
                (resultSet, i) -> new GiftCertificate().setId(resultSet.getLong(ID)).
                        setName(resultSet.getString(NAME)).
                        setTags(getAllTagsByCertificate(resultSet.getLong(ID))).
                        setDescription(resultSet.getString(DESCRIPTION)).
                        setPrice(resultSet.getInt(PRICE)).
                        setDuration(resultSet.getInt(DURATION)).
                        setCreateDate(resultSet.getDate(CREATE_DATE)).
                        setLastUpdateDate(resultSet.getDate(LAST_UPDATE_DATE)));
    }

    public List<GiftCertificate> getGiftCertificatesSortedByDate(String sortDirection) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.getTypeOfSortingForDate(sortDirection),
                (resultSet, i) -> new GiftCertificate().setId(resultSet.getLong(ID)).
                        setName(resultSet.getString(NAME)).
                        setTags(getAllTagsByCertificate(resultSet.getLong(ID))).
                        setDescription(resultSet.getString(DESCRIPTION)).
                        setPrice(resultSet.getInt(PRICE)).
                        setDuration(resultSet.getInt(DURATION)).
                        setCreateDate(resultSet.getDate(CREATE_DATE)).
                        setLastUpdateDate(resultSet.getDate(LAST_UPDATE_DATE)));
    }

    public List<GiftCertificate> getGiftCertificatesSortedByDateAndByName(String firstSortDirection, String secondSortDirection) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.getTypeOfSortingForDateAndName(firstSortDirection, secondSortDirection),
                (resultSet, i) -> new GiftCertificate().setId(resultSet.getLong(ID)).
                        setName(resultSet.getString(NAME)).
                        setTags(getAllTagsByCertificate(resultSet.getLong(ID))).
                        setDescription(resultSet.getString(DESCRIPTION)).
                        setPrice(resultSet.getInt(PRICE)).
                        setDuration(resultSet.getInt(DURATION)).
                        setCreateDate(resultSet.getDate(CREATE_DATE)).
                        setLastUpdateDate(resultSet.getDate(LAST_UPDATE_DATE)));
    }

    private List<Tag> getAllTagsByCertificateIdOrderByTagName(long certificateId, String tagName) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.GET_ALL_TAGS_BY_GIFT_CERTIFICATE_ID_ORDER_BY_TAG_NAME,
                new Object[]{certificateId, tagName},
                (resultSet, i) ->
                        new Tag().setId(resultSet.getLong(ID)).setName(resultSet.getString(NAME)));
    }

    private List<Tag> getAllTagsByCertificate(long certificateId) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.GET_ALL_TAGS_BY_GIFT_CERTIFICATE_ID,
                new Long[]{certificateId},
                (resultSet, i) ->
                        new Tag().setId(resultSet.getLong(ID)).setName(resultSet.getString(NAME)));
    }
}
