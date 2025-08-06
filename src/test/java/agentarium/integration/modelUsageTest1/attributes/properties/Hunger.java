package agentarium.integration.modelUsageTest1.attributes.properties;

import agentarium.attributes.Property;

public class Hunger extends Property<Double> {

    private double hungerLevel = 1.0;

    public Hunger() {
        super("Hunger", true, Double.TYPE);
    }

    @Override
    public void set(Double aDouble) {
        hungerLevel = aDouble;
        normaliseLevel();
    }

    @Override
    public Double get() {
        return hungerLevel;
    }

    @Override
    public void run() {
        hungerLevel = hungerLevel - 0.1;
        normaliseLevel();
    }

    private void normaliseLevel() {
        if (hungerLevel > 1.0)
            hungerLevel = 1.0;
        else if (hungerLevel < 0.0)
            hungerLevel = 0.0;
    }
}
