package com.epam.esm.user.model;

import com.epam.esm.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class UserDTOMapper {

    public UserDTO convertUserToUserDTO(User user) {
        log.debug("Converting user into userDTO, returns userDTO ");
        return UserDTO.builder().id(user.getId()).username(user.getName()).orders(user.getOrders()).role(user.getRole()).build();
    }

    public Page<UserDTO> convertUserPageToUserDTOPage(Page<User> pagedUsers) {
        log.debug("Converting user page model into userDTO page model");

        List<UserDTO> listUserDTOs = new ArrayList<>();
        pagedUsers.forEach(pagedUser-> listUserDTOs.add(convertUserToUserDTO(pagedUser)));
        log.debug("Returns userDTO pageModel");
        return new PageImpl<>(listUserDTOs);
    }
}
