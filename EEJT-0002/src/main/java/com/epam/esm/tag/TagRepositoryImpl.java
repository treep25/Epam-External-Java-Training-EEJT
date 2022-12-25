package com.epam.esm.tag;

import com.epam.esm.utils.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTag(Tag tag) {
        jdbcTemplate.update(SqlQuery.Tag.CREATE_TAG, tag.getName());
    }

    @Override
    public int deleteTag(long id) {
        return jdbcTemplate.update(SqlQuery.Tag.DELETE_TAG, id);
    }

    @Override
    public List<Tag> getAllTags() {
        return jdbcTemplate.query(SqlQuery.Tag.GET_ALL_TAGS, (resultSet, i) ->
                new Tag().setId(resultSet.getLong("id")).setName(resultSet.getString("name")));
    }

    @Override
    public List<Tag> getTagById(long id) {
        return jdbcTemplate.query(SqlQuery.Tag.GET_TAG_BY_ID, new Long[]{id}, (resultSet, i) ->
                new Tag().setId(resultSet.getLong("id")).setName(resultSet.getString("name")));
    }

    @Override
    public boolean isTagWithNameExists(String name) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(SqlQuery.Tag.IS_TAG_WITH_NAME_EXISTS, Boolean.class, name));
    }

    @Override
    public long getIdByTag(Tag tag) {
        return jdbcTemplate.queryForObject(SqlQuery.Tag.GET_ID_BY_TAG, new String[]{tag.getName()}, Long.class);
    }

    @Override
    public List<Tag> getAllTagsByCertificateId(long id) {
        return jdbcTemplate.query(SqlQuery.Tag.GET_ALL_TAGS_BY_CERTIFICATE_ID, new Long[]{id}, (resultSet, i) ->
                new Tag().setId(resultSet.getLong("id")).setName(resultSet.getString("name")));
    }
}
