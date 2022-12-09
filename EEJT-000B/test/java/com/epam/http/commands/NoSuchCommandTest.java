package com.epam.http.commands;

import com.epam.Command;
import com.epam.http.JsonResponse;
import com.epam.http.MapResponse;
import com.epam.product.ProductRepo;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class NoSuchCommandTest {

    private final ProductRepo productRepoMock = mock(ProductRepo.class);
    private final Command<JsonResponse> testObj = new NoSuchCommand();

    @Test
    void shouldReturnMapOfNoSuchCommandsWhenNothingWasFittedTestExecute() {
        //given
        String requestStub = "/shop/itemksvdkdskkksdfakdkdsksdkdsaadfasdf";
        JsonResponse expected = new MapResponse(
                Map.of(requestStub, "No such command"));
        JsonResponse actual = testObj.execute(requestStub);

        //then
        assertEquals(expected.getJson(), actual.getJson());
    }
}