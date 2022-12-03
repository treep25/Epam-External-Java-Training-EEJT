package http;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface JsonResponse {
    ObjectMapper jsonMapper = new ObjectMapper();

    String getJson();
}
