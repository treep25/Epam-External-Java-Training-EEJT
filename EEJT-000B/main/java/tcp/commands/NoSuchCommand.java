package tcp.commands;

import services.Command;
import tcp.Response;
import tcp.StringResponse;

public class NoSuchCommand implements Command<StringResponse> {
    @Override
    public StringResponse execute() {
        return new Response("Unknown command");
    }
}
