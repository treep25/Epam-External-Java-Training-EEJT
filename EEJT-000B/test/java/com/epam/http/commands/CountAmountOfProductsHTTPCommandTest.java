package com.epam.http.commands;

import com.epam.Command;
import com.epam.http.JsonResponse;
import com.epam.http.MapResponse;
import org.junit.jupiter.api.Test;
import com.epam.product.ProductRepo;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountAmountOfProductsHTTPCommandTest {

    private final ProductRepo productRepoMock = mock(ProductRepo.class);
    private final Command<JsonResponse> testObj = new CountAmountOfProductsHTTPCommand(productRepoMock);


    @Test
    void shouldReturnAmountOfProductsTestExecute() {
        //given
        JsonResponse expectedObj = new MapResponse(Map.of("count", "4"));
        String commandPlug = "GET/shop/count";

        //when
        when(productRepoMock.getSize()).thenReturn(4);
        JsonResponse actual = testObj.execute(commandPlug);
        //then
        assertEquals(expectedObj.getJson(), actual.getJson());
    }

}