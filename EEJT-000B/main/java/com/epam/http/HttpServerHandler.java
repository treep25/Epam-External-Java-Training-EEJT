package com.epam.http;

import com.epam.http.commands.CommandHttpFactory;
import com.epam.http.commands.NoSuchCommand;
import com.epam.product.ProductRepo;
import com.epam.io.ConsoleIO;
import com.epam.io.SocketIO;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpServerHandler extends Thread {
    private final CommandHttpFactory commandMap;
    private final Socket socket;
    private final ConsoleIO consoleIO = new ConsoleIO();
    private final static Map<String, String> CONTENT_TYPES = Map.of("type", "application/json");

    public HttpServerHandler(Socket socket, ProductRepo productRepo) {
        this.socket = socket;
        commandMap = new CommandHttpFactory(productRepo);
    }

    private void getHeader(SocketIO socketIO, int statusCode, String statusText, String type) {
        socketIO.writef("HTTP/1.1 %s %s%n", statusCode, statusText);
        socketIO.writef("Content-Type: %s%n", type);
        socketIO.write("");
    }

    private void isUnknownCommandBody(String request, JsonResponse response, SocketIO socketIO) {
        if (response.getJson().equals(new MapResponse(Map.of(request, "No such command")).getJson())) {
            getHeader(socketIO, 404, response.getJson(), CONTENT_TYPES.get("type"));
        } else {
            getHeader(socketIO, 200, "Ok", CONTENT_TYPES.get("type"));
        }
    }

    public void run() {
        try (SocketIO socketIO = new SocketIO(socket)) {
            socketIO.initIO();

            String request = socketIO.readLine();

            if (request != null) {
                consoleIO.print("HTTP Client connected HTTP");

                StringTokenizer parse = new StringTokenizer(request);

                String method = parse.nextToken().toUpperCase(Locale.ROOT);


                consoleIO.print("HTTP Method is " + method);
                String requestParsed = parse.nextToken().toLowerCase(Locale.ROOT);
                consoleIO.print("HTTP Request is - " + requestParsed);

                JsonResponse response = commandMap.getCommand(method + requestParsed).execute(requestParsed);
                isUnknownCommandBody(requestParsed, response, socketIO);

                System.out.println("HTTP Server response is " + response.getJson());
                socketIO.write(response.getJson());
                consoleIO.print("HTTP Client connection closed! HTTP");

            }
        }

    }
}
