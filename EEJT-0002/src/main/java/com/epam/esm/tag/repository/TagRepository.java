package com.epam.esm.tag.repository;

import com.epam.esm.tag.model.Tag;

import java.util.List;

public interface TagRepository {
    boolean createTag(Tag tag);

    boolean deleteTag(long id);

    List<Tag> getAllTags();

    List<Tag> getTagById(long id);

    boolean isTagWithNameExists(String name);

    long getIdByTag(Tag tag);

    List<Tag> getAllTagsByCertificateId(long id);
}
