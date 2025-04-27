package agentarium.multithreading;

public class Response {
    private final String requester;
    private final String destination;
    private final ResponseType responseType;
    private final Object payload;

    public Response(String requester, String destination, ResponseType responseType, Object payload) {
        this.requester = requester;
        this.destination = destination;
        this.responseType = responseType;
        this.payload = payload;
    }

    public String getRequester() {
        return requester;
    }

    public String getDestination() {
        return destination;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public Object getPayload() {
        return payload;
    }
}
