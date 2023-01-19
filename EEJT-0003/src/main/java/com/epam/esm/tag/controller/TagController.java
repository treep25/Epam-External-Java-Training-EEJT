package com.epam.esm.tag.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.model.TagHateoasResponse;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagController {

    private final TagService tagService;
    private final PagedResourcesAssembler<Tag> representationModelAssembler;

    private final TagHateoasResponse tagHateoasResponse = new TagHateoasResponse();

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        if (DataValidation.isValidTag(tag)) {

            Tag savedTag = tagService.createTag(tag);

            CollectionModel<Tag> collectionModelSavedTag = tagHateoasResponse
                    .getHateoasTagForCreating(savedTag);

            return new ResponseEntity<>(Map.of("saved tag", collectionModelSavedTag), HttpStatus.CREATED);
        }
        throw new ServerException("Something went wrong during the request, check your fields");
    }

    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {

        DataValidation.validatePageAndSizePagination(page, size);

        Page<Tag> allTags = tagService.getAllTags(page, size);

        PagedModel<Tag> tagsPagedModel = tagHateoasResponse
                .getHateoasTagForReading(allTags, representationModelAssembler);

        return ResponseEntity.ok(Map.of("all tags", tagsPagedModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            Tag currentTag = tagService.getTagById(id);

            CollectionModel<Tag> collectionModelCurrentTag = tagHateoasResponse.getHateoasTagForReadingById(currentTag);

            return ResponseEntity.ok(Map.of("tag", collectionModelCurrentTag));
        }
        throw new ServerException("tag id is not valid (id = " + id + ")");
    }

    @GetMapping("/widely-used-tag")
    public ResponseEntity<?> getTheMostWidelyUsedTag() {
        Tag theMostWidelyUsedTag = tagService.getTheMostWidelyUsedTagOfUserOrder();

        CollectionModel<Tag> collectionModelTheMostWidelyUsed = tagHateoasResponse
                .getHateoasTagForGettingTheMostWidelyUsedTag(theMostWidelyUsedTag);

        return ResponseEntity.ok(Map.of("the most widely used tag", collectionModelTheMostWidelyUsed));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {

            tagService.deleteTag(id);
            return ResponseEntity.noContent().build();
        }
        throw new ServerException("The Tag ID is not valid: id = " + id);
    }
}
