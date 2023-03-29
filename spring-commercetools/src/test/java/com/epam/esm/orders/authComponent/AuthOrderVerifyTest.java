package com.epam.esm.orders.authComponent;

import com.epam.esm.orders.model.Order;
import com.epam.esm.role.Role;
import com.epam.esm.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class AuthOrderVerifyTest {

    @InjectMocks
    private AuthOrderVerify authOrderVerify;


    @Test
    void hasPermission_ReturnTrue_ForAdmin() {
        User user = User.builder().role(Role.ADMIN).build();
        assertTrue(authOrderVerify.hasPermission(user,1L));
    }

    @Test
    void hasPermission_ReturnTrue_ForUser_WhenUserHasThisOrderId() {
        User user = User.builder().role(Role.USER).orders(Set.of(Order.builder().id(1L).build())).build();
        assertTrue(authOrderVerify.hasPermission(user,1L));
    }

    @Test
    void hasPermission_ReturnFalse_ForUser_WhenUserHasThisNoOrderId() {
        User user = User.builder().role(Role.USER).build();
        assertFalse(authOrderVerify.hasPermission(user,1L));
    }
}