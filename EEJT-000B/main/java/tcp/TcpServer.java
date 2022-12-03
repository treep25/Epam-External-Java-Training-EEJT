package tcp;

import product.ProductRepo;
import services.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer extends Server {
    private final int port;
    private final ProductRepo productRepo;
    private boolean run = true;

    public TcpServer(int port, ProductRepo productRepo) {
        this.port = port;
        this.productRepo = productRepo;
    }


    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (run) {
                Socket socket = serverSocket.accept();
                new TcpServerHandler(socket, productRepo).start();
            }
        } catch (IOException ex) {
            System.out.println("services.Server exception" + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public void end() {
        run = false;
    }

}
