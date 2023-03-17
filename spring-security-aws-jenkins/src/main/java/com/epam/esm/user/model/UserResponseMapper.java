package com.epam.esm.user.model;

import com.epam.esm.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Generated
@Slf4j
@Component
public class UserResponseMapper {

    public UserResponse convertUserToUserDTO(User user) {
        log.debug("Converting user into userDTO, returns userDTO ");
        return UserResponse.builder().id(user.getId()).username(user.getName()).orders(user.getOrders()).role(user.getRole()).build();
    }

    public Page<UserResponse> convertUserPageToUserDTOPage(Page<User> pagedUsers) {
        log.debug("Converting user page model into userDTO page model");

        List<UserResponse> listUserResponses = new ArrayList<>();
        pagedUsers.forEach(pagedUser-> listUserResponses.add(convertUserToUserDTO(pagedUser)));
        log.debug("Returns userDTO pageModel");
        return new PageImpl<>(listUserResponses);
    }
}
