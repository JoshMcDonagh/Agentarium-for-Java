package integration.modelUsageTest2.attributes.postevents;

import agentarium.attributes.Event;
import agentarium.attributes.Property;

public class EatFood extends Event {

    public EatFood() {
        super("Eat Food", false);
    }

    @Override
    public boolean isTriggered() {
        Property<Double> hunger = (Property<Double>) getAssociatedModelElement().getAttributeSetCollection().get("food").getProperties().get("Hunger");
        return hunger.get() > 0.7;
    }

    @Override
    public void run() {
        Property<Double> hunger = (Property<Double>) getAssociatedModelElement().getAttributeSetCollection().get("food").getProperties().get("Hunger");
        hunger.set(hunger.get() - 0.5);
    }
}
