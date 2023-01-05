package com.epam.esm.tag.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.validation.DataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        if (DataValidation.isValidTag(tag)) {
            if (tagService.createTag(tag)) {
                return new ResponseEntity<>(Map.of("status", HttpStatus.CREATED), HttpStatus.CREATED);
            }
        }
        throw new ServerException("Something went wrong during the request, check your fields");
    }

    @GetMapping
    public ResponseEntity<?> getAllTags() {
        return new ResponseEntity<>(Map.of("tags", tagService.getAllTags()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        if (DataValidation.moreThenZero(id)) {
            return new ResponseEntity<>(Map.of("tag", tagService.getTagById(id)), HttpStatus.OK);
        }
        throw new ServerException("incorrect tag id=" + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        if (DataValidation.moreThenZero(id)) {
            tagService.deleteTag(id);
            return ResponseEntity.ok(Map.of("status", HttpStatus.OK));
        }
        throw new ServerException("incorrect tag id=" + id);
    }
}
