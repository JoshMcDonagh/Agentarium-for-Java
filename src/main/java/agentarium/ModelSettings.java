package agentarium;

import agentarium.attributes.AttributeSetCollection;

public class ModelSettings {
    private int numOfAgents = 1;
    private int numOfCores = 1;
    private int numOfTicksToRun = 1;
    private int numOfWarmUpTicks = 0;
    private AttributeSetCollection baseAgentAttributeSetCollection = new AttributeSetCollection();
    private AttributeSetCollection baseEnvironmentAttributeSetCollection = new AttributeSetCollection();
    private boolean areProcessesSynced = false;
    private boolean doAgentStoresHoldAgentCopies = false;
    private boolean isCacheUsed = false;

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

    public void setBaseAgentAttributeSetCollection(AttributeSetCollection baseAgentAttributeSetCollection) {
        this.baseAgentAttributeSetCollection = baseAgentAttributeSetCollection;
    }

    public void setBaseEnvironmentAttributeSetCollection(AttributeSetCollection baseEnvironmentAttributeSetCollection) {
        this.baseEnvironmentAttributeSetCollection = baseEnvironmentAttributeSetCollection;
    }

    public void setAreProcessesSynced(boolean areProcessesSynced) {
        this.areProcessesSynced = areProcessesSynced;
    }

    public void setDoAgentStoresHoldAgentCopies(boolean doAgentStoresHoldAgentCopies) {
        this.doAgentStoresHoldAgentCopies = doAgentStoresHoldAgentCopies;
    }

    public void setIsCacheUsed(boolean isCacheUsed) {
        this.isCacheUsed = isCacheUsed;
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

    public AttributeSetCollection getBaseAgentAttributeSetCollection() {
        return baseAgentAttributeSetCollection;
    }

    public AttributeSetCollection getBaseEnvironmentAttributeSetCollection() {
        return baseEnvironmentAttributeSetCollection;
    }

    public boolean getAreProcessesSynced() {
        return areProcessesSynced;
    }

    public boolean getDoAgentStoresHoldAgentCopies() {
        return doAgentStoresHoldAgentCopies;
    }

    public boolean getIsCacheUsed() {
        return isCacheUsed;
    }
}
