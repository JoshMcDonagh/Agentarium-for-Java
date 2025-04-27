package agentarium.multithreading;

public class Request {
    private final String requester;
    private final String destination;
    private final RequestType requestType;
    private final Object payload;

    public Request(String requester, String destination, RequestType requestType, Object payload) {
        this.requester = requester;
        this.destination = destination;
        this.requestType = requestType;
        this.payload = payload;
    }

    public String getRequester() {
        return requester;
    }

    public String getDestination() {
        return destination;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public Object getPayload() {
        return payload;
    }
}
