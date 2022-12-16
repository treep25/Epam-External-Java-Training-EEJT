package com.epam.esm.giftcertficate;

import com.epam.esm.utils.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Repository
public class GiftCertificateRepositoryImp implements GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateRepositoryImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() {
        return jdbcTemplate.query(SqlQuery.GiftCertificate.GET_ALL_CERTIFICATES, (resultSet, i) ->
                new GiftCertificate().setId(resultSet.getLong("id")).
                        setName(resultSet.getString("name")).
                        setDescription(resultSet.getString("description")).
                        setPrice(resultSet.getInt("price")).
                        setPrice(resultSet.getInt("price")).
                        setDuration(resultSet.getInt("duration")).
                        setCreateDate(resultSet.getDate("create_date")).
                        setLastUpdateDate(resultSet.getDate("last_update_date")));
    }

    @Override
    public void createCertificate(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SqlQuery.GiftCertificate.CREATE_CERTIFICATE, giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                Instant.now(),
                Instant.now());
    }


    @Override
    public GiftCertificate getCertificateById(long id) {
        return jdbcTemplate.queryForObject(SqlQuery.GiftCertificate.GET_CERTIFICATE_BY_ID, new Object[]{id}, (resultSet, i) ->
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
        try {
            StringBuilder generatedQuery = new StringBuilder("UPDATE gift_certificate SET ");
            for (Map.Entry<String, ?> entry : updatesMap.entrySet()) {
                generatedQuery.append(entry.getKey()).append("= '").append(entry.getValue()).append("',");
            }
            generatedQuery.append("last_update_date = ? WHERE id = ?");
            return jdbcTemplate.update(generatedQuery.toString(), Instant.now(), id);

        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Incorrect input");
        }

    }
}
