package com.epam.esm.giftcertficate;

import com.epam.esm.tag.Tag;
import com.epam.esm.utils.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.swing.text.html.Option;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;

@Repository
public class GiftCertificateRepositoryImp implements GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public GiftCertificateRepositoryImp(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
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
    public void createCertificate(GiftCertificate giftCertificate) {
        transactionTemplate.execute((TransactionCallback<Object>) transactionStatus ->
                jdbcTemplate.update(SqlQuery.GiftCertificate.CREATE_CERTIFICATE, giftCertificate.getName(),
                        giftCertificate.getDescription(),
                        giftCertificate.getPrice(),
                        giftCertificate.getDuration(),
                        Instant.now(),
                        Instant.now()));
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
    public int deleteCertificate(long id) {
        return jdbcTemplate.update(SqlQuery.GiftCertificate.DELETE_CERTIFICATE, id);
    }

    @Override
    public int updateGiftCertificate(long id, Optional<Map<String, String>> updatesMap) {
        List<Object> updatesParam = new ArrayList<>();

        StringBuilder generatedQuery = new StringBuilder("UPDATE gift_certificate SET ");
        updatesMap.get().forEach((key, value) -> {

            generatedQuery.append(key).append("= ? ,");
            updatesParam.add(value);
        });
        generatedQuery.append("last_update_date = ? WHERE id = ?");

        updatesParam.addAll(List.of(Instant.now(), id));

        return jdbcTemplate.update(generatedQuery.toString(), updatesParam.toArray());
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
        transactionTemplate.execute((TransactionCallback<Object>) transactionStatus ->
                jdbcTemplate.update(SqlQuery.GiftCertificate.CREATE_GIFT_CERTIFICATE_TAG, giftCertificateId, tagId));
    }

    @Override
    public void createGiftCertificateTagList(List<Long> tagsId, long giftCertificateId) {
        for (long tagId : tagsId) {
            createGiftCertificateTag(tagId, giftCertificateId);
        }
    }
}
