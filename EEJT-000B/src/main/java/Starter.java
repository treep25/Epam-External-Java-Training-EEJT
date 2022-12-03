import http.HttpServer;
import http.HttpServerHandler;
import product.ProductRepo;
import services.Server;
import services.AbstractFactory;
import http.HttpServerFactory;
import tcp.TcpServerFactory;
import tcp.TcpServer;

public class Starter {
    public static void main(String[] args) {
        //TODO Remake abstract factory
        ProductRepo productRepo = new ProductRepo();
        Server tcp = new TcpServer(3009);
        AbstractFactory tcpFactory = new TcpServerFactory(tcp);
        Server http = new HttpServer(8080, productRepo);
        AbstractFactory httpFactory = new HttpServerFactory(http);
        httpFactory.start();
        tcpFactory.start();

    }
}
