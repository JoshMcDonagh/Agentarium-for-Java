package models.multithreading.requestresponse;

/**
 * The `Request` class represents a communication request in a multi-threaded environment.
 * <p>
 * It encapsulates information about the sender, recipient, the nature of the request,
 * and any associated data to be processed.
 */
public class Request {

    // The name of the thread or entity sending the request.
    private final String requester;

    // The name of the thread or entity to which the request is directed.
    private final String destination;

    // A unique code identifying the type of request.
    private final String requestCode;

    // The data or object associated with the request.
    private final Object payload;

    /**
     * Constructs a new `Request` with the specified parameters.
     *
     * @param requester   The name of the requester (sender).
     * @param destination The name of the destination (recipient).
     * @param requestCode The unique code representing the request type.
     * @param payload     The data or object associated with the request.
     */
    public Request(String requester, String destination, String requestCode, Object payload) {
        this.requester = requester;
        this.destination = destination;
        this.requestCode = requestCode;
        this.payload = payload;
    }

    /**
     * Retrieves the name of the requester (sender) of the request.
     *
     * @return The name of the requester.
     */
    public String getRequester() {
        return requester;
    }

    /**
     * Retrieves the name of the destination (recipient) of the request.
     *
     * @return The name of the destination.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Retrieves the request code, identifying the type of request.
     *
     * @return The request code.
     */
    public String getRequestCode() {
        return requestCode;
    }

    /**
     * Retrieves the payload, which contains the data or object associated with the request.
     *
     * @return The payload object.
     */
    public Object getPayload() {
        return payload;
    }
}
