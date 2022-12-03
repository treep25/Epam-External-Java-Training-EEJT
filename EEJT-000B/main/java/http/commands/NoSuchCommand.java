package http.commands;

import http.JsonResponse;
import http.MapResponse;
import services.Command;

import java.util.Map;

public class NoSuchCommand implements Command<JsonResponse> {
    @Override
    public JsonResponse execute() {
        return new MapResponse(Map.of("message", "No such command"));
    }
}
