package com.epam.esm.user.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.user.model.User;
import com.epam.esm.user.model.UserHateoasBuilder;
import com.epam.esm.user.service.UserService;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;
    private final UserHateoasBuilder userHateoasBuilder;

    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model fields");

        DataValidation.validatePageAndSizePagination(page, size);

        Page<User> allUsers = userService.getAllUsers(page, size);
        log.debug("Receive all users");

        PagedModel<User> allUsersPagedModel = userHateoasBuilder
                .getHateoasUserForReading(allUsers);
        log.debug("Return Hateoas model of users");

        return ResponseEntity.ok(Map.of("users", allUsersPagedModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable long id) {
        log.debug("Validation request model User ID");

        if (DataValidation.moreThenZero(id)) {
            User currentUser = userService.getById(id);
            log.debug("Receive user");

            CollectionModel<User> userCollectionModel = userHateoasBuilder.getHateoasUserForReadingById(currentUser);
            log.debug("Return Hateoas model of user");

            return ResponseEntity.ok(Map.of("user", userCollectionModel));
        }
        log.error("The user ID is not valid: id = " + id);
        throw new ServerException("the user ID is not valid: id = " + id);
    }
}
