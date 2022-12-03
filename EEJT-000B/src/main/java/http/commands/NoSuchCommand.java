package http.commands;

import http.MapResponse;
import services.Command;
import http.JsonResponse;

import java.util.Map;

public class NoSuchCommand implements Command<JsonResponse> {
    // TODO rename this one and tcp one
    @Override
    public JsonResponse execute() {
        return new MapResponse(Map.of("message", "No such command"));
    }
}
