package com.epam.esm.tag.service;


import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<Tag> getAllTags(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return tagRepository.findAll(pageRequest);
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

    public long getTagIdByTag(Tag tag) {
        return tagRepository.getIdByTagName(tag.getName());
    }

    public Tag getTheMostWidelyUsedTagOfUserOrder() {
        return tagRepository.getTheMostWidelyUsedTag();
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
