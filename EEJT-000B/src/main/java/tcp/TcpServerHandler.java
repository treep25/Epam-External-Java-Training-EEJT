package tcp;

import services.Command;
import io.ConsoleIO;
import tcp.commands.CountAmountOfProductsTCPCommand;
import tcp.commands.GetItemByNumberTCPCommand;
import tcp.commands.UnknownCommand;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

//TODO mahe like in http
public class TcpServerHandler extends Thread {
    private ConsoleIO console;
    private Map<Predicate<String>, Command> commandMap;

    public TcpServerHandler(Socket socket) {
        console = new ConsoleIO(socket);
    }

    void mapInitCommands(String request) {
        commandMap.put(req -> Objects.equals(request, "<get count>"), new CountAmountOfProductsTCPCommand());
        commandMap.put(req -> request.matches("<get item = \\d+>"), new GetItemByNumberTCPCommand(request.replaceAll("\\D", "")));
        commandMap.put(req -> !Objects.equals(request, "<get count>") && !request.matches("<get item = \\d+>"), new UnknownCommand());
    }

    @Override
    public void run() {//TODO ConsoleIO
        console.initIO();

        console.print("TCP Client is connected TCP");
        console.writeServer("(get count) - To count amount of products");
        console.writeServer("(get item = number of product(id))- To see information");

        while (true) {
            String request = console.readLine();

            if (!request.equals("")) {
                commandMap = new HashMap<>();
                console.print("TCP Client request is " + request);
                mapInitCommands(request);
                for (Map.Entry<Predicate<String>, Command> entry : commandMap.entrySet()) {
                    if (entry.getKey().test(request)) {
                        String response = (String) entry.getValue().execute();
                        console.print("TCP Server response is " + response);
                        console.writeServer(response);
                    }
                }
            } else {
                console.print("TCP Client is disconnected TCP");
                break;
            }
        }
        console.closeIO();
        console.closeSocket();
    }
}
