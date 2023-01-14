package com.epam.esm.tag.service;


import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagService {
    private final TagRepository tagRepository;


    public Tag createTag(Tag tag) {
        if (isTagNotExistsByName(tag.getName())) {
            return tagRepository.save(tag);
        }

        throw new ServerException("This tag with (name = " + tag.getName() + ") has already existed");
    }

    public boolean isTagNotExistsByName(String name) {
        return !tagRepository.isTagExistsByName(name);
    }

    public List<Tag> getAllTags() {

        return tagRepository.findAll();
    }

    public Tag getTagById(long id) {

        return tagRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("There are no tags with (ID = " + id + ")"));
    }

    public boolean deleteTag(long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);

            return true;
        }
        throw new ItemNotFoundException("There is no tag with (id = " + id + ")");
    }

    public boolean isTagsExistOrElseCreate(List<Tag> tags) {
        for (Tag tag : tags) {
            if (isTagNotExistsByName(tag.getName())) {
                createTag(tag);
            }
        }
        return true;
    }

    public long getTagIdByTag(Tag tag) {
        return tagRepository.getIdByTagName(tag.getName());
    }

    public List<Long> getTagsIdByTags(List<Tag> tags) {
        List<Long> listTagsId = new ArrayList<>();
        for (Tag tag : tags) {
            listTagsId.add(getTagIdByTag(tag));
        }
        return listTagsId;
    }

    public List<Tag> getAllTagsByGiftCertificateId(long id) {
        return tagRepository.getAllTagsByGiftCertificateId(id);
    }

    public Set<Tag> verifyIsTagsExistWhenCreatingOrUpdatingGiftCertificate(Set<Tag> tags) {
        tags.forEach(tag -> {
            if (!isTagNotExistsByName(tag.getName())) {
                tag.setId(getTagIdByTag(tag));
            }
        });
        tagRepository.saveAll(tags);

        return tags;
    }
}
