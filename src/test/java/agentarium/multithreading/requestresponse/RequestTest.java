package agentarium.multithreading.requestresponse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Request} class.
 *
 * <p>Ensures that request construction, field access, and null handling behave as expected.
 */
public class RequestTest {

    @Test
    public void testRequestFieldsAreSetCorrectly() {
        String requester = "Worker_1";
        String destination = "Coordinator";
        RequestType type = RequestType.AGENT_ACCESS;
        Object payload = "Agent_007";

        Request request = new Request(requester, destination, type, payload);

        assertEquals(requester, request.getRequester(), "Requester should match input");
        assertEquals(destination, request.getDestination(), "Destination should match input");
        assertEquals(type, request.getRequestType(), "Request type should match input");
        assertEquals(payload, request.getPayload(), "Payload should match input");
    }

    @Test
    public void testRequestWithNullPayload() {
        Request request = new Request("Worker_2", "Coordinator", RequestType.ALL_WORKERS_FINISH_TICK, null);

        assertNotNull(request);
        assertEquals("Worker_2", request.getRequester());
        assertEquals("Coordinator", request.getDestination());
        assertEquals(RequestType.ALL_WORKERS_FINISH_TICK, request.getRequestType());
        assertNull(request.getPayload(), "Payload should be null when not provided");
    }

    @Test
    public void testRequestWithNullDestination() {
        Request request = new Request("Worker_3", null, RequestType.UPDATE_COORDINATOR_AGENTS, "Payload");

        assertEquals("Worker_3", request.getRequester());
        assertNull(request.getDestination(), "Destination should allow null if not needed");
        assertEquals(RequestType.UPDATE_COORDINATOR_AGENTS, request.getRequestType());
        assertEquals("Payload", request.getPayload());
    }
}
