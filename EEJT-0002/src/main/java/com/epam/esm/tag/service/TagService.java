package com.epam.esm.tag.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public int createTag(Tag tag) {
        if (!isTagByNameExists(tag.getName())) {
            return tagRepository.createTag(tag);
        } else {
            throw new IllegalArgumentException("This tag has already existed");
        }
    }

    public List<Tag> getAllTags() {
        List<Tag> allTags = tagRepository.getAllTags();
        if (!allTags.isEmpty()) {
            return allTags;
        }
        throw new ItemNotFoundException("There are no tags");
    }

    public List<Tag> getTagById(long id) {
        List<Tag> tag = tagRepository.getTagById(id);
        if (!tag.isEmpty()) {
            return tag;
        }
        throw new ItemNotFoundException("There are no tags with id= " + id);
    }

    public int deleteTag(long id) {
        int result = tagRepository.deleteTag(id);
        if (result == 1) {
            return result;
        }
        throw new ItemNotFoundException("There is no tag with id= " + id);
    }

    public boolean isTagByNameExists(String name) {
        return tagRepository.isTagWithNameExists(name);
    }

    public void isTagsExistOrElseCreate(List<Tag> tags) {
        for (Tag tag : tags) {
            if (!isTagByNameExists(tag.getName())) {
                createTag(tag);
            }
        }
    }

    public long getTagIdByTag(Tag tag) {
        return tagRepository.getIdByTag(tag);
    }

    public List<Long> getTagsIdByTags(List<Tag> tags) {
        List<Long> listTagsId = new ArrayList<>();
        for (Tag tag : tags) {
            listTagsId.add(getTagIdByTag(tag));
        }
        return listTagsId;
    }

    public List<Tag> getAllTagsByCertificateId(long id) {
        return tagRepository.getAllTagsByCertificateId(id);
    }
}
