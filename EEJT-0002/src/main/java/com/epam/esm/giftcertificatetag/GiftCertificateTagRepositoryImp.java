package com.epam.esm.giftcertificatetag;

import com.epam.esm.giftcertficate.GiftCertificate;
import com.epam.esm.tag.Tag;
import com.epam.esm.utils.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class GiftCertificateTagRepositoryImp {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateTagRepositoryImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<?> getGiftCertificatesAndTags() {
        return jdbcTemplate.query("SELECT t.id, t.name, gc.id, gc.name, description, price,duration, create_date, last_update_date FROM  gift_certificate_tag gct INNER JOIN tag t ON gct.tag_id = t.id INNER JOIN gift_certificate gc ON gct.gift_certificate_id = gc.id",
                (resultSet, i) -> Map.of(new Tag().setId(resultSet.getLong(1)).setName(resultSet.getString(2)),
                        new GiftCertificate().setId(resultSet.getLong(3)).setName(resultSet.getString(4)).setDescription(resultSet.getString(5)).setPrice(resultSet.getInt(6)).setDuration(resultSet.getInt(7)).setCreateDate(resultSet.getDate(8)).setLastUpdateDate(resultSet.getDate(9))));
    }

    public List<?> getGiftCertificatesAndTagsByNameOrByPartOfName(String partOfName) {
        return jdbcTemplate.query("SELECT t.id, t.name, gc.id, gc.name, description, price,duration, create_date, last_update_date FROM  gift_certificate_tag gct INNER JOIN tag t ON gct.tag_id = t.id AND t.name LIKE ? INNER JOIN gift_certificate gc ON gct.gift_certificate_id = gc.id", new String[]{partOfName + "%"},
                (resultSet, i) -> Map.of(new Tag().setId(resultSet.getLong(1)).setName(resultSet.getString(2)),
                        new GiftCertificate().setId(resultSet.getLong(3)).setName(resultSet.getString(4)).setDescription(resultSet.getString(5)).setPrice(resultSet.getInt(6)).setDuration(resultSet.getInt(7)).setCreateDate(resultSet.getDate(8)).setLastUpdateDate(resultSet.getDate(9))));

    }

    public List<?> sortingAscDescByDate(String method) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.getTypeOfSortingForDate(method),
                (resultSet, i) -> Map.of(new Tag().setId(resultSet.getLong(1)).setName(resultSet.getString(2)),
                        new GiftCertificate().setId(resultSet.getLong(3)).setName(resultSet.getString(4)).setDescription(resultSet.getString(5)).setPrice(resultSet.getInt(6)).setDuration(resultSet.getInt(7)).setCreateDate(resultSet.getDate(8)).setLastUpdateDate(resultSet.getDate(9))));
    }

    public List<?> sortingAscDescByDateAndByName(String method) {
        return jdbcTemplate.query(SqlQuery.TagGiftCertificate.getTypeOfSortingForDateAndName(method),
                (resultSet, i) -> Map.of(new Tag().setId(resultSet.getLong(1)).setName(resultSet.getString(2)),
                        new GiftCertificate().setId(resultSet.getLong(3)).setName(resultSet.getString(4)).setDescription(resultSet.getString(5)).setPrice(resultSet.getInt(6)).setDuration(resultSet.getInt(7)).setCreateDate(resultSet.getDate(8)).setLastUpdateDate(resultSet.getDate(9))));
    }
}
