package com.epam.esm.tag;

import com.epam.esm.utils.DataValidation;
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

    @PostMapping(value = "/createTag", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        if (DataValidation.isValidTag(tag)) {
            tagService.createTag(tag);
            return new ResponseEntity<>(Map.of("status", HttpStatus.CREATED), HttpStatus.CREATED);
        }
        throw new IllegalArgumentException("Something went wrong during the request, check your fields");
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAllTags() {
        return new ResponseEntity<>(Map.of("tags", tagService.getAllTags()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        if (DataValidation.moreThenZero(id)) {
            return new ResponseEntity<>(Map.of("tag", tagService.getTagById(id)), HttpStatus.OK);
        }
        throw new IllegalArgumentException("incorrect tag id=" + id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        if (DataValidation.moreThenZero(id)) {
            tagService.deleteTag(id);
            return ResponseEntity.ok(Map.of("status", HttpStatus.OK));
        }
        throw new IllegalArgumentException("incorrect tag id=" + id);
    }
}
