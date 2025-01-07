package models.multithreading.requestresponse;

/**
 * The `RequestCodes` class defines a set of unique identifiers for different types of requests
 * in a multi-threaded environment. These codes are used to categorise and handle specific
 * operations efficiently.
 */
public class RequestCodes {

    /**
     * Code indicating that all workers have finished their current tick.
     */
    public static final String ALL_WORKERS_FINISH_TICK = "e#Km66xgPCeNMCjj";

    /**
     * Code indicating that all workers should update the coordinator with their data.
     */
    public static final String ALL_WORKERS_UPDATE_COORDINATOR = "Mm?#e&kxEM$FDf4i";

    /**
     * Code for accessing a specific agent.
     */
    public static final String AGENT_ACCESS = "@oFRaXyY6J?f9PfY";

    /**
     * Code for accessing a filtered list of agents based on specific criteria.
     */
    public static final String FILTERED_AGENTS_ACCESS = "Kf9Qq7sjz?J94tic";

    /**
     * Code for accessing model attribute data.
     */
    public static final String MODEL_ATTRIBUTES_ACCESS = "4nJJ!JRB9Ab4nn9$";

    /**
     * Code for updating the coordinator with agent data.
     */
    public static final String UPDATE_COORDINATOR_AGENTS = "nnNfaNTQM5R@RydL";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private RequestCodes() {
        // This class should not be instantiated.
    }
}
