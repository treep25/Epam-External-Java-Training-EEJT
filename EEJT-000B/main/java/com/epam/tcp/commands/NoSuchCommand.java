package com.epam.tcp.commands;

import com.epam.Command;
import com.epam.tcp.Response;
import com.epam.tcp.StringResponse;

public class NoSuchCommand implements Command<StringResponse> {
    @Override
    public StringResponse execute(String request) {
        return new Response(request + " is Unknown command");
    }
}
