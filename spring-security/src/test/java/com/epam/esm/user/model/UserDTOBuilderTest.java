package com.epam.esm.user.model;

import com.epam.esm.user.authComponent.AuthUserComponent;
import com.epam.esm.user.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class UserDTOBuilderTest {

    @InjectMocks
    private UserDTOBuilder userDTOBuilder;

    @Test
    void convertUserToUserDTO() {
        User before = User.builder().id(1L).build();

        assertNotNull(userDTOBuilder.convertUserToUserDTO(before));

    }

    @Test
    void convertUserPageToUserDTOPage() {
        User before = User.builder().id(1L).build();
        Page<User> pagedUsers = new PageImpl<>(List.of(before));

        assertNotNull(userDTOBuilder.convertUserPageToUserDTOPage(pagedUsers));
    }
}