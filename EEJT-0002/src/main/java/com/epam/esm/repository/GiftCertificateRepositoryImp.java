package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public HttpStatus createCertificate(GiftCertificate giftCertificate) {
        jdbcTemplate.update("INSERT INTO gift_certificate(name,description,price,duration,create_date,last_update_date) VALUES(?,?,?,?,?,?)", giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdateDate());
        return HttpStatus.CREATED;
    }

    //TODO remake this cannot find solving
    @Override
    public GiftCertificate getCertificateById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM gift_certificate WHERE id=?", (resultSet, i) ->
                new GiftCertificate().setId(resultSet.getLong("id")).
                        setName(resultSet.getString("name")).
                        setDescription(resultSet.getString("description")).
                        setPrice(resultSet.getInt("price")).
                        setPrice(resultSet.getInt("price")).
                        setDuration(resultSet.getInt("duration")).
                        setCreateDate(resultSet.getDate("create_date")).
                        setLastUpdateDate(resultSet.getDate("last_update_date")));
    }
}
