package com.epam.http;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public class MapResponse implements JsonResponse {
    private Map<String, String> responseMap;

    public MapResponse(Map<String, String> responseMap) {
        this.responseMap = responseMap;
    }

    @Override
    public String getJson() {
        try {
            return jsonMapper.writeValueAsString(responseMap);
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
