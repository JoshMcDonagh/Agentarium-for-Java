package agentarium.multithreading.requestresponse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestResponseController {
    private final boolean areProcessesSynced;
    private final BlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();;
    private final BlockingQueue<Response> responseQueue = new LinkedBlockingQueue<>();;

    public RequestResponseController(boolean areProcessesSynced) {
        this.areProcessesSynced = areProcessesSynced;
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
