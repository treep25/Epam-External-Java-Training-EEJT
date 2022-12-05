package com.epam.tcp.commands;

import com.epam.Command;
import com.epam.http.JsonResponse;
import com.epam.http.commands.CommandHttpFactory;
import com.epam.product.ProductRepo;
import com.epam.tcp.Response;
import com.epam.tcp.StringResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandTcpFactoryTest {
    @Mock
    private ProductRepo productRepoMock = mock(ProductRepo.class);

    @Test
    void getCommand_CountAmountOfProducts_ReturnNotNullTest() {
        Command<StringResponse> expected = new CountAmountOfProductsTCPCommand(productRepoMock);
        Command<StringResponse> actual = new CommandTcpFactory(productRepoMock).getCommand("<get count>");

        assertEquals(expected, actual);
    }

    @Test
    void getCommand_GetItemById_ReturnNotNullTest() {
        Command<StringResponse> expected = new GetItemByNumberTCPCommand(productRepoMock);
        Command<StringResponse> actual = new CommandTcpFactory(productRepoMock).getCommand("<get item = >");

        assertEquals(expected, actual);
    }

    @Test
    void getUnknownCommandReturnNotNullTest() {
        Command<StringResponse> expectedNotNull = new CommandTcpFactory(productRepoMock).getCommand("asdaaaadsaasdadssda");

        assertNotNull(expectedNotNull);

    }
}