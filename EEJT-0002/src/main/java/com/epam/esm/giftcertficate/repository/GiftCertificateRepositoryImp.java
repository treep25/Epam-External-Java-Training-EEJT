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

    @Autowired
    public GiftCertificateRepositoryImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private List<Tag> getAllTagsByCertificateId(long certificateId) {
        return jdbcTemplate.query(SqlQuery.GiftCertificate.GET_ALL_TAGS_BY_CERTIFICATE_ID, new Long[]{certificateId}, (resultSet, i) ->
                new Tag().setId(resultSet.getLong("id")).setName(resultSet.getString("name")));
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() {
        return jdbcTemplate.query(SqlQuery.GiftCertificate.GET_ALL_CERTIFICATES, (resultSet, i) ->
                new GiftCertificate().setId(resultSet.getLong("id")).
                        setName(resultSet.getString("name")).
                        setTags(getAllTagsByCertificateId(resultSet.getLong("id"))).
                        setDescription(resultSet.getString("description")).
                        setPrice(resultSet.getInt("price")).
                        setDuration(resultSet.getInt("duration")).
                        setCreateDate(resultSet.getDate("create_date")).
                        setLastUpdateDate(resultSet.getDate("last_update_date")));
    }

    @Override
    public boolean createCertificate(GiftCertificate giftCertificate) {
        return jdbcTemplate.update(SqlQuery.GiftCertificate.CREATE_CERTIFICATE, giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                Instant.now(),
                Instant.now()) == 1;
    }

    @Override
    public List<GiftCertificate> getCertificateById(long id) {
        return jdbcTemplate.query(SqlQuery.GiftCertificate.GET_CERTIFICATE_BY_ID, new Object[]{id}, (resultSet, i) ->
                new GiftCertificate().
                        setId(resultSet.getLong("id")).
                        setName(resultSet.getString("name")).
                        setTags(getAllTagsByCertificateId(resultSet.getLong("id"))).
                        setDescription(resultSet.getString("description")).
                        setPrice(resultSet.getInt("price")).
                        setPrice(resultSet.getInt("price")).
                        setDuration(resultSet.getInt("duration")).
                        setCreateDate(resultSet.getDate("create_date")).
                        setLastUpdateDate(resultSet.getDate("last_update_date")));
    }

    @Override
    public boolean deleteCertificate(long id) {
        return jdbcTemplate.update(SqlQuery.GiftCertificate.DELETE_CERTIFICATE, id) == 1;
    }

    @Override
    public boolean updateGiftCertificate(long id, Optional<Map<String, String>> updatesMap) {
        List<Object> updatesParam = new ArrayList<>();

        StringBuilder generatedQuery = new StringBuilder("UPDATE gift_certificate SET ");
        updatesMap.get().forEach((key, value) -> {

            generatedQuery.append(key).append("= ? ,");
            updatesParam.add(value);
        });
        generatedQuery.append("last_update_date = ? WHERE id = ?");

        updatesParam.addAll(List.of(Instant.now(), id));

        return jdbcTemplate.update(generatedQuery.toString(), updatesParam.toArray()) == 1;
    }

    @Override
    public long getIdByGiftCertificate(GiftCertificate giftCertificate) {
        return jdbcTemplate.queryForObject(SqlQuery.GiftCertificate.GET_ID_BY_GIFT_CERTIFICATE,
                new Object[]{giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration()}
                , Long.class);
    }

    @Override
    public boolean isGiftCertificateExist(GiftCertificate giftCertificate) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(SqlQuery.GiftCertificate.IS_GIFT_CERTIFICATE_EXIST, Boolean.class,
                giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration()));
    }

    private void createGiftCertificateTag(long tagId, long giftCertificateId) {
        jdbcTemplate.update(SqlQuery.GiftCertificate.CREATE_GIFT_CERTIFICATE_TAG, giftCertificateId, tagId);
    }

    private void deleteGiftCertificateTag(long tagId, long giftCertificateId) {
        jdbcTemplate.update(SqlQuery.GiftCertificate.DELETE_GIFT_CERTIFICATE_TAG, giftCertificateId, tagId);
    }

    @Override
    public boolean createGiftCertificateTagRelationship(List<Long> tagsId, long giftCertificateId) {
        for (long tagId : tagsId) {
            createGiftCertificateTag(tagId, giftCertificateId);
        }
        return true;
    }

    @Override
    public boolean deleteGiftCertificateTagRelationship(List<Long> tagsId, long giftCertificateId) {
        for (long tagId : tagsId) {
            deleteGiftCertificateTag(tagId, giftCertificateId);
        }
        return true;
    }
}
