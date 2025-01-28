package agentarium;

public class ModelSettings {
    private int numOfAgents = 1;
    private int numOfCores = 1;

    public void setNumOfAgents(int numOfAgents) {
        this.numOfAgents = numOfAgents;
    }

    public void setNumOfCores(int numOfCores) {
        this.numOfCores = numOfCores;
    }

    public int getNumOfAgents() {
        return numOfAgents;
    }

    public int getNumOfCores() {
        return numOfCores;
    }
}
