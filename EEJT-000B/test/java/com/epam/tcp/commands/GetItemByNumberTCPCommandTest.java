package com.epam.tcp.commands;

import com.epam.Command;
import com.epam.http.JsonResponse;
import com.epam.http.MapResponse;
import com.epam.http.commands.GetItemByNumberHTTPCommand;
import com.epam.product.Product;
import com.epam.product.ProductRepo;
import com.epam.tcp.Response;
import com.epam.tcp.StringResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetItemByNumberTCPCommandTest {
    private final ProductRepo productRepo = mock(ProductRepo.class);
    private final Command<StringResponse> testObj = new GetItemByNumberTCPCommand(productRepo);

    @Test
    void shouldReturnItemWhen_IdIsFit_TestExecute() {
        //given
        Product expectedProduct = new Product("NameStub", 1L);
        StringResponse expected = new Response(expectedProduct.name() + "|" + expectedProduct.price());
        String requestStub = "<get item = 1>";

        //when
        when(productRepo.getSize()).thenReturn(10);
        when(productRepo.getItem(1L)).thenReturn(expectedProduct);
        StringResponse actual = testObj.execute(requestStub);

        //then
        assertEquals(expected.getString(), actual.getString());

    }

    @Test
    void shouldReturnIllInputWhen_IdIsNotFitAndBlank_TestExecute() {
        //given
        StringResponse expected = new Response("We have not got such item in our shop");
        String requestStub = "<get item = >";

        //when
        StringResponse actual = testObj.execute(requestStub);

        //then
        assertEquals(expected.getString(), actual.getString());

    }

    @Test
    void shouldReturnIllInputWhen_IdIsNotFitAndLessZeroCase_TestExecute() {
        //given
        StringResponse expected = new Response("We have not got such item in our shop");
        String requestStub = "<get item = -1>";


        //when
        StringResponse actual = testObj.execute(requestStub);

        //then
        assertEquals(expected.getString(), actual.getString());
    }

    @Test
    void shouldReturnIllInputWhen_IdIsNotFitAndMoreTheAmountOfProducts_TestExecute() {
        //given
        StringResponse expected = new Response("We have not got such item in our shop");
        String requestStub = "<get item = 123141>";

        //when
        when(productRepo.getSize()).thenReturn(245);
        StringResponse actual = testObj.execute(requestStub);

        //then
        assertEquals(expected.getString(), actual.getString());
    }
}