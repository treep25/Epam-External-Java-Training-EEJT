package tcp.commands;

public class UnknownCommand implements Command<String> {
    @Override
    public String execute() {
        return "Unknown command";
    }
}
