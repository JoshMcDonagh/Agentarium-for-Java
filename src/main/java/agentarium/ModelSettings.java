package agentarium;

public class ModelSettings {
    private int numOfAgents = 1;
    private int numOfCores = 1;
    private int numOfTicksToRun = 1;
    private int numOfWarmUpTicks = 0;

    public void setNumOfAgents(int numOfAgents) {
        this.numOfAgents = numOfAgents;
    }

    public void setNumOfCores(int numOfCores) {
        this.numOfCores = numOfCores;
    }

    public void setNumOfTicksToRun(int numOfTicksToRun) {
        this.numOfTicksToRun = numOfTicksToRun;
    }

    public void setNumOfWarmUpTicks(int numOfWarmUpTicks) {
        this.numOfWarmUpTicks = numOfWarmUpTicks;
    }

    public int getNumOfAgents() {
        return numOfAgents;
    }

    public int getNumOfCores() {
        return numOfCores;
    }

    public int getNumOfTicksToRun() {
        return numOfTicksToRun;
    }

    public int getNumOfWarmUpTicks() {
        return numOfWarmUpTicks;
    }
}
