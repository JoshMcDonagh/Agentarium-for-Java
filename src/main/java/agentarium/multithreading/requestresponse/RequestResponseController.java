package agentarium.multithreading.requestresponse;

import java.util.concurrent.BlockingQueue;

public class RequestResponseController {
    private final boolean areProcessesSynced;
    private final BlockingQueue<Request> requestQueue;
    private final BlockingQueue<Response> responseQueue;

    public RequestResponseController(boolean areProcessesSynced, BlockingQueue<Request> requestQueue, BlockingQueue<Response> responseQueue) {
        this.areProcessesSynced = areProcessesSynced;
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    public BlockingQueue<Request> getRequestQueue() {
        return requestQueue;
    }

    public BlockingQueue<Response> getResponseQueue() {
        return responseQueue;
    }

    public RequestResponseInterface getInterface(String name) {
        return new RequestResponseInterface(name, areProcessesSynced, requestQueue, responseQueue);
    };
}
