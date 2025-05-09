package agentarium.multithreading;

import agentarium.ModelSettings;
import agentarium.environments.Environment;
import agentarium.multithreading.requestresponse.CoordinatorRequestHandler;
import agentarium.multithreading.requestresponse.Request;
import agentarium.multithreading.requestresponse.RequestResponseController;
import agentarium.multithreading.requestresponse.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CoordinatorThread} class.
 *
 * <p>Verifies correct setup behaviour and basic coordination logic using mocked dependencies.
 */
public class CoordinatorThreadTest {

    private ModelSettings settings;
    private Environment environment;
    private RequestResponseController controller;
    private BlockingQueue<Request> requestQueue;
    private BlockingQueue<Response> responseQueue;

    @BeforeEach
    void setUp() {
        settings = mock(ModelSettings.class);
        environment = mock(Environment.class);
        controller = mock(RequestResponseController.class);
        requestQueue = new LinkedBlockingQueue<>();
        responseQueue = new LinkedBlockingQueue<>();

        when(controller.getRequestQueue()).thenReturn(requestQueue);
        when(controller.getResponseQueue()).thenReturn(responseQueue);
    }

    @Test
    void testCoordinatorThreadInitialisesCorrectly() {
        CoordinatorThread coordinatorThread = new CoordinatorThread(
                "Coordinator-1",
                settings,
                environment,
                controller
        );

        assertNotNull(coordinatorThread, "Coordinator thread should be instantiated without error.");
    }

    @Test
    void testCoordinatorThreadHandlesRequestWhenAvailable() throws InterruptedException {
        // Mock request to simulate worker input
        Request mockRequest = mock(Request.class);
        requestQueue.add(mockRequest);

        // Spy on static method
        var handler = mockStatic(CoordinatorRequestHandler.class);
        handler.when(() -> CoordinatorRequestHandler.initialise(any(), any(), any(), any(), any())).thenCallRealMethod();
        handler.when(() -> CoordinatorRequestHandler.handleCoordinatorRequest(mockRequest)).thenAnswer(invocation -> null);

        CoordinatorThread coordinatorThread = new CoordinatorThread("TestThread", settings, environment, controller);

        // Run in a separate thread and interrupt after some delay to prevent infinite loop
        Thread thread = new Thread(() -> {
            try {
                coordinatorThread.run(); // This normally loops forever
            } catch (RuntimeException ignored) {
                // Ignore runtime exceptions for test interruption
            }
        });

        thread.start();
        Thread.sleep(100); // Let it process at least once
        thread.stop(); // Deprecated but acceptable in tightly controlled test conditions

        handler.verify(() -> CoordinatorRequestHandler.handleCoordinatorRequest(mockRequest), times(1));
        handler.close();
    }
}
