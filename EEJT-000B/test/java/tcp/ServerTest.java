package tcp;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerTest {
    ServerSocket serverSocket = mock(ServerSocket.class);
    Socket socket = mock(Socket.class);

    @Test
    public void TestServerWork() throws IOException {
        when(serverSocket.accept()).thenReturn(socket);
        ServerThread serverThread = new ServerThread(socket);

    }
}