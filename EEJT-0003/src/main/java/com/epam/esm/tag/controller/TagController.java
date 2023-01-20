package com.epam.esm.tag.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.model.TagHateoasResponse;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagController {

    private final TagService tagService;
    private final PagedResourcesAssembler<Tag> representationModelAssembler;

    private final TagHateoasResponse tagHateoasResponse = new TagHateoasResponse();

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        log.debug("Validation of request model of tag");

        if (DataValidation.isValidTag(tag)) {

            Tag savedTag = tagService.createTag(tag);
            log.debug("Receive tag");

            CollectionModel<Tag> collectionModelSavedTag = tagHateoasResponse
                    .getHateoasTagForCreating(savedTag);
            log.debug("Return Hateoas model of current tag");

            return new ResponseEntity<>(Map.of("saved tag", collectionModelSavedTag), HttpStatus.CREATED);
        }
        log.error("Something went wrong during the request, check your fields");
        throw new ServerException("Something went wrong during the request, check your fields");
    }

    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model params");

        DataValidation.validatePageAndSizePagination(page, size);

        Page<Tag> allTags = tagService.getAllTags(page, size);
        log.debug("Receive tags");

        PagedModel<Tag> tagsPagedModel = tagHateoasResponse
                .getHateoasTagForReading(allTags, representationModelAssembler);
        log.debug("Return Hateoas model of tags");

        return ResponseEntity.ok(Map.of("all tags", tagsPagedModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable("id") long id) {
        log.debug("Validation of request model field ID");

        if (DataValidation.moreThenZero(id)) {

            Tag currentTag = tagService.getTagById(id);
            log.debug("Receive tag");

            CollectionModel<Tag> collectionModelCurrentTag = tagHateoasResponse.getHateoasTagForReadingById(currentTag);
            log.debug("Return Hateoas model of current tag");

            return ResponseEntity.ok(Map.of("tag", collectionModelCurrentTag));
        }
        log.error("tag id is not valid (id = " + id + ")");
        throw new ServerException("tag id is not valid (id = " + id + ")");
    }

    @GetMapping("/widely-used-tag")
    public ResponseEntity<?> getTheMostWidelyUsedTag() {
        Tag theMostWidelyUsedTag = tagService.getTheMostWidelyUsedTagOfUserOrder();
        log.debug("Receive tag");

        CollectionModel<Tag> collectionModelTheMostWidelyUsed = tagHateoasResponse
                .getHateoasTagForGettingTheMostWidelyUsedTag(theMostWidelyUsedTag);
        log.debug("Return Hateoas model of current tag");

        return ResponseEntity.ok(Map.of("the most widely used tag", collectionModelTheMostWidelyUsed));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        log.debug("Validation of request model field ID");

        if (DataValidation.moreThenZero(id)) {
            tagService.deleteTag(id);
            log.debug("deleted - return noContent");

            return ResponseEntity.noContent().build();
        }
        log.error("The Tag ID is not valid: id = " + id);
        throw new ServerException("The Tag ID is not valid: id = " + id);
    }
}
