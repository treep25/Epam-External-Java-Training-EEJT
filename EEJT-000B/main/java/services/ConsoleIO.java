package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConsoleIO {
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public ConsoleIO(Socket socket) {
        this.socket = socket;
    }

    public void initIO() {
        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String readLine() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void writeServer(String message) {
        String prefix = "<";
        String postfix = ">";
        printWriter.println(prefix + message + postfix);
    }

    public void write(String message) {
        printWriter.println(message);
    }

    public void closeIO() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        printWriter.close();
    }

    public void print(String message) {
        System.out.println(message);
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}
