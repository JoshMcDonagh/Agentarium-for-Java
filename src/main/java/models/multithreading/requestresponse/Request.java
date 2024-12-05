package models.multithreading.requestresponse;

public class Request {
    private final String requester;
    private final String destination;
    private final String requestCode;
    private final Object payload;

    public Request(String requester, String destination, String requestCode, Object payload) {
        this.requester = requester;
        this.destination = destination;
        this.requestCode = requestCode;
        this.payload = payload;
    }

    public String getRequester() {
        return requester;
    }

    public String getDestination() {
        return destination;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public Object getPayload() {
        return payload;
    }
}
