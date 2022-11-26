package TCP;

import entity.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private static List<Product> getSomeProducts() {
        return List.of(new Product(1L, "Weed chair", 1200L),
                new Product(2L, "Table", 300L),
                new Product(3L, "Laptop", 3300L),
                new Product(4L, "Notebook", 2500L),
                new Product(5L, "T-shirt", 12L),
                new Product(6L, "NoteBook bag ", 300L),
                new Product(7L, "Camp chair", 235L),
                new Product(8L, "Door", 900L),
                new Product(9L, "Iphone", 3000L),
                new Product(10L, "Bluetooth micro", 12000L));
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3000);
        Socket socket = serverSocket.accept();

        List<Product> productList = getSomeProducts();

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        printWriter.println("Hello, you can count products or get by id these ones in our shop");
        printWriter.println("Whats your name ");
        printWriter.flush();

        String name = bufferedReader.readLine();

        while (true) {
            printWriter.println(name + "\n" + "(get count) - count products \n (get item = id) - get products by ID \n" + "0 - exit");
            printWriter.flush();
            String option = bufferedReader.readLine();
            if (option.equals("get count")) {
                printWriter.println(productList.size());
                printWriter.flush();
            } else if (option.equals("2")) {
                for (Product product : productList) {
                    if (product.getId().equals(Long.parseLong(option))) {
                        printWriter.println(product.getId() + "|" + product.getName() + "|" + product.getPrice());
                        printWriter.flush();
                    }
                }
            } else if (option.equals("0")) {
                break;
            }
        }

    }
}
