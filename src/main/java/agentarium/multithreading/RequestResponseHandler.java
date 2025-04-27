package agentarium.multithreading;

import java.util.concurrent.BlockingQueue;

public class RequestResponseHandler {
    private final BlockingQueue<Request> requestQueue;
    private final BlockingQueue<Response> responseQueue;

    public RequestResponseHandler(BlockingQueue<Request> requestQueue, BlockingQueue<Response> responseQueue) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    public BlockingQueue<Request> getRequestQueue() {
        return requestQueue;
    }

    public BlockingQueue<Response> getResponseQueue() {
        return responseQueue;
    }
}
