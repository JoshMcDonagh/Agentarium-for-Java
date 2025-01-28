package agentarium;

import agentarium.attributes.AttributeSetCollection;

public class ModelSettings {
    private int numOfAgents = 1;
    private int numOfCores = 1;
    private int numOfTicksToRun = 1;
    private int numOfWarmUpTicks = 0;
    private AttributeSetCollection baseAgentAttributeSetCollection = new AttributeSetCollection();
    private AttributeSetCollection baseEnvironmentAttributeSetCollection = new AttributeSetCollection();

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

    public void setBaseAgentAttributeSetCollection(AttributeSetCollection baseAgentAttributeSetCollection) {
        this.baseAgentAttributeSetCollection = baseAgentAttributeSetCollection;
    }

    public void setBaseEnvironmentAttributeSetCollection(AttributeSetCollection baseEnvironmentAttributeSetCollection) {
        this.baseEnvironmentAttributeSetCollection = baseEnvironmentAttributeSetCollection;
    }

    public AttributeSetCollection getBaseAgentAttributeSetCollection() {
        return baseAgentAttributeSetCollection;
    }

    public AttributeSetCollection getBaseEnvironmentAttributeSetCollection() {
        return baseEnvironmentAttributeSetCollection;
    }
}
