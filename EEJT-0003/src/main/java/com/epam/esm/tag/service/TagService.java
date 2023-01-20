package com.epam.esm.tag.service;


import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagService {
    private final TagRepository tagRepository;

    public Tag createTag(Tag tag) {
        log.info("Service receives tag for creating");

        log.debug("Verifying tag has not existed");
        if (isTagNotExistsByName(tag.getName())) {

            log.debug("Service returns representation of current tag");
            return tagRepository.save(tag);
        }
        log.error("This tag with (name = " + tag.getName() + ") has already existed");
        throw new ServerException("this tag with (name = " + tag.getName() + ") has already existed");
    }

    public boolean isTagNotExistsByName(String name) {
        log.info("Service receives name for verifying is tag exist");

        log.debug("Service returns result of this operation");
        return !tagRepository.isTagExistsByName(name);
    }

    public Page<Tag> getAllTags(int page, int size) {
        log.info("Service receives params for getting all");

        PageRequest pageRequest = PageRequest.of(page, size);

        log.debug("Service returns representation of all tags");
        return tagRepository.findAll(pageRequest);
    }

    public Tag getTagById(long id) {
        log.info("Service receives ID for getting");
        log.debug("Service returns representation of current tag");

        return tagRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("there are no tags with (ID = " + id + ")"));
    }

    public boolean deleteTag(long id) {
        log.info("Service receives params for deleting");

        log.debug("Checking that tag exists");
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);

            log.debug("Service returns result od deleting");
            return true;
        }
        log.error("There is no tag with (id = " + id + ")");
        throw new ItemNotFoundException("there is no tag with (id = " + id + ")");
    }

    public long getTagIdByTag(Tag tag) {
        log.info("Service receives tag model for getting ID");
        log.debug("Service returns ID of current tag");

        return tagRepository.getIdByTagName(tag.getName());
    }

    public Tag getTheMostWidelyUsedTagOfUserOrder() {
        log.info("Service receives nothing for getting");
        log.debug("Service returns current of tag");

        return tagRepository.getTheMostWidelyUsedTag();
    }

    public Set<Tag> verifyIsTagsExistWhenCreatingOrUpdatingGiftCertificate(Set<Tag> tags) {
        log.info("Service receives Set tag model for verifying exist tag or not for creating and updating");

        tags.forEach(tag -> {
            if (!isTagNotExistsByName(tag.getName())) {
                tag.setId(getTagIdByTag(tag));
            }
        });
        tagRepository.saveAll(tags);
        log.debug("Service returns representation of current tags");

        return tags;
    }
}
