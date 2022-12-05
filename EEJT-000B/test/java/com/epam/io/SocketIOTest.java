package com.epam.io;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SocketIOTest {

    Socket socket = mock(Socket.class);
    SocketIO socketIO = new SocketIO(socket);

    @Before
    public void init() throws IOException {

    }
//    @Test
//    void readLine() throws IOException  {
//        InputStream inputStreamMock = mock(InputStream.class);
//        OutputStream outputStreamMock = mock(OutputStream.class);
//
//        when(socket.getOutputStream()).thenReturn(outputStreamMock);
//        when(socket.getInputStream()).thenReturn(inputStreamMock);
//
//        socketIO.initIO();
//
//        when(inputStreamMock.read(new byte[]{1,2,3,4})).thenReturn(1);
//        assertEquals(socketIO.readLine(),"1");
//    }

    @Test
    void writeServer() {
    }

    @Test
    void writef() {
    }

    @Test
    void testWritef() {
    }

    @Test
    void write() {
    }
}