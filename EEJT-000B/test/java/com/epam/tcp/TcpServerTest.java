package com.epam.tcp;

import com.epam.http.HttpServer;
import com.epam.http.HttpServerHandler;
import com.epam.product.ProductRepo;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TcpServerTest {

    ProductRepo productRepoMock = mock(ProductRepo.class);
    Socket socketMock = mock(Socket.class);
    TcpServer testObj = new TcpServer(200, productRepoMock);

    @Test
    void returnThreadOfTcpServerHandlerWhenAllParamCorrect() {
        TcpServerHandler expected = new TcpServerHandler(socketMock, productRepoMock);
        TcpServerHandler actual = testObj.creatThreadServerTcp(socketMock);

        assertEquals(expected.getState(), actual.getState());
    }
}