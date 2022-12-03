import http.HttpServerFactory;
import product.ProductRepo;
import services.AbstractFactory;
import services.Server;
import tcp.TcpServerFactory;

public class Starter {
    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();

        AbstractFactory tcp = new TcpServerFactory();
        AbstractFactory http = new HttpServerFactory();

        Server tcpServer = tcp.getServer(3009, productRepo);
        Server httpServer = http.getServer(8080, productRepo);

        tcpServer.start();
        httpServer.start();
    }
}
