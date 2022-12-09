package com.epam.tcp;

import com.epam.io.ConsoleIO;
import com.epam.product.ProductRepo;
import com.epam.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Server {
    private final int port;
    private final ProductRepo productRepo;
    private boolean run = true;
    private final ConsoleIO consoleIO;

    public TcpServer(int port, ProductRepo productRepo) {
        this.port = port;
        this.productRepo = productRepo;
        consoleIO = new ConsoleIO();
    }

    public TcpServerHandler creatThreadServerTcp(Socket socket) {
        return new TcpServerHandler(socket, productRepo);
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (run) {
                Socket socket = serverSocket.accept();
                creatThreadServerTcp(socket).start();
            }
        } catch (IOException ex) {
            consoleIO.print("Server exception" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public void end() {
        run = false;
    }

}
