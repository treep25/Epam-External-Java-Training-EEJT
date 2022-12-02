import http.HttpServer;
import services.Server;
import services.factory.AbstractFactory;
import services.factory.HttpServerFactory;
import services.factory.TcpServerFactory;
import tcp.TcpServer;

public class Starter {
    public static void main(String[] args) {
        Server tcp = new TcpServer(3009);
        AbstractFactory tcpFactory = new TcpServerFactory(tcp);
        Server http = new HttpServer(8080);
        AbstractFactory httpFactory = new HttpServerFactory(http);
        httpFactory.start();
        tcpFactory.start();

    }
}
