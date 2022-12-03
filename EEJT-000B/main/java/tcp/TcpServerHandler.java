package tcp;

import io.ConsoleIO;
import io.SocketIO;
import product.ProductRepo;
import tcp.commands.CommandTcpFactory;
import tcp.commands.NoSuchCommand;

import java.net.Socket;

public class TcpServerHandler extends Thread {
    private ProductRepo productRepo;
    private Socket socket;

    public TcpServerHandler(Socket socket, ProductRepo productRepo) {
        this.socket = socket;
        this.productRepo = productRepo;
    }

    @Override
    public void run() {
        ConsoleIO console = new ConsoleIO();
        try (SocketIO socketIO = new SocketIO(socket)) {
            socketIO.initIO();

            console.print("TCP Client is connected TCP");
            socketIO.writeServer("(get count) - To count amount of products");
            socketIO.writeServer("(get item = number of product(id))- To see information");

            while (true) {
                String request = socketIO.readLine();

                if (!request.equals("")) {

                    console.print("TCP Client request is " + request);
                    StringResponse response = new CommandTcpFactory(productRepo).getCommandsMap(request).getOrDefault(request, new NoSuchCommand()).execute();
                    console.print("TCP services.Server response is " + response.getString());
                    socketIO.writeServer(response.getString());
                } else {
                    console.print("TCP Client is disconnected TCP");
                    break;
                }
            }
        }
    }
}
