package com.epam.esm.tag;

import com.epam.esm.utils.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    private final TagRepositoryImpl tagRepositoryImpl;

    @Autowired
    public TagService(TagRepositoryImpl tagRepositoryImpl) {
        this.tagRepositoryImpl = tagRepositoryImpl;
    }

    public void createTag(Tag tag) {
        if (!isTagByNameExists(tag.getName())) {
            tagRepositoryImpl.createTag(tag);
        } else {
            throw new IllegalArgumentException("This tag has already existed");
        }
    }

    public List<Tag> getAllTags() {
        List<Tag> allTags = tagRepositoryImpl.getAllTags();
        if (!allTags.isEmpty()) {
            return allTags;
        }
        throw new ItemNotFoundException("There are no tags");
    }

    public List<Tag> getTagById(long id) {
        List<Tag> tag = tagRepositoryImpl.getTagById(id);
        if (!tag.isEmpty()) {
            return tag;
        }
        throw new ItemNotFoundException("There are no gift certificate with id= " + id);
    }

    public void deleteTag(long id) {
        if (tagRepositoryImpl.deleteTag(id) != 1) {
            throw new ItemNotFoundException("there is no gift certificate with id= " + id);
        }
    }

    public boolean isTagByNameExists(String name) {
        return tagRepositoryImpl.isTagWithNameExists(name);
    }

    public void isTagsExistOrElseCreate(List<Tag> tags) {
        for (Tag tag : tags) {
            if (!isTagByNameExists(tag.getName())) {
                createTag(tag);
            }
        }
    }

    public long getTagIdByName(Tag tag) {
        return tagRepositoryImpl.getIdByTag(tag);
    }

    public List<Long> getTagsIdByNames(List<Tag> tags) {
        List<Long> listTagsId = new ArrayList<>();
        for (Tag tag : tags) {
            listTagsId.add(getTagIdByName(tag));
        }
        return listTagsId;
    }

    public List<Tag> getAllTagsByCertificateId(long id) {
        return tagRepositoryImpl.getAllTagsByCertificateId(id);
    }
}
