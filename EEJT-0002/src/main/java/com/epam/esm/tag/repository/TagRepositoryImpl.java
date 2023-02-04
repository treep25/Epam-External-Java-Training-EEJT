package com.epam.esm.tag.repository;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.query.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final int DB_OPERATION_SUCCESSFUL_RESULT = 1;


    @Autowired
    public TagRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createTag(Tag tag) {
        return jdbcTemplate.update(SqlQuery.Tag.CREATE_TAG, tag.getName()) == DB_OPERATION_SUCCESSFUL_RESULT;
    }

    @Override
    public boolean deleteTag(long id) {
        return jdbcTemplate.update(SqlQuery.Tag.DELETE_TAG, id) == DB_OPERATION_SUCCESSFUL_RESULT;
    }

    @Override
    public List<Tag> getAllTags() {
        return jdbcTemplate.query(SqlQuery.Tag.GET_ALL_TAGS, (resultSet, i) ->
                new Tag().setId(resultSet.getLong(ID)).setName(resultSet.getString(NAME)));
    }

    @Override
    public List<Tag> getTagById(long id) {
        return jdbcTemplate.query(SqlQuery.Tag.GET_TAG_BY_ID, new Long[]{id}, (resultSet, i) ->
                new Tag().setId(resultSet.getLong(ID)).setName(resultSet.getString(NAME)));
    }

    @Override
    public boolean isTagWithNameExists(String name) {
        return jdbcTemplate.queryForObject(SqlQuery.Tag.IS_TAG_WITH_NAME_EXISTS, Boolean.class, name);
    }

    @Override
    public long getIdByTag(Tag tag) {
        return jdbcTemplate.queryForObject(SqlQuery.Tag.GET_ID_BY_TAG, new String[]{tag.getName()}, Long.class);
    }

    @Override
    public List<Tag> getAllTagsByCertificateId(long id) {
        return jdbcTemplate.query(SqlQuery.Tag.GET_ALL_TAGS_BY_CERTIFICATE_ID, new Long[]{id}, (resultSet, i) ->
                new Tag().setId(resultSet.getLong(ID)).setName(resultSet.getString(NAME)));
    }
}
