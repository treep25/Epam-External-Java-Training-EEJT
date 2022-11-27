import entity.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {
    private final Socket socket;
    private final List<Product> productList;

    public ServerThread(Socket socket, List<Product> productList) {
        this.socket = socket;
        this.productList = productList;
    }

    public void run() {
        try {
            System.out.println("Client is connected");

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            printWriter.println("(get count) - To count amount of products");
            printWriter.println("(get item = number of product(id))- To see information");
            while (true) {
                String request = bufferedReader.readLine();

                System.out.println("Client request is " + request);

                if (request.equals("get count")) {
                    CountAmountOfProductsCommand command = new CountAmountOfProductsCommand(productList);
                    command.execute();
                    System.out.println("Server response is " + command.getCountProducts());
                    printWriter.println(command.getCountProducts());
                } else if (request.matches("get item = \\d")) {
                    request = request.replaceAll("\\D", "");
                    GetItemByNumber command = new GetItemByNumber(productList, request);
                    command.execute();
                    System.out.println("Server response is " + command.getResponse());
                    printWriter.println(command.getResponse());
                } else if (request.equals("")) {
                    System.out.println("Client is disconnected");
                    break;
                } else {
                    System.out.println("Incorrect input");
                }
            }
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
