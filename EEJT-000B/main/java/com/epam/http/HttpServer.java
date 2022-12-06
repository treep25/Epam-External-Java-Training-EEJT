package com.epam.http;


import com.epam.io.ConsoleIO;
import com.epam.product.ProductRepo;
import com.epam.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer extends Server {
    private final int port;
    private final ProductRepo productRepo;
    private boolean run = true;
    private final ConsoleIO consoleIO;

    public HttpServer(int port, ProductRepo productRepo) {
        this.port = port;
        this.productRepo = productRepo;
        this.consoleIO = new ConsoleIO();
    }

    public HttpServerHandler creatThreadServerHttp(Socket socket) {
        return new HttpServerHandler(socket, productRepo);
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (run) {
                Socket socket = serverSocket.accept();
                creatThreadServerHttp(socket).start();
            }
        } catch (IOException ex) {
            consoleIO.print("Server exception" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void end() {
        run = false;
    }

}

