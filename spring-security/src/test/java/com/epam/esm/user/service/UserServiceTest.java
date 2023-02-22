package com.epam.esm.user.service;

import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.orders.model.Order;
import com.epam.esm.orders.repository.OrderRepository;
import com.epam.esm.orders.service.OrderService;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsersTest() {
        //given
        Page<User> allUsersExpected = new PageImpl<>(List.of(new User(), new User()));
        PageRequest pageRequest = PageRequest.of(0, 5);
        when(userRepository.findAll(pageRequest)).thenReturn(allUsersExpected);

        //when
        Page<User> allOrdersActual = userService.getAllUsers(0, 5);

        //then
        assertEquals(allUsersExpected, allOrdersActual);
    }

    @Test
    void getByIdTest_ReturnUserById() {
        //given
        User expectedUser = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        //when
        User usersByIdActual = userService.getById(1L);

        //then
        assertEquals(expectedUser, usersByIdActual);
    }

    @Test
    void getByIdTest_ReturnItemNotFoundException_WhenThereAreNoById() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class,
                () -> userService.getById(1L));

        //then
        assertEquals("there are no users with (id = " + 1 + ")", thrown.getMessage());
    }

    @Test
    void updateUserOrder() {
        //given
        User userExpected = new User();
        when(userRepository.save(userExpected)).thenReturn(userExpected);

        //when
        userService.updateUserOrder(userExpected);

        //then
        verify(userRepository,times(1)).save(userExpected);
    }
}