package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3009)) {
            Scanner scanner = new Scanner(System.in);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println(bufferedReader.readLine());
            System.out.println(bufferedReader.readLine());
            while (true) {
                String request = scanner.nextLine();

                if (request.equals("")) {
                    printWriter.println(request);
                    break;
                }
                printWriter.println(request);
                System.out.println(bufferedReader.readLine());
            }

        } catch (IOException ex) {
            System.out.println("tcp.Client exception " + ex.getMessage());
            throw new RuntimeException(ex);
        }

    }


}
