package tcp;

public class Response implements StringResponse {
    private final String response;

    public Response(String response) {
        this.response = response;
    }

    @Override
    public String getString() {
        return response;
    }
}
