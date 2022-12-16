package com.epam.esm.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
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
                new Tag(resultSet.getLong("id"), resultSet.getString("name")));
    }

    @Override
    public Tag getTagById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM tag WHERE id = ?", new Object[]{id}, (resultSet, i) ->
                new Tag(resultSet.getLong("id"), resultSet.getString("name")));
    }

    @Override
    public boolean getTagByName(String name) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT * FROM tag WHERE name=?)", Boolean.class, name));
    }
}
