package com.epam.esm.user.authComponent;

import com.epam.esm.role.Role;
import com.epam.esm.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class AuthUserVerifyTest {

    @InjectMocks
    private AuthUserVerify authUserVerify;

    @Test
    void hasPermission_ReturnTrue_ForAdmin() {
        User user = User.builder().role(Role.ADMIN).build();
        assertTrue(authUserVerify.hasPermission(user,1L));
    }

    @Test
    void hasPermission_ReturnTrue_ForUser_WhenUserHasThisOrderId() {
        User user = User.builder().id(1L).role(Role.USER).build();
        assertTrue(authUserVerify.hasPermission(user,1L));
    }

    @Test
    void hasPermission_ReturnFalse_ForUser_WhenUserHasThisNoOrderId() {
        User user = User.builder().id(2L).role(Role.USER).build();
        assertFalse(authUserVerify.hasPermission(user,1L));
    }
}