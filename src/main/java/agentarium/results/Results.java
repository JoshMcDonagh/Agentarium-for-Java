package agentarium.results;

import agentarium.agents.Agent;
import agentarium.agents.AgentSet;
import agentarium.attributes.results.AttributeSetCollectionResults;
import agentarium.attributes.results.AttributeSetResults;
import agentarium.attributes.results.databases.AttributeSetResultsDatabase;
import agentarium.attributes.results.databases.AttributeSetResultsDatabaseFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Results {
    private AgentResults agentResults;
    private EnvironmentResults environmentResults;

    private final Map<String, AttributeSetResultsDatabase> accumulatedAgentAttributeSetResultsDatabaseMap = new HashMap<>();
    private final Map<String, AttributeSetResultsDatabase> processedEnvironmentAttributeSetResultsDatabaseMap = new HashMap<>();
    private final List<AttributeSetResultsDatabase> accumulatedAgentAttributeSetResultsDatabaseList = new ArrayList<>();
    private final List<AttributeSetResultsDatabase> processedEnvironmentAttributeSetResultsDatabaseList = new ArrayList<>();

    private final List<String> agentNames = new ArrayList<>();

    private boolean isRawAgentAttributeSetDataConnected = false;
    private boolean isRawEnvironmentAttributeSetDataConnected = false;
    private boolean isAccumulatedAgentAttributeSetDataConnected = false;
    private boolean isProcessedEnvironmentAttributeSetDataConnected = false;

    private boolean isImmutable = false;

    public void seal() {
        isImmutable = true;
    }

    public void setAgentNames(AgentSet agents) {
        if (isImmutable) throw new IllegalStateException("Cannot modify Results: object is immutable.");

        for (Agent agent : agents)
            agentNames.add(agent.getName());
    }

    public List<String> getAgentNames() {
        return agentNames;
    }

    public void setAgentResults(AgentResults agentResults) {
        if (isImmutable) throw new IllegalStateException("Cannot modify Results: object is immutable.");

        this.agentResults = agentResults;
        if (agentResults != null)
            isRawAgentAttributeSetDataConnected = true;
    }

    public void setEnvironmentResults(EnvironmentResults environmentResults) {
        if (isImmutable) throw new IllegalStateException("Cannot modify Results: object is immutable.");

        this.environmentResults = environmentResults;
        if (agentResults != null)
            isRawEnvironmentAttributeSetDataConnected = true;
    }

    public List<Object> getAgentPropertyValues(String agentName, String attributeSetName, String propertyName) {
        if (isRawAgentAttributeSetDataConnected)
            return agentResults.getPropertyValues(agentName, attributeSetName, propertyName);
        else
            throw new IllegalStateException("Access of agent attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Boolean> getAgentPreEventValues(String agentName, String attributeSetName, String eventName) {
        if (isRawAgentAttributeSetDataConnected)
            return agentResults.getPreEventValues(agentName, attributeSetName, eventName);
        else
            throw new IllegalStateException("Access of agent attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Boolean> getAgentPostEventValues(String agentName, String attributeSetName, String eventName) {
        if (isRawAgentAttributeSetDataConnected)
            return agentResults.getPostEventValues(agentName, attributeSetName, eventName);
        else
            throw new IllegalStateException("Access of agent attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getEnvironmentPropertyValues(String attributeSetName, String propertyName) {
        if (isRawEnvironmentAttributeSetDataConnected)
            return environmentResults.getPropertyValues(attributeSetName, propertyName);
        else
            throw new IllegalStateException("Access of environment attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Boolean> getEnvironmentPreEventValues(String attributeSetName, String eventName) {
        if (isRawEnvironmentAttributeSetDataConnected)
            return environmentResults.getPreEventValues(attributeSetName, eventName);
        else
            throw new IllegalStateException("Access of environment attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Boolean> getEnvironmentPostEventValues(String attributeSetName, String eventName) {
        if (isRawEnvironmentAttributeSetDataConnected)
            return environmentResults.getPostEventValues(attributeSetName, eventName);
        else
            throw new IllegalStateException("Access of environment attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedAgentPropertyValues(String attributeSetName, String propertyName) {
        if (isAccumulatedAgentAttributeSetDataConnected)
            return accumulatedAgentAttributeSetResultsDatabaseMap.get(attributeSetName).getPropertyColumnAsList(propertyName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedAgentPreEventValues(String attributeSetName, String eventName) {
        if (isAccumulatedAgentAttributeSetDataConnected)
            return accumulatedAgentAttributeSetResultsDatabaseMap.get(attributeSetName).getPreEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedAgentPostEventValues(String attributeSetName, String eventName) {
        if (isAccumulatedAgentAttributeSetDataConnected)
            return accumulatedAgentAttributeSetResultsDatabaseMap.get(attributeSetName).getPostEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedEnvironmentPropertyValues(String attributeSetName, String propertyName) {
        if (isProcessedEnvironmentAttributeSetDataConnected)
            return processedEnvironmentAttributeSetResultsDatabaseMap.get(attributeSetName).getPropertyColumnAsList(propertyName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedEnvironmentPreEventValues(String attributeSetName, String eventName) {
        if (isProcessedEnvironmentAttributeSetDataConnected)
            return processedEnvironmentAttributeSetResultsDatabaseMap.get(attributeSetName).getPreEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public List<Object> getAccumulatedEnvironmentPostEventValues(String attributeSetName, String eventName) {
        if (isProcessedEnvironmentAttributeSetDataConnected)
            return processedEnvironmentAttributeSetResultsDatabaseMap.get(attributeSetName).getPostEventColumnAsList(eventName);
        else
            throw new IllegalStateException("Access of accumulated attribute database is not allowed when the appropriate database is disconnected.");
    }

    public void disconnectedRawDatabases() {
        if (isRawAgentAttributeSetDataConnected) {
            agentResults.disconnectDatabases();
            isRawAgentAttributeSetDataConnected = false;
        }

        if (isRawEnvironmentAttributeSetDataConnected) {
            environmentResults.disconnectDatabases();;
            isRawEnvironmentAttributeSetDataConnected = false;
        }
    }

    public void disconnectAccumulatedDatabases() {
        if (isAccumulatedAgentAttributeSetDataConnected) {
            for (AttributeSetResultsDatabase attributeSetResultsDatabase : accumulatedAgentAttributeSetResultsDatabaseList)
                attributeSetResultsDatabase.disconnect();
            accumulatedAgentAttributeSetResultsDatabaseMap.clear();
            accumulatedAgentAttributeSetResultsDatabaseList.clear();
            isAccumulatedAgentAttributeSetDataConnected = false;
        }

        if (isProcessedEnvironmentAttributeSetDataConnected) {
            for (AttributeSetResultsDatabase attributeSetResultsDatabase : processedEnvironmentAttributeSetResultsDatabaseList)
                attributeSetResultsDatabase.disconnect();
            processedEnvironmentAttributeSetResultsDatabaseMap.clear();
            processedEnvironmentAttributeSetResultsDatabaseList.clear();
            isProcessedEnvironmentAttributeSetDataConnected = false;
        }
    }

    public void disconnectAllDatabases() {
        disconnectedRawDatabases();
        disconnectAccumulatedDatabases();
    }


    public void accumulateAgentAttributeData() {
        if (isImmutable) throw new IllegalStateException("Cannot modify Results: object is immutable.");

        for (int i = 0; i < agentResults.getAttributeSetCollectionSetCount(); i++) {
            AttributeSetCollectionResults agentAttributeSetCollectionResults = agentResults.getAttributeSetCollectionResults(i);

            if (accumulatedAgentAttributeSetResultsDatabaseList.isEmpty()) {
                for (int j = 0; j < agentAttributeSetCollectionResults.getAttributeSetCount(); j++) {
                    String attributeName = agentAttributeSetCollectionResults.getAttributeSetResults(j).getAttributeSetName();
                    AttributeSetResultsDatabase newDatabase = AttributeSetResultsDatabaseFactory.createDatabase();
                    assert newDatabase != null;
                    newDatabase.connect();
                    accumulatedAgentAttributeSetResultsDatabaseMap.put(attributeName, newDatabase);
                    accumulatedAgentAttributeSetResultsDatabaseList.add(newDatabase);
                }
                isAccumulatedAgentAttributeSetDataConnected = true;
            }

            for (int j = 0; j < agentAttributeSetCollectionResults.getAttributeSetCount(); j++) {
                AttributeSetResults agentAttributeSetResults = agentAttributeSetCollectionResults.getAttributeSetResults(j);
                String attributeName = agentAttributeSetResults.getAttributeSetName();

                List<String> propertyNamesList = agentAttributeSetResults.getPropertyNamesList();
                for (String propertyName : propertyNamesList) {
                    List<?> accumulatedValues = accumulatedAgentAttributeSetResultsDatabaseMap.get(attributeName).getPropertyColumnAsList(propertyName);
                    List<?> valuesToBeProcessed = agentAttributeSetResults.getPropertyValues(propertyName);
                    List<?> newAccumulatedValues = accumulateAgentPropertyResults(attributeName, propertyName, accumulatedValues, valuesToBeProcessed);
                    accumulatedAgentAttributeSetResultsDatabaseMap.get(attributeName).setPropertyColumn(propertyName, (List<Object>) newAccumulatedValues);
                }

                List<String> preEventNamesList = agentAttributeSetResults.getPreEventNamesList();
                for (String preEventName : preEventNamesList) {
                    List<?> accumulatedValues = accumulatedAgentAttributeSetResultsDatabaseMap.get(attributeName).getPreEventColumnAsList(preEventName);
                    List<Boolean> valuesToBeProcessed = agentAttributeSetResults.getPreEventValues(preEventName);
                    List<?> newAccumulatedValues = accumulateAgentPreEventResults(attributeName, preEventName, accumulatedValues, valuesToBeProcessed);
                    accumulatedAgentAttributeSetResultsDatabaseMap.get(attributeName).setPreEventColumn(preEventName, (List<Object>) newAccumulatedValues);
                }

                List<String> postEventNamesList = agentAttributeSetResults.getPostEventNamesList();
                for (String postEventName : postEventNamesList) {
                    List<?> accumulatedValues= accumulatedAgentAttributeSetResultsDatabaseMap.get(attributeName).getPostEventColumnAsList(postEventName);
                    List<Boolean> valuesToBeProcessed = agentAttributeSetResults.getPostEventValues(postEventName);
                    List<?> newAccumulatedValues= accumulateAgentPostEventResults(attributeName, postEventName, accumulatedValues, valuesToBeProcessed);
                    accumulatedAgentAttributeSetResultsDatabaseMap.get(attributeName).setPostEventColumn(postEventName, (List<Object>) newAccumulatedValues);
                }
            }
        }
    }

    public void processModelAttributeData() {
        if (isImmutable) throw new IllegalStateException("Cannot modify Results: object is immutable.");

        AttributeSetCollectionResults environmentAttributeSetCollectionResults = environmentResults.getAttributeSetCollectionResults();

        if (processedEnvironmentAttributeSetResultsDatabaseList.isEmpty()) {
            for (int i = 0; i < environmentAttributeSetCollectionResults.getAttributeSetCount(); i++) {
                String attributeName = environmentAttributeSetCollectionResults.getAttributeSetResults(i).getAttributeSetName();
                AttributeSetResultsDatabase newDatabase = AttributeSetResultsDatabaseFactory.createDatabase();
                assert newDatabase != null;
                newDatabase.connect();
                processedEnvironmentAttributeSetResultsDatabaseMap.put(attributeName, newDatabase);
                accumulatedAgentAttributeSetResultsDatabaseList.add(newDatabase);
            }
            isProcessedEnvironmentAttributeSetDataConnected = true;
        }

        for (int i = 0; i < environmentAttributeSetCollectionResults.getAttributeSetCount(); i++) {
            AttributeSetResults environmentAttributeSetResults = environmentAttributeSetCollectionResults.getAttributeSetResults(i);
            String attributeName = environmentAttributeSetResults.getAttributeSetName();

            List<String> propertyNamesList = environmentAttributeSetResults.getPropertyNamesList();
            for (String propertyName : propertyNamesList) {
                List<?> valuesToBeProcessed = environmentAttributeSetResults.getPropertyValues(propertyName);
                List<?> newProcessedValues = processEnvironmentPropertyResults(attributeName, propertyName, valuesToBeProcessed);
                processedEnvironmentAttributeSetResultsDatabaseMap.get(attributeName).setPropertyColumn(propertyName, (List<Object>) newProcessedValues);
            }

            List<String> preEventNamesList = environmentAttributeSetResults.getPreEventNamesList();
            for (String preEventName : preEventNamesList) {
                List<?> valuesToBeProcessed = environmentAttributeSetResults.getPreEventValues(preEventName);
                List<?> newProcessedValues = processEnvironmentPreEventResults(attributeName, preEventName, valuesToBeProcessed);
                processedEnvironmentAttributeSetResultsDatabaseMap.get(attributeName).setPreEventColumn(preEventName, (List<Object>) newProcessedValues);
            }

            List<String> postEventNamesList = environmentAttributeSetResults.getPostEventNamesList();
            for (String postEventName : postEventNamesList) {
                List<?> valuesToBeProcessed = environmentAttributeSetResults.getPostEventValues(postEventName);
                List<?> newProcessedValues = processEnvironmentPostEventResults(attributeName, postEventName, valuesToBeProcessed);
                processedEnvironmentAttributeSetResultsDatabaseMap.get(attributeName).setPostEventColumn(postEventName, (List<Object>) newProcessedValues);
            }
        }
    }

    public void mergeWithBeforeAccumulation(Results otherResults) {
        if (isImmutable) throw new IllegalStateException("Cannot modify Results: object is immutable.");

        agentResults.mergeWith(otherResults.agentResults);
    }

    protected List<?> processEnvironmentPropertyResults(String attributeName, String propertyName, List<?> propertyValues) {
        // Default implementation: return the original property values without modification.
        return propertyValues;
    }

    protected List<?> processEnvironmentPreEventResults(String attributeName, String eventName, List<?> preEventValues) {
        // Default implementation: return the original pre-event values without modification.
        return preEventValues;
    }

    protected List<?> processEnvironmentPostEventResults(String attributeName, String eventName, List<?> postEventValues) {
        // Default implementation: return the original post-event values without modification.
        return postEventValues;
    }

    protected abstract List<?> accumulateAgentPropertyResults(
            String attributeSetName,
            String propertyName,
            List<?> accumulatedValues,
            List<?> valuesToBeProcessed
    );

    protected abstract List<?> accumulateAgentPreEventResults(
            String attributeSetName,
            String preEventName,
            List<?> accumulatedValues,
            List<Boolean> valuesToBeProcessed
    );

    protected abstract List<?> accumulateAgentPostEventResults(
            String attributeSetName,
            String postEventName,
            List<?> accumulatedValues,
            List<Boolean> valuesToBeProcessed
    );
}
