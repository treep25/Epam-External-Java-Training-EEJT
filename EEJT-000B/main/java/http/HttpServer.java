package http;


import product.ProductRepo;
import services.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer extends Server {
    private final int port;
    private final ProductRepo productRepo;
    private boolean run = true;

    public HttpServer(int port, ProductRepo productRepo) {
        this.port = port;
        this.productRepo = productRepo;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (run) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(120000);
                new HttpServerHandler(socket, productRepo).start();
            }
        } catch (IOException ex) {
            System.out.println("services.Server exception" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void end() {
        run = false;
    }

}

