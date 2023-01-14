package com.epam.esm.tag.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        if (DataValidation.isValidTag(tag)) {

            Tag savedTag = tagService.createTag(tag);
            savedTag
                    .add(linkTo(methodOn(TagController.class)
                            .delete(savedTag.getId()))
                            .withRel(() -> "delete tag"))
                    .add(linkTo(methodOn(TagController.class)
                            .readById(savedTag.getId()))
                            .withRel(() -> "get tag"))
                    .add(linkTo(methodOn(TagController.class)
                            .read())
                            .withRel(() -> "get all tags"));

            return ResponseEntity.ok(tagService.createTag(tag));
        }
        throw new ServerException("Something went wrong during the request, check your fields");
    }

    @GetMapping
    public ResponseEntity<?> read() {
        List<Tag> allTags = tagService.getAllTags();

        allTags.forEach(tag -> {
            tag.add(linkTo(methodOn(TagController.class)
                            .delete(tag.getId()))
                            .withRel(() -> "delete tag"))
                    .add(linkTo(methodOn(TagController.class)
                            .create(new Tag()))
                            .withRel(() -> "create tag"))
                    .add(linkTo(methodOn(TagController.class)
                            .readById(tag.getId()))
                            .withRel(() -> "get tag"));
        });

        return ResponseEntity.ok(Map.of("tags", allTags));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable("id") long id) {
        if (DataValidation.moreThenZero(id)) {
            Tag currentTag = tagService.getTagById(id);
            currentTag
                    .add(linkTo(methodOn(TagController.class)
                            .delete(currentTag.getId()))
                            .withRel(() -> "delete tag"))
                    .add(linkTo(methodOn(TagController.class)
                            .create(new Tag()))
                            .withRel(() -> "create tag"))
                    .add(linkTo(methodOn(TagController.class)
                            .read())
                            .withRel(() -> "get all tags"));

            return ResponseEntity.ok(Map.of("tag", currentTag));
        }
        throw new ServerException("The Tag ID is not valid: id = " + id);
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
