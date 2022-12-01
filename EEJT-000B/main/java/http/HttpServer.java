package http;

import com.fasterxml.jackson.databind.ObjectMapper;
import http.commands.CountAmountOfProductsHTTPCommand;
import http.commands.GetItemByNumberHTTPCommand;
//import http.commands.InsertProductToMapHTTPCommand;
import services.Command;
import services.Server;
import tcp.commands.UnknownCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Predicate;

public class HttpServer extends Thread implements Server {
    private final int port;
    private Map<Predicate<String>, Command> commandMap = new HashMap<>();
    private final Map<String, String> CONTENT_TYPES = new HashMap<>();

    public HttpServer(int port) {
        this.port = port;
    }

    private void mapInitCommands(String request, String method) {
        if (method.equals("GET")) {
            commandMap.put(req -> {
                CONTENT_TYPES.put("type", "application/json");
                return request.equals("/shop/count");
            }, new CountAmountOfProductsHTTPCommand());
            commandMap.put(req -> {
                CONTENT_TYPES.put("type", "application/json");
                return request.contains("/shop/item?get_info=");
            }, new GetItemByNumberHTTPCommand(request.replaceAll("\\D", "")));
            commandMap.put(req -> {
                CONTENT_TYPES.put("type", "text/plain");
                return !request.contains("/shop/count") && !request.contains("/shop/item?get_info=");
            }, new UnknownCommand());
        } else if (method.equals("POST")) {
            //TODO mb insert methods
        }
    }

    private void isUnknownCommandBody(Object response, PrintWriter printWriter) {
        if (response.equals("Unknown command")) {
            getHeader(printWriter, 404, response.toString(), CONTENT_TYPES.get("type"));
        } else {
            getHeader(printWriter, 200, "Ok", CONTENT_TYPES.get("type"));
        }
    }

    private void getHeader(PrintWriter printWriter, int statusCode, String statusText, String type) {
        printWriter.printf("HTTP/1.1 %s %s%n", statusCode, statusText);
        printWriter.printf("Content-Type: %s%n", type);
        printWriter.println();
    }

    public void run() {
        Socket socket = null;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                socket = serverSocket.accept();
                System.out.println("HTTP Client connected HTTP");
                try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    String request = bufferedReader.readLine();
                    if (request == null) {
                        break;
                    }

                    StringTokenizer parse = new StringTokenizer(request);
                    ObjectMapper mapper = new ObjectMapper();

                    String method = parse.nextToken().toUpperCase(Locale.ROOT);
                    System.out.println("HTTP Method is " + method);
                    String requestParsed = parse.nextToken().toLowerCase(Locale.ROOT);
                    System.out.println("HTTP Request is - " + requestParsed);

                    commandMap = new HashMap<>();
                    mapInitCommands(requestParsed, method);

                    for (Map.Entry<Predicate<String>, Command> entry : commandMap.entrySet()) {
                        if (entry.getKey().test(requestParsed)) {
                            Object response = entry.getValue().execute();
                            isUnknownCommandBody(response, printWriter);

                            System.out.println("HTTP Server response is " + mapper.writeValueAsString(response));
                            printWriter.println(mapper.writeValueAsString(response));
                        }
                    }
                }

            }
            socket.close();
            System.out.println("HTTP Client connection closed! HTTP");
        } catch (IOException e) {
            System.err.println("HTTP services.Server exception: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
