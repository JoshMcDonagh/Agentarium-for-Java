package models.multithreading.requestresponse;

public class Response {
    private final String requester;
    private final String destination;
    private final String responseCode;
    private final Object payload;

    public Response(String requester, String destination, String responseCode, Object payload) {
        this.requester = requester;
        this.destination = destination;
        this.responseCode = responseCode;
        this.payload = payload;
    }

    public String getRequester() {
        return requester;
    }

    public String getDestination() {
        return destination;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public Object getPayload() {
        return payload;
    }
}
