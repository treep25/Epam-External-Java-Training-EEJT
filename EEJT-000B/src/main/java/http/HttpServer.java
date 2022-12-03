package http;

import product.ProductRepo;
import services.Server;
import tcp.TcpServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Server {
    private final int port;
    private final ProductRepo productRepo;

    public HttpServer(int port, ProductRepo productRepo) {
        this.port = port;
        this.productRepo = productRepo;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(120000);
                new HttpServerHandler(socket, productRepo).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
