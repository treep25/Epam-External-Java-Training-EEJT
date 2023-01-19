package com.epam.esm.user.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.user.model.User;
import com.epam.esm.user.model.UserHateoasResponse;
import com.epam.esm.user.service.UserService;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;
    private final UserHateoasResponse userHateoasResponse = new UserHateoasResponse();
    private final PagedResourcesAssembler<User> representationModelAssembler;

    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {

        DataValidation.validatePageAndSizePagination(page, size);

        Page<User> allUsers = userService.getAllUsers(page, size);

        PagedModel<User> allUsersPagedModel = userHateoasResponse
                .getHateoasUserForReading(allUsers, representationModelAssembler);

        return ResponseEntity.ok(Map.of("users", allUsersPagedModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable long id) {
        if (DataValidation.moreThenZero(id)) {
            User currentUser = userService.getById(id);

            CollectionModel<User> userCollectionModel = userHateoasResponse.getHateoasUserForReadingById(currentUser);

            return ResponseEntity.ok(Map.of("user", userCollectionModel));
        }
        throw new ServerException("The user ID is not valid: id = " + id);
    }
}
