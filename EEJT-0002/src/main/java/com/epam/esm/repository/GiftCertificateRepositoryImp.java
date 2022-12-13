package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.rowMapper.GiftCertificateRowMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        return jdbcTemplate.query(SqlQuery.GiftCertificate.GET_ALL_CERTIFICATES, new GiftCertificateRowMapper());
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
}
