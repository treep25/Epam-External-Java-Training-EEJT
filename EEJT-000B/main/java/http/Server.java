package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.err.println("Server is running on port: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.err.println("Client connected");

                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    System.out.println(s);
                    if (s.isEmpty()) {
                        break;
                    }
                }
                printWriter.println("HTTP/1.1 200 OK\n");
                printWriter.println("\n");
                printWriter.println("<b>Welcome To Asim Code!</b>");
                printWriter.println("\n\n");
                printWriter.println("\r\n\r\n");

                System.err.println("Client connection closed!");
                socket.close();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
