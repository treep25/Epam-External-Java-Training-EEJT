package com.epam.tcp.commands;

import com.epam.Command;
import com.epam.http.JsonResponse;
import com.epam.http.MapResponse;
import com.epam.product.ProductRepo;
import com.epam.tcp.Response;
import com.epam.tcp.StringResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class NoSuchCommandTest {

    private final ProductRepo productRepoMock = mock(ProductRepo.class);
    private final Command<StringResponse> testObj = new NoSuchCommand();

    @Test
    void shouldReturnMapOfNoSuchCommandsWhenNothingWasFittedTestExecute() {
        //given
        String requestStub = "<asdfghj>";
        StringResponse expected = new Response(requestStub + " is Unknown command");

        StringResponse actual = testObj.execute(requestStub);

        //then
        assertEquals(expected.getString(), actual.getString());
    }
}