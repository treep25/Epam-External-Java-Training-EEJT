package com.epam.tcp;

import com.epam.io.ConsoleIO;
import com.epam.io.SocketIO;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ConsoleIO consoleIO = new ConsoleIO();
        try (Socket socket = new Socket("localhost", 3009);
             SocketIO socketIO = new SocketIO(socket);) {

            socketIO.initIO();

            consoleIO.print(socketIO.readLine());
            consoleIO.print(socketIO.readLine());

            while (true) {
                String request = consoleIO.scanner();
                if (!request.equals("")) {
                    if (request.contains("<") && request.contains(">")) {
                        socketIO.write(request);
                        consoleIO.print(socketIO.readLine());
                    } else {
                        consoleIO.print("<Command should contains (< >)>");
                    }
                } else {
                    socketIO.write(request);
                    break;
                }

            }
        } catch (IOException ex) {
            consoleIO.print(ex.getMessage());
            throw new RuntimeException(ex);
        }

    }


}
