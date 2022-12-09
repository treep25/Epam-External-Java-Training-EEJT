package com.epam.tcp.commands;

import com.epam.Command;
import com.epam.http.JsonResponse;
import com.epam.http.MapResponse;
import com.epam.http.commands.CountAmountOfProductsHTTPCommand;
import com.epam.product.ProductRepo;
import com.epam.tcp.Response;
import com.epam.tcp.StringResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountAmountOfProductsTCPCommandTest {

    private final ProductRepo productRepoMock = mock(ProductRepo.class);
    private final Command<StringResponse> testObj = new CountAmountOfProductsTCPCommand(productRepoMock);

    @Test
    void shouldReturnAmountOfProductsTestExecute() {
        //given
        StringResponse expectedObj = new Response("34");
        String commandPlug = "<get count>";

        //when
        when(productRepoMock.getSize()).thenReturn(34);
        StringResponse actual = testObj.execute(commandPlug);

        //then
        assertEquals(expectedObj.getString(), actual.getString());
    }
}