package tcp;

import services.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer implements Server {
    private int port;

    public TcpServer(int port) {
        this.port = port;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new TcpServerHandler(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

}
