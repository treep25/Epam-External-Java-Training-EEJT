package http;

import http.commands.CommandHttpFactory;
import http.commands.NoSuchCommand;
import io.ConsoleIO;
import io.SocketIO;
import product.ProductRepo;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpServerHandler extends Thread {
    private final CommandHttpFactory commandMap;
    private final Socket socket;
    private final static Map<String, String> CONTENT_TYPES = Map.of("type", "application/json");

    public HttpServerHandler(Socket socket, ProductRepo productRepo) {
        this.socket = socket;
        commandMap = new CommandHttpFactory(productRepo);
    }

    private void isUnknownCommandBody(JsonResponse response, PrintWriter printWriter) {
        if (response.getJson().equals(new MapResponse(Map.of("message", "No such command")).getJson())) {
            getHeader(printWriter, 404, response.getJson(), CONTENT_TYPES.get("type"));
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
        ConsoleIO console = new ConsoleIO();
        try (SocketIO socketIO = new SocketIO(socket)) {
            socketIO.initIO();

            String request = socketIO.readLine();

            if (request != null) {
                console.print("HTTP Client connected HTTP");

                StringTokenizer parse = new StringTokenizer(request);

                String method = parse.nextToken().toUpperCase(Locale.ROOT);


                console.print("HTTP Method is " + method);
                String requestParsed = parse.nextToken().toLowerCase(Locale.ROOT);
                console.print("HTTP Request is - " + requestParsed);

                JsonResponse response = commandMap.getCommandsMap(requestParsed).getOrDefault(method + requestParsed, new NoSuchCommand()).execute();
                isUnknownCommandBody(response, socketIO.getPrintWriter());

                System.out.println("HTTP services.Server response is " + response.getJson());
                socketIO.write(response.getJson());
                console.print("HTTP Client connection closed! HTTP");

            }
        }

    }
}
