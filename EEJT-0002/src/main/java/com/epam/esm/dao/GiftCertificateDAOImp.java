package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.rowMapper.GiftCertificateRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class GiftCertificateDAOImp implements GiftCertificateDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDAOImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<Long, GiftCertificate>> getAllGiftCertificates() {
        return jdbcTemplate.query(SqlQuery.GiftCertificate.GET_ALL_CERTIFICATES, new GiftCertificateRowMapper());
    }
}
