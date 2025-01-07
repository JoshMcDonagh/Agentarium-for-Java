package models.multithreading.requestresponse;

/**
 * The `Response` class represents a response sent in a request-response
 * communication model. It encapsulates details about the requester, destination,
 * response code, and any associated payload data.
 */
public class Response {

    // The name of the thread or entity that initiated the request.
    private final String requester;

    // The name of the thread or entity that the response is addressed to.
    private final String destination;

    // A code indicating the type or purpose of the response.
    private final String responseCode;

    // The data or object associated with the response.
    private final Object payload;

    /**
     * Constructs a `Response` object with the specified details.
     *
     * @param requester    The name of the requester that initiated the request.
     * @param destination  The destination of the response.
     * @param responseCode The response code indicating the type or purpose of the response.
     * @param payload      The data or object associated with the response.
     */
    public Response(String requester, String destination, String responseCode, Object payload) {
        this.requester = requester;
        this.destination = destination;
        this.responseCode = responseCode;
        this.payload = payload;
    }

    /**
     * Retrieves the name of the requester that initiated the request.
     *
     * @return The requester name.
     */
    public String getRequester() {
        return requester;
    }

    /**
     * Retrieves the destination of the response.
     *
     * @return The destination name.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Retrieves the response code indicating the type or purpose of the response.
     *
     * @return The response code.
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Retrieves the payload data or object associated with the response.
     *
     * @return The payload object.
     */
    public Object getPayload() {
        return payload;
    }
}
