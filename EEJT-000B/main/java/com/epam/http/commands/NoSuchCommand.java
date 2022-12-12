package com.epam.http.commands;

import com.epam.http.JsonResponse;
import com.epam.http.MapResponse;
import com.epam.Command;

import java.util.Map;

public class NoSuchCommand implements Command<JsonResponse> {

    @Override
    public JsonResponse execute(String request) {
        return new MapResponse(Map.of(request, "No such command"));
    }
}
