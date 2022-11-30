package tcp;

import tcp.commands.Command;
import tcp.commands.CountAmountOfProductsCommand;
import tcp.commands.GetItemByNumber;
import tcp.commands.UnknownCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class ServerThread extends Thread {
    private final Socket socket;
    private Map<Predicate<String>, Command> commandMap;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    void mapInitCommands(String request) {
        commandMap.put(req -> Objects.equals(request, "get count"), new CountAmountOfProductsCommand());
        commandMap.put(req -> request.matches("get item = \\d"), new GetItemByNumber(request.replaceAll("\\D", "")));
        commandMap.put(req -> !Objects.equals(request, "get count") && !request.matches("get item = \\d"), new UnknownCommand());
    }

    public void run() {
        try {
            System.err.println("Client is connected");
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            printWriter.println("(get count) - To count amount of products");
            printWriter.println("(get item = number of product(id))- To see information");

            while (true) {
                String request = bufferedReader.readLine();

                if (!request.equals("")) {
                    commandMap = new HashMap<>();

                    System.out.println("tcp.Client request is " + request);
                    mapInitCommands(request);

                    for (Map.Entry<Predicate<String>, Command> entry : commandMap.entrySet()) {
                        if (entry.getKey().test(request)) {
                            String response = (String) entry.getValue().execute();
                            System.out.println("tcp.Server response is " + response);
                            printWriter.println(response);
                        }
                    }
                } else {
                    System.err.println("Client is disconnected");
                    break;
                }
            }
            socket.close();
        } catch (IOException ex) {
            System.err.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
