package com.epam.http.commands;

import com.epam.Command;
import com.epam.http.JsonResponse;
import com.epam.http.commands.CommandHttpFactory;
import com.epam.tcp.StringResponse;
import com.epam.tcp.commands.CommandTcpFactory;
import com.epam.tcp.commands.CountAmountOfProductsTCPCommand;
import com.epam.tcp.commands.GetItemByNumberTCPCommand;
import org.junit.jupiter.api.Test;
import com.epam.product.ProductRepo;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandHttpFactoryTest {
    @Mock
    private ProductRepo productRepoMock = mock(ProductRepo.class);

    @Test
    void getCommand_CountAmountOfProducts_ReturnNotNullTest() {
        Command<JsonResponse> expected = new CountAmountOfProductsHTTPCommand(productRepoMock);
        Command<JsonResponse> actual = new CommandHttpFactory(productRepoMock).getCommand("GET/shop/count");

        assertEquals(expected, actual);
    }

    @Test
    void getUnknownCommandReturnNotNullTest() {
        Command<JsonResponse> expectedNotNull = new CommandHttpFactory(productRepoMock).getCommand("asdaaaadsaasdadssda");
        assertNotNull(expectedNotNull);

    }
}