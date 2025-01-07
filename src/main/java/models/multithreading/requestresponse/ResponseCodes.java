package models.multithreading.requestresponse;

/**
 * The `ResponseCodes` class defines a collection of constants representing
 * standardised codes used in responses within a request-response communication model.
 * <p>
 * These codes are used to identify the purpose or type of a response, ensuring
 * consistent communication between threads or entities in a multi-threaded environment.
 */
public class ResponseCodes {

    /**
     * Code indicating that all workers have finished their current tick.
     */
    public static final String ALL_WORKERS_FINISH_TICK = "kaX7h3bP@eTgX9oy";

    /**
     * Code indicating that all workers have updated the coordinator.
     */
    public static final String ALL_WORKERS_UPDATE_COORDINATOR = "Dr4p$QHoS8FXQtKh";

    /**
     * Code for accessing a specific agent.
     */
    public static final String AGENT_ACCESS = "ydMzzfPc87f@7xrQ";

    /**
     * Code for accessing filtered agents based on a condition.
     */
    public static final String FILTERED_AGENTS_ACCESS = "6rhNNtc3DnQje?Ha";

    /**
     * Code for accessing model attribute sets.
     */
    public static final String MODEL_ATTRIBUTES_ACCESS = "kqG8qSyxi#&Ms78k";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ResponseCodes() {
        // This class should not be instantiated.
    }
}
