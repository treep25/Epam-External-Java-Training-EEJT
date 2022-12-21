package com.epam.esm.tag;

import java.util.List;

public interface TagRepository {
    void createTag(Tag tag);

    int deleteTag(long id);

    List<Tag> getAllTags();

    List<Tag> getTagById(long id);

    boolean isTagWithThisNameExists(String name);

    long getIdByTag(Tag tag);
}
