package com.epam.http;

import com.epam.product.ProductRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class HttpServerTest {

    ProductRepo productRepoMock = mock(ProductRepo.class);
    Socket socketMock = mock(Socket.class);
    HttpServer testObj = new HttpServer(200, productRepoMock);

    @Test
    void returnThreadOfHttpServerHandlerWhenAllParamCorrect() {
        HttpServerHandler expected = new HttpServerHandler(socketMock, productRepoMock);
        HttpServerHandler actual = testObj.creatThreadServerHttp(socketMock);

        assertEquals(expected.getState(), actual.getState());

    }
}