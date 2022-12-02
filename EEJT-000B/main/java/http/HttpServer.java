package http;

import com.fasterxml.jackson.databind.ObjectMapper;
import http.commands.CountAmountOfProductsHTTPCommand;
import http.commands.GetItemByNumberHTTPCommand;
//import http.commands.InsertProductToMapHTTPCommand;
import services.Command;
import services.ConsoleIO;
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
        CONTENT_TYPES.put("type", "application/json");
        if (method.equals("GET")) {
            commandMap.put(req -> request.equals("/shop/count"), new CountAmountOfProductsHTTPCommand());
            commandMap.put(req -> request.contains("/shop/item?get_info="), new GetItemByNumberHTTPCommand(request.replaceAll("\\D", "")));
            commandMap.put(req -> !request.contains("/shop/count") && !request.contains("/shop/item?get_info="), new UnknownCommand());
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
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ConsoleIO console = new ConsoleIO(socket);
                console.initIO();

                console.print("HTTP Client connected HTTP");

                String request = console.readLine();
                if (request == null) {
                    break;
                }

                StringTokenizer parse = new StringTokenizer(request);
                ObjectMapper mapper = new ObjectMapper();

                String method = parse.nextToken().toUpperCase(Locale.ROOT);
                console.print("HTTP Method is " + method);
                String requestParsed = parse.nextToken().toLowerCase(Locale.ROOT);
                console.print("HTTP Request is - " + requestParsed);

                commandMap = new HashMap<>();
                mapInitCommands(requestParsed, method);
                for (Map.Entry<Predicate<String>, Command> entry : commandMap.entrySet()) {
                    if (entry.getKey().test(requestParsed)) {
                        Object response = entry.getValue().execute();
                        isUnknownCommandBody(response, console.getPrintWriter());

                        System.out.println("HTTP Server response is " + mapper.writeValueAsString(response));
                        console.write(mapper.writeValueAsString(response));
                    }
                }
                console.print("HTTP Client connection closed! HTTP");
                console.closeIO();
                console.closeSocket();
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("HTTP Server exception: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
