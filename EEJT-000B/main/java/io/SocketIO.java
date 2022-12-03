package io;

import java.io.*;
import java.net.Socket;

public class SocketIO implements Closeable {
    private Socket socket;

    private PrintWriter printWriter;

    private BufferedReader bufferedReader;

    public SocketIO(Socket socket) {
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


    @Override
    public void close() {
        try {
            bufferedReader.close();
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}
