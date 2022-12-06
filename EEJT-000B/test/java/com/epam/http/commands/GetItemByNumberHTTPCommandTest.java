package com.epam.http.commands;

import com.epam.Command;
import com.epam.http.JsonResponse;
import com.epam.http.MapResponse;
import com.epam.http.commands.CommandHttpFactory;
import com.epam.product.Product;
import org.junit.jupiter.api.Test;
import com.epam.product.ProductRepo;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetItemByNumberHTTPCommandTest {
    private final ProductRepo productRepo = mock(ProductRepo.class);
    private final Command<JsonResponse> testObj = new GetItemByNumberHTTPCommand(productRepo);

    @Test
    void shouldReturnItemWhen_IdIsFit_TestExecute() {
        //given
        Product expectedProduct = new Product("NameStub", 1L);
        JsonResponse expected = new MapResponse(
                new LinkedHashMap<>() {
                    {
                        put("name", expectedProduct.name());
                        put("price", expectedProduct.price().toString());
                    }
                }
        );
        String requestStub = "/shop/item?get_info=5";

        //when
        when(productRepo.getSize()).thenReturn(10);
        when(productRepo.getItem(5L)).thenReturn(expectedProduct);
        JsonResponse actual = testObj.execute(requestStub);

        //then
        assertEquals(expected.getJson(), actual.getJson());

    }

    @Test
    void shouldReturnMapOfIllInputWhen_IdIsNotFitAndBlank_TestExecute() {
        //given
        JsonResponse expected = new MapResponse(
                Map.of("Oops", "We have not such product"));
        String requestStub = "/shop/item?get_info=";

        //when

        JsonResponse actual = testObj.execute(requestStub);

        //then
        assertEquals(expected.getJson(), actual.getJson());

    }

    @Test
    void shouldReturnMapOfIllInputWhen_IdIsNotFitAndLessZeroCase_TestExecute() {
        //given
        JsonResponse expected = new MapResponse(
                Map.of("Oops", "We have not such product"));
        String requestStub = "/shop/item?get_info=-1";

        //when

        JsonResponse actual = testObj.execute(requestStub);

        //then
        assertEquals(expected.getJson(), actual.getJson());
    }

    @Test
    void shouldReturnMapOfIllInputWhen_IdIsNotFitAndMoreTheAmountOfProducts_TestExecute() {
        JsonResponse expected = new MapResponse(
                Map.of("Oops", "We have not such product"));
        String requestStub = "/shop/item?get_info=2000";

        //when
        when(productRepo.getSize()).thenReturn(2000 / 2);
        JsonResponse actual = testObj.execute(requestStub);

        //then
        assertEquals(expected.getJson(), actual.getJson());
    }

}