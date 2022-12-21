package com.epam.esm.tag;

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
        jdbcTemplate.update("INSERT INTO tag (name) VALUES(?)", tag.getName());
    }

    @Override
    public int deleteTag(long id) {
        return jdbcTemplate.update("DELETE  FROM tag WHERE id=?", id);
    }

    @Override
    public List<Tag> getAllTags() {
        return jdbcTemplate.query("SELECT * FROM tag", (resultSet, i) ->
                new Tag().setId(resultSet.getLong("id")).setName(resultSet.getString("name")));
    }

    @Override
    public List<Tag> getTagById(long id) {
        return jdbcTemplate.query("SELECT * FROM tag WHERE id = ?", new Long[]{id}, (resultSet, i) ->
                new Tag().setId(resultSet.getLong("id")).setName(resultSet.getString("name")));
    }

    @Override
    public boolean isTagWithThisNameExists(String name) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT * FROM tag WHERE name=?)", Boolean.class, name));
    }

    @Override
    public long getIdByTag(Tag tag) {
        return jdbcTemplate.queryForObject("SELECT id FROM tag WHERE name = ?", new String[]{tag.getName()}, Long.class);
    }
}
