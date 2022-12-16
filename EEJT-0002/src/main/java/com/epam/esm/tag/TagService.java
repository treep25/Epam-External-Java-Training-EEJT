package com.epam.esm.tag;

import com.epam.esm.giftcertficate.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepositoryImpl tagRepositoryImpl;

    @Autowired
    public TagService(TagRepositoryImpl tagRepositoryImpl) {
        this.tagRepositoryImpl = tagRepositoryImpl;
    }

    public void createTag(Tag tag) {
        tagRepositoryImpl.createTag(tag);
    }

    public List<Tag> getAllTags() {
        return tagRepositoryImpl.getAllTags();
    }

    public Tag getTagById(long id) {
        Tag tag = tagRepositoryImpl.getTagById(id);
        if (tag != null) {
            return tag;
        }
        throw new ItemNotFoundException("There are no gift certificate with id= " + id);
    }

    public void deleteTag(long id) {
        if (tagRepositoryImpl.deleteTag(id) < 1) {
            throw new ItemNotFoundException("there is no gift certificate with id= " + id);
        }
    }

    public boolean getTagByName(String name) {
        return tagRepositoryImpl.getTagByName(name);
    }
}
