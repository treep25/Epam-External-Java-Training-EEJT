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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

@Repository
public class GiftCertificateRepositoryImp implements GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public GiftCertificateRepositoryImp(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() {
        return jdbcTemplate.query(SqlQuery.GiftCertificate.GET_ALL_CERTIFICATES, (resultSet, i) ->
                new GiftCertificate().setId(resultSet.getLong("id")).
                        setName(resultSet.getString("name")).
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
    public int updateGiftCertificate(long id, Map<String, ?> updatesMap) {
        List<Object> listObj = new ArrayList<>();
        StringBuilder generatedQuery = new StringBuilder("UPDATE gift_certificate SET ");
        for (Map.Entry<String, ?> entry : updatesMap.entrySet()) {
            generatedQuery.append(entry.getKey()).append("= ? ,");
            listObj.add(entry.getValue());
        }

        generatedQuery.append("last_update_date = ? WHERE id = ?");
        listObj.addAll(List.of(Instant.now(), id));

        return jdbcTemplate.update(generatedQuery.toString(), listObj.toArray());
    }

    @Override
    public long getIdByGiftCertificate(GiftCertificate giftCertificate) {
        return jdbcTemplate.queryForObject("SELECT id FROM gift_certificate WHERE name = ? AND description = ? AND price = ? AND duration = ?",
                new Object[]{giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration()}
                , Long.class);
    }

    public boolean isGiftCertificateExist(GiftCertificate giftCertificate) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT * FROM gift_certificate WHERE name=? AND description = ? AND price = ? AND duration = ?)", Boolean.class,
                giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration()));
    }

    @Override
    public void createGiftCertificateTag(long tagId, long giftCertificateId) {
        transactionTemplate.execute((TransactionCallback<Object>) transactionStatus ->
                jdbcTemplate.update("INSERT INTO gift_certificate_tag (gift_certificate_id,tag_id) VALUES (?,?)", giftCertificateId, tagId));
    }

    @Override
    public int deleteGiftCertificateTag(long giftCertificateId) {
        return jdbcTemplate.update("DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?", giftCertificateId);
    }

    @Override
    public Optional<String> getGiftCertificateNameById(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT name FROM gift_certificate WHERE id = ?", new Long[]{id}, String.class));
    }
}
