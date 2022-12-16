package com.epam.esm.tag;

import java.util.List;
import java.util.Map;

public interface TagRepository {
    void createTag(Tag tag);

    int deleteTag(long id);

    List<Tag> getAllTags();

    Tag getTagById(long id);

    boolean getTagByName(String name);
}
