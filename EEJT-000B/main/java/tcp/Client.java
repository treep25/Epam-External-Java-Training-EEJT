package tcp;

import services.ConsoleIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3009)) {
            ConsoleIO console = new ConsoleIO(socket);
            console.initIO();
            Scanner scanner = new Scanner(System.in);

            console.print(console.readLine());
            console.print(console.readLine());

            while (true) {
                String request = scanner.nextLine();

                if (request.equals("")) {
                    console.write(request);
                    break;
                }
                console.write(request);
                console.print(console.readLine());
            }
            console.closeIO();
        } catch (IOException ex) {
            System.out.println("Client exception " + ex.getMessage());
            throw new RuntimeException(ex);
        }

    }


}
