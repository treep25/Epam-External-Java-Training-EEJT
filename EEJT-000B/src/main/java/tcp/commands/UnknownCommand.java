package tcp.commands;

import services.Command;

public class UnknownCommand implements Command<String> {
    @Override
    public String execute() {
        return "Unknown command";
    }
}
