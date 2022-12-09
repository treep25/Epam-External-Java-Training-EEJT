package com.epam.http;

import com.epam.Command;
import com.epam.http.commands.CommandHttpFactory;
import com.epam.http.commands.CountAmountOfProductsHTTPCommand;
import com.epam.io.SocketIO;
import com.epam.product.ProductRepo;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HttpServerHandlerTest {

    ProductRepo productRepoMock = mock(ProductRepo.class);
    Socket socketMock = mock(Socket.class);
    HttpServerHandler testObj = new HttpServerHandler(socketMock, productRepoMock);
    SocketIO socketIOMock = mock(SocketIO.class);
    CommandHttpFactory commandHttpFactoryMock = mock(CommandHttpFactory.class);

    @Test
    void run() {
        //given
        String requestPlug = "/shop/count";
        String methodPlug = "GEt";
        Command<JsonResponse> commandMock = new CountAmountOfProductsHTTPCommand(productRepoMock);
        JsonResponse expected = new MapResponse(Map.of("count", "10"));
        //when
        when(socketIOMock.readLine()).thenReturn(requestPlug);
        when(productRepoMock.getSize()).thenReturn(10);
        when(commandHttpFactoryMock.getCommand(methodPlug + requestPlug)).thenReturn(commandMock);

        //then
        JsonResponse actual = commandMock.execute(requestPlug);
        assertEquals(expected.getJson(), actual.getJson());
    }
}