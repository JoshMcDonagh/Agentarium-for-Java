package integration.modelUsageTest2.attributes;

import agentarium.attributes.AttributeSet;
import agentarium.attributes.AttributeSetCollection;
import agentarium.attributes.Events;
import agentarium.attributes.Properties;
import integration.modelUsageTest2.attributes.postevents.EatFood;
import integration.modelUsageTest2.attributes.properties.Hunger;

public class ModelAttributes {
    public static AttributeSetCollection getAgentAttributeSetCollection() {
        Events preEvents = new Events();

        Properties properties = new Properties();
        properties.add(new Hunger());

        Events postEvents = new Events();
        postEvents.add(new EatFood());

        AttributeSet foodAttributeSet = new AttributeSet("food", preEvents, properties, postEvents);

        AttributeSetCollection attributeSetCollection = new AttributeSetCollection();
        attributeSetCollection.add(foodAttributeSet);

        return attributeSetCollection;
    }

    public static AttributeSetCollection getEnvironmentAttributeSetCollection() {
        AttributeSetCollection attributeSetCollection = new AttributeSetCollection();
        return attributeSetCollection;
    }
}
