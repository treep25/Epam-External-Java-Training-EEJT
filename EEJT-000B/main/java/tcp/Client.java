package tcp;

import io.ConsoleIO;
import io.SocketIO;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ConsoleIO consoleIO = new ConsoleIO();
        try (Socket socket = new Socket("localhost", 3009);
             SocketIO socketIO = new SocketIO(socket);) {

            socketIO.initIO();
            Scanner scanner = new Scanner(System.in);

            consoleIO.print(socketIO.readLine());
            consoleIO.print(socketIO.readLine());

            while (true) {
                String request = scanner.nextLine();
                if (!request.equals("")) {
                    if (request.contains("<") && request.contains(">")) {
                        socketIO.write(request);
                        consoleIO.print(socketIO.readLine());
                    } else {
                        consoleIO.print("<services.Command should contains (< >)>");
                    }
                } else {
                    socketIO.write(request);
                    break;
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }

    }


}
