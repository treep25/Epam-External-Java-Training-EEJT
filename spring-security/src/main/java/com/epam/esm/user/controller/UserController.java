package com.epam.esm.user.controller;

import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.user.dto.UserDTO;
import com.epam.esm.user.model.User;
import com.epam.esm.user.model.UserDTOBuilder;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;
    private final UserHateoasBuilder userHateoasBuilder;

    private final UserDTOBuilder userDTOBuilder;

    @GetMapping
    public ResponseEntity<?> read(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size) {
        log.debug("Validation of request model fields " + page + " " + size);

        DataValidation.validatePageAndSizePagination(page, size);

        Page<User> allUsers = userService.getAllUsers(page, size);

        Page<UserDTO> userDTOs = userDTOBuilder.convertUserPageToUserDTOPage(allUsers);
        log.debug("Receive all users");

        PagedModel<UserDTO> allUsersPagedModel = userHateoasBuilder
                .getHateoasUserForReading(userDTOs);
        log.debug("Return Hateoas model of users");

        return ResponseEntity.ok(Map.of("users", allUsersPagedModel));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authUserComponent.hasPermission(#user,#id)")
    public ResponseEntity<?> readById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        User currentUser = userService.getById(id);
        log.debug("Receive user");

        UserDTO userDTO = userDTOBuilder.convertUserToUserDTO(currentUser);

        CollectionModel<UserDTO> userCollectionModel = userHateoasBuilder.getHateoasUserForReadingById(userDTO);
        log.debug("Return Hateoas model of user");

        return ResponseEntity.ok(Map.of("user", userCollectionModel));

    }
}
