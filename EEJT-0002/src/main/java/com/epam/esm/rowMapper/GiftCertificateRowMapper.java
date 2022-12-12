package com.epam.esm.rowMapper;

import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GiftCertificateRowMapper implements RowMapper<Map<Long, GiftCertificate>> {
    @Override
    public Map<Long, GiftCertificate> mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Map<Long, GiftCertificate> giftCertificateMap = new HashMap<>();
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(resultSet.getLong("id"));
        giftCertificate.setName(resultSet.getString("name"));
        giftCertificate.setDescription(resultSet.getString("description"));
        giftCertificate.setPrice(resultSet.getInt("price"));
        giftCertificate.setDuration(resultSet.getInt("duration"));
        giftCertificate.setCreateDate(resultSet.getDate("create_date"));
        giftCertificate.setLastUpdateDate(resultSet.getDate("last_update_date"));
        giftCertificateMap.put(giftCertificate.getId(), giftCertificate);
        return giftCertificateMap;
    }
}
