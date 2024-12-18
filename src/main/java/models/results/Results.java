package models.results;

import agents.Agent;
import agents.attributes.results.AgentAttributeResults;
import agents.attributes.results.AgentAttributeSetResults;
import attributedatabases.AttributeResultsDatabase;
import attributedatabases.AttributeResultsDatabaseFactory;
import models.modelattributes.results.ModelAttributeResults;
import models.modelattributes.results.ModelAttributeSetResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Results {
    private FinalAgentAttributeResults finalAgentAttributeResults;
    private FinalModelAttributeResults finalModelAttributeResults;

    private final Map<String, AttributeResultsDatabase> accumulatedAgentAttributeResultsDatabasesMap = new HashMap<String, AttributeResultsDatabase>();
    private final Map<String, AttributeResultsDatabase> accumulatedModelAttributeResultsDatabasesMap = new HashMap<String, AttributeResultsDatabase>();
    private final List<AttributeResultsDatabase> accumulatedAgentAttributeResultsDatabasesList = new ArrayList<AttributeResultsDatabase>();
    private final List<AttributeResultsDatabase> accumulatedModelAttributeResultsDatabasesList = new ArrayList<AttributeResultsDatabase>();

    private final List<String> agentNames = new ArrayList<>();

    private boolean isRawAgentAttributeDataConnected = false;
    private boolean isRawModelAttributeDataConnected = false;
    private boolean isAccumulatedModelAttributeDataConnected = false;

    public void setAgentNames(List<Agent> agents) {
        for (Agent agent : agents)
            agentNames.add(agent.name());
    }

    public List<String> getAgentNames() {
        return agentNames;
    }

    public void setFinalAgentAttributeResults(FinalAgentAttributeResults finalAgentAttributeResults) {
        this.finalAgentAttributeResults = finalAgentAttributeResults;
        if (finalAgentAttributeResults != null)
            isRawAgentAttributeDataConnected = true;
    }

    public void setFinalModelAttributes(FinalModelAttributeResults finalModelAttributeResults) {
        this.finalModelAttributeResults = finalModelAttributeResults;
        if (finalModelAttributeResults != null)
            isRawModelAttributeDataConnected = true;
    }

    public List<Object> getAgentPropertyValues(String agentName, String attributeName, String propertyName) {
        if (isRawAgentAttributeDataConnected)
            return finalAgentAttributeResults.getAgentPropertyValues(agentName, attributeName, propertyName);
        else
            throw new IllegalStateException("Access of agent attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Boolean> getAgentPreEventTriggers(String agentName, String attributeName, String eventName) {
        if (isRawAgentAttributeDataConnected)
            return finalAgentAttributeResults.getAgentPreEventTriggers(agentName, attributeName, eventName);
        else
            throw new IllegalStateException("Access of agent attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Boolean> getAgentPostEventTriggers(String agentName, String attributeName, String eventName) {
        if (isRawAgentAttributeDataConnected)
            return finalAgentAttributeResults.getAgentPostEventTriggers(agentName, attributeName, eventName);
        else
            throw new IllegalStateException("Access of agent attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getModelPropertyValues(String attributeName, String propertyName) {
        if (isRawModelAttributeDataConnected)
            return finalModelAttributeResults.getModelPropertyValues(attributeName, propertyName);
        else
            throw new IllegalStateException("Access of model attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Boolean> getModelPreEventTriggers(String attributeName, String eventName) {
        if (isRawModelAttributeDataConnected)
            return finalModelAttributeResults.getModelPreEventTriggers(attributeName, eventName);
        else
            throw new IllegalStateException("Access of model attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Boolean> getModelPostEventTriggers(String attributeName, String eventName) {
        if (isRawModelAttributeDataConnected)
            return finalModelAttributeResults.getModelPostEventTriggers(attributeName, eventName);
        else
            throw new IllegalStateException("Access of model attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedAgentPropertyValues(String attributeName, String propertyName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPropertyColumnAsList(propertyName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedAgentPreEventValues(String attributeName, String eventName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPreEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedAgentPostEventValues(String attributeName, String eventName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPostEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedModelPropertyValues(String attributeName, String propertyName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedModelAttributeResultsDatabasesMap.get(attributeName).getPropertyColumnAsList(propertyName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedModelPreEventValues(String attributeName, String eventName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedModelAttributeResultsDatabasesMap.get(attributeName).getPreEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedModelPostEventValues(String attributeName, String eventName) {
        if (isAccumulatedModelAttributeDataConnected)
            return accumulatedModelAttributeResultsDatabasesMap.get(attributeName).getPostEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public void disconnectRawDatabases() {
        if (isRawAgentAttributeDataConnected) {
            finalAgentAttributeResults.disconnectDatabases();
            isRawAgentAttributeDataConnected = false;
        }

        if (isRawModelAttributeDataConnected) {
            finalModelAttributeResults.disconnectDatabases();
            isRawModelAttributeDataConnected = false;
        }
    }

    public void disconnectAccumulatedDatabases() {
        if (isAccumulatedModelAttributeDataConnected) {
            for (AttributeResultsDatabase attributeResultsDatabase : accumulatedAgentAttributeResultsDatabasesList)
                attributeResultsDatabase.disconnect();
            accumulatedModelAttributeResultsDatabasesMap.clear();
            accumulatedAgentAttributeResultsDatabasesList.clear();

            for (AttributeResultsDatabase attributeResultsDatabase : accumulatedModelAttributeResultsDatabasesList)
                attributeResultsDatabase.disconnect();
            accumulatedModelAttributeResultsDatabasesMap.clear();
            accumulatedModelAttributeResultsDatabasesList.clear();

            isAccumulatedModelAttributeDataConnected = false;
        }
    }

    public void disconnectAllDatabases() {
        disconnectRawDatabases();
        disconnectAccumulatedDatabases();
    }

    public void accumulateAgentAttributeData() {
        for (int i = 0; i < finalAgentAttributeResults.getAgentAttributeResultsCount(); i++) {
            AgentAttributeResults agentAttributeResults = finalAgentAttributeResults.getAgentAttributeResultsByIndex(i);

            if (accumulatedAgentAttributeResultsDatabasesMap.isEmpty()) {
                for (int j = 0; j < agentAttributeResults.getAttributeSetCount(); j++) {
                    String attributeName = agentAttributeResults.getAttributeResultsByIndex(j).getAttributeName();
                    AttributeResultsDatabase newDatabase = AttributeResultsDatabaseFactory.createDatabase();
                    assert newDatabase != null;
                    newDatabase.connect();
                    accumulatedAgentAttributeResultsDatabasesMap.put(attributeName, newDatabase);
                    accumulatedAgentAttributeResultsDatabasesList.add(newDatabase);
                }

                isAccumulatedModelAttributeDataConnected = true;
            }

            for (int j = 0; j < agentAttributeResults.getAttributeSetCount(); j++) {
                AgentAttributeSetResults agentAttributeSetResults = agentAttributeResults.getAttributeResultsByIndex(j);
                String attributeName = agentAttributeSetResults.getAttributeName();

                List<String> propertyNamesList = agentAttributeSetResults.getPropertyNamesList();
                for (String propertyName : propertyNamesList) {
                    List<?> accumulatedValues = accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPropertyColumnAsList(propertyName);
                    List<?> valuesToBeProcessed = agentAttributeSetResults.getPropertyValues(propertyName);
                    List<?> newAccumulatedValues = accumulateAgentPropertyResults(attributeName, propertyName, accumulatedValues, valuesToBeProcessed);
                    accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).setPropertyColumn(propertyName, (List<Object>) newAccumulatedValues);
                }

                List<String> preEventNamesList = agentAttributeSetResults.getPreEventNamesList();
                for (String preEventName : preEventNamesList) {
                    List<?> accumulatedValues = accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPreEventColumnAsList(preEventName);
                    List<?> valuesToBeProcessed = agentAttributeSetResults.getPreEventTriggers(preEventName);
                    List<?> newAccumulatedValues = accumulateAgentPropertyResults(attributeName, preEventName, accumulatedValues, valuesToBeProcessed);
                    accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).setPreEventColumn(preEventName, (List<Object>) newAccumulatedValues);
                }

                List<String> postEventNamesList = agentAttributeSetResults.getPostEventNamesList();
                for (String postEventName : postEventNamesList) {
                    List<?> accumulatedValues = accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).getPostEventColumnAsList(postEventName);
                    List<?> valuesToBeProcessed = agentAttributeSetResults.getPostEventTriggers(postEventName);
                    List<?> newAccumulatedValues = accumulateAgentPropertyResults(attributeName, postEventName, accumulatedValues, valuesToBeProcessed);
                    accumulatedAgentAttributeResultsDatabasesMap.get(attributeName).setPostEventColumn(postEventName, (List<Object>) newAccumulatedValues);
                }
            }
        }
    }

    public void processModelAttributeData() {
        ModelAttributeResults modelAttributeResults = finalModelAttributeResults.get();

        if (accumulatedModelAttributeResultsDatabasesMap.isEmpty()) {
            for (int j = 0; j < modelAttributeResults.getAttributeSetCount(); j++) {
                String attributeName = modelAttributeResults.getAttributeResultsByIndex(j).getAttributeName();
                AttributeResultsDatabase newDatabase = AttributeResultsDatabaseFactory.createDatabase();
                assert newDatabase != null;
                newDatabase.connect();
                accumulatedModelAttributeResultsDatabasesMap.put(attributeName, newDatabase);
                accumulatedModelAttributeResultsDatabasesList.add(newDatabase);
            }
        }

        for (int i = 0; i < modelAttributeResults.getAttributeSetCount(); i++) {
            ModelAttributeSetResults modelAttributeSetResults = modelAttributeResults.getAttributeResultsByIndex(i);
            String attributeName = modelAttributeSetResults.getAttributeName();

            List<String> propertyNamesList = modelAttributeSetResults.getPropertyNamesList();
            for (String propertyName : propertyNamesList) {
                List<?> valuesToBeProcessed = modelAttributeSetResults.getPropertyValues(propertyName);
                List<?> newProcessedValues = processModelPropertyResults(attributeName, propertyName, valuesToBeProcessed);
                accumulatedModelAttributeResultsDatabasesMap.get(attributeName).setPropertyColumn(propertyName, (List<Object>) newProcessedValues);
            }

            List<String> preEventNamesList = modelAttributeSetResults.getPreEventNamesList();
            for (String preEventName : preEventNamesList) {
                List<?> valuesToBeProcessed = modelAttributeSetResults.getPreEventTriggers(preEventName);
                List<?> newProcessedValues = processModelPreEventResults(attributeName, preEventName, valuesToBeProcessed);
                accumulatedModelAttributeResultsDatabasesMap.get(attributeName).setPreEventColumn(preEventName, (List<Object>) newProcessedValues);
            }

            List<String> postEventNamesList = modelAttributeSetResults.getPostEventNamesList();
            for (String postEventName : postEventNamesList) {
                List<?> valuesToBeProcessed = modelAttributeSetResults.getPostEventTriggers(postEventName);
                List<?> newProcessedValues = processModelPostEventResults(attributeName, postEventName, valuesToBeProcessed);
                accumulatedModelAttributeResultsDatabasesMap.get(attributeName).setPostEventColumn(postEventName, (List<Object>) newProcessedValues);
            }
        }
    }

    public void mergeWithBeforeAccumulation(Results otherResults) {
        finalAgentAttributeResults.mergeWith(otherResults.finalAgentAttributeResults);
    }

    //public abstract Results duplicate();

    protected List<?> processModelPropertyResults(String attributeName, String propertyName, List<?> propertyValues) {
        return propertyValues;
    }

    protected List<?> processModelPreEventResults(String attributeName, String eventName, List<?> preEventValues) {
        return preEventValues;
    }

    protected List<?> processModelPostEventResults(String attributeName, String eventName, List<?> postEventValues) {
        return postEventValues;
    }

    protected abstract List<?> accumulateAgentPropertyResults(
            String attributeName,
            String propertyName,
            List<?> accumulatedValues,
            List<?> valuesToBeProcessed
    );

    protected abstract List<?> accumulateAgentPreEventResults(
            String attributeName,
            String preEventName,
            List<?> accumulatedValues,
            List<Boolean> valuesToBeProcessed
    );

    protected abstract List<?> accumulateAgentPostEventResults(
            String attributeName,
            String postEventName,
            List<?> accumulatedValues,
            List<Boolean> valuesToBeProcessed
    );
}
