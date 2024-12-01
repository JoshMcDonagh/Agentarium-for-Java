package models;

import agents.Agent;
import models.agentgenerator.AgentGenerator;
import models.modelattributes.ModelAttributeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private int numOfAgents;
    private ModelClock clock;
    private AgentGenerator agentGenerator;
    private ModelResults results;

    private int numOfCores = 1;
    private List<ModelResults> resultsPerProcess = new ArrayList<ModelResults>();
    private Map<String, ModelAttributeSet> modelAttributeSetMap = new HashMap<String, ModelAttributeSet>();
    private List<ModelAttributeSet> modelAttributeSetList = new ArrayList<ModelAttributeSet>();
    private boolean areProcessesSynced = false;
    private boolean doProcessStoresHoldAgentCopies = true;
    private boolean isAgentCacheUsed = false;

    public Model(int numOfAgents, ModelClock clock, AgentGenerator agentGenerator, ModelResults results) {
        this.numOfAgents = numOfAgents;
        this.clock = clock;
        this.agentGenerator = agentGenerator;
        this.agentGenerator.setAssociatedModel(this);
        this.results = results;
    }

    public void addAttributeSet(ModelAttributeSet modelAttributeSet) {
        modelAttributeSetMap.put(modelAttributeSet.name(), modelAttributeSet);
        modelAttributeSetList.add(modelAttributeSet);
    }

    public void addAttributeSets(ModelAttributeSet[] modelAttributeSets) {
        for (ModelAttributeSet modelAttributeSet : modelAttributeSets) {
            addAttributeSet(modelAttributeSet);
        }
    }

    public void setNumberOfCores(int numOfCores) {
        this.numOfCores = numOfCores;
    }

    public void setAreProcessesSynced(boolean areProcessesSynced) {
        this.areProcessesSynced = areProcessesSynced;
    }

    public void setDoProcessStoresHoldAgentCopies() {
        doProcessStoresHoldAgentCopies = true;
    }

    public void setIsAgentCacheUsed(boolean isAgentCacheUsed) {
        this.isAgentCacheUsed = isAgentCacheUsed;
    }

    public int numberOfAgents() {
        return numOfAgents;
    }

    public ModelClock clock() {
        return clock;
    }

    public void run() {
        List<Agent> agents = agentGenerator.generateAgents(numOfAgents);
    }
}
