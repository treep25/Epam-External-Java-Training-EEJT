package http;

import http.commands.CommandFactory;
import http.commands.NoSuchCommand;
import io.ConsoleIO;
import product.ProductRepo;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpServerHandler extends Thread {
    private final CommandFactory commandMap;
    private ConsoleIO console;
    private final Map<String, String> CONTENT_TYPES = Map.of("type", "application/json");

    public HttpServerHandler(Socket socket, ProductRepo productRepo) {
        console = new ConsoleIO(socket);
        commandMap = new CommandFactory(productRepo);
    }

    private void isUnknownCommandBody(JsonResponse response, PrintWriter printWriter) {
        if (response.getJson().equals(new MapResponse(Map.of("message", "No such command")).getJson())) {
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

        console.print("HTTP Client connected HTTP");
        console.initIO();

        String request = console.readLine();
        if (request != null) {

            StringTokenizer parse = new StringTokenizer(request);

            String method = parse.nextToken().toUpperCase(Locale.ROOT);
            console.print("HTTP Method is " + method);
            String requestParsed = parse.nextToken().toLowerCase(Locale.ROOT);
            console.print("HTTP Request is - " + requestParsed);

            JsonResponse response = commandMap.getCommandsMap(requestParsed).getOrDefault(method + requestParsed, new NoSuchCommand()).execute();
            isUnknownCommandBody(response, console.getPrintWriter());

            System.out.println("HTTP Server response is " + response.getJson());
            console.write(response.getJson());

        } else {
            console.print("HTTP Client connection closed! HTTP");
            console.closeIO();
            console.closeSocket();
        }


    }
}
