package com.epam.esm.giftcertficate.repository;

import com.epam.esm.giftcertficate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.query.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
public class GiftCertificateRepositoryImp implements GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";
    private static final String PART_OF_QUERY_FOR_UPDATING = "UPDATE gift_certificate SET ";
    private Instant currentDateTime;
    private static final int DB_OPERATION_SUCCESSFUL_RESULT = 1;

    @Autowired
    public GiftCertificateRepositoryImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private List<Tag> getAllTagsByCertificateId(long certificateId) {
        return jdbcTemplate.query(SqlQuery.GiftCertificate.GET_ALL_TAGS_BY_CERTIFICATE_ID, new Long[]{certificateId},
                (resultSet, i) ->
                        new Tag().setId(resultSet.getLong(ID)).setName(resultSet.getString(NAME)));
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() {
        return jdbcTemplate.query(SqlQuery.GiftCertificate.GET_ALL_CERTIFICATES, (resultSet, i) ->
                new GiftCertificate().setId(resultSet.getLong(ID)).
                        setName(resultSet.getString(NAME)).
                        setTags(getAllTagsByCertificateId(resultSet.getLong(ID))).
                        setDescription(resultSet.getString(DESCRIPTION)).
                        setPrice(resultSet.getInt(PRICE)).
                        setDuration(resultSet.getInt(DURATION)).
                        setCreateDate(resultSet.getDate(CREATE_DATE)).
                        setLastUpdateDate(resultSet.getDate(LAST_UPDATE_DATE)));
    }

    @Override
    public boolean createCertificate(GiftCertificate giftCertificate) {
        return jdbcTemplate.update(SqlQuery.GiftCertificate.CREATE_CERTIFICATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                currentDateTime.now(),
                currentDateTime.now()) == DB_OPERATION_SUCCESSFUL_RESULT;
    }

    @Override
    public List<GiftCertificate> getCertificateById(long id) {
        return jdbcTemplate.query(SqlQuery.GiftCertificate.GET_CERTIFICATE_BY_ID, new Object[]{id},
                (resultSet, i) ->
                        new GiftCertificate().
                                setId(resultSet.getLong(ID)).
                                setName(resultSet.getString(NAME)).
                                setTags(getAllTagsByCertificateId(resultSet.getLong(ID))).
                                setDescription(resultSet.getString(DESCRIPTION)).
                                setPrice(resultSet.getInt(PRICE)).
                                setDuration(resultSet.getInt(DURATION)).
                                setCreateDate(resultSet.getDate(CREATE_DATE)).
                                setLastUpdateDate(resultSet.getDate(LAST_UPDATE_DATE)));
    }

    @Override
    public boolean deleteCertificate(long id) {
        return jdbcTemplate.update(SqlQuery.GiftCertificate.DELETE_CERTIFICATE, id) == DB_OPERATION_SUCCESSFUL_RESULT;
    }

    @Override
    public boolean updateGiftCertificate(long id, Map<String, String> updatesMap) {
        List<Object> updatesParam = new ArrayList<>();

        StringBuilder generatedQuery = new StringBuilder(PART_OF_QUERY_FOR_UPDATING);
        updatesMap.forEach((key, value) -> {

            generatedQuery.append(key).append("= ? ,");
            updatesParam.add(value);
        });
        generatedQuery.append(LAST_UPDATE_DATE + " = ? WHERE " + ID + " = ?");

        updatesParam.addAll(List.of(currentDateTime.now(), id));

        return jdbcTemplate.update(generatedQuery.toString(), updatesParam.toArray()) == DB_OPERATION_SUCCESSFUL_RESULT;
    }

    @Override
    public long getIdByGiftCertificate(GiftCertificate giftCertificate) {
        return jdbcTemplate.queryForObject(SqlQuery.GiftCertificate.GET_ID_BY_GIFT_CERTIFICATE,
                new Object[]{giftCertificate.getName(),
                        giftCertificate.getDescription(),
                        giftCertificate.getPrice(),
                        giftCertificate.getDuration()}, Long.class);
    }

    @Override
    public boolean isGiftCertificateExist(GiftCertificate giftCertificate) {
        return jdbcTemplate.queryForObject(SqlQuery.GiftCertificate.IS_GIFT_CERTIFICATE_EXIST, Boolean.class,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration());
    }

    private void createGiftCertificateTag(long tagId, long giftCertificateId) {
        jdbcTemplate.update(SqlQuery.GiftCertificate.CREATE_GIFT_CERTIFICATE_TAG, giftCertificateId, tagId);
    }

    private void deleteGiftCertificateTag(long tagId, long giftCertificateId) {
        jdbcTemplate.update(SqlQuery.GiftCertificate.DELETE_GIFT_CERTIFICATE_TAG, giftCertificateId, tagId);
    }

    @Override
    public boolean createGiftCertificateTagRelationship(List<Long> tagsId, long giftCertificateId) {
        tagsId.forEach(tagId -> createGiftCertificateTag(tagId, giftCertificateId));

        return true;
    }

    @Override
    public boolean deleteGiftCertificateTagRelationship(List<Long> tagsId, long giftCertificateId) {
        tagsId.forEach(tagId -> deleteGiftCertificateTag(tagId, giftCertificateId));

        return true;
    }
}
