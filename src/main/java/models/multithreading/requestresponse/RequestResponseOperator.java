package models.multithreading.requestresponse;

import models.ModelClock;

import java.util.concurrent.BlockingQueue;

public class RequestResponseOperator {
    private String threadName;
    private ModelClock clock;
    private boolean areProcessesSynced;
    private BlockingQueue<Request> requestQueue;
    private BlockingQueue<Response> responseQueue;

    public RequestResponseOperator(String threadName, ModelClock clock, boolean areProcessesSynced, BlockingQueue<Request> requestQueue, BlockingQueue<Response> responseQueue) {
        this.threadName = threadName;
        this.clock = clock;
        this.areProcessesSynced = areProcessesSynced;
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    private void wait(String requestCode, String responseCode) throws InterruptedException {
        if (!areProcessesSynced)
            return;

        requestQueue.put(new Request(threadName, null, requestCode, null));

        while (true) {
            while (responseQueue.isEmpty())
                continue;

            Response response = responseQueue.poll();

            if (response.getResponseCode() == responseCode && response.getDestination() == threadName)
                return;
            else
                responseQueue.put(response);
        }
    }
}
