package com.epam.http;

import com.epam.http.MapResponse;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapResponseTest {

    @Test
    void getJsonTest() {
        assertEquals("{\"message\":\"No such command\"}", new MapResponse(Map.of("message", "No such command")).getJson());
        assertEquals("{\"1\":\"1\"}", new MapResponse(Map.of("1", "1")).getJson());
    }
}