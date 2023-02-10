package com.epam.esm.user.model;

import com.epam.esm.user.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserDTOBuilder {

    public UserDTO convertUserToUserDTO(User user) {
        return UserDTO.builder().id(user.getId()).username(user.getName()).orders(user.getOrders()).role(user.getRole()).build();
    }

    public Page<UserDTO> convertUserPageToUserDTOPage(Page<User> pagedUsers) {
        List<UserDTO> listUserDTOs = new ArrayList<>();
        pagedUsers.forEach(pagedUser-> listUserDTOs.add(convertUserToUserDTO(pagedUser)));

        return new PageImpl<>(listUserDTOs);
    }
}
