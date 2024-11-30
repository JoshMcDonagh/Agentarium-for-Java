package models.modelattributes;

import models.modelattributes.event.ModelEvents;
import models.modelattributes.property.ModelProperties;

public class ModelAttributes {
    private static int modelAttributeCount = 0;

    private final ModelProperties properties = new ModelProperties();
    private final ModelEvents preEvents = new ModelEvents();
    private final ModelEvents postEvents = new ModelEvents();

    private final String name;

    public ModelAttributes(final String name) {
        this.name = name;
        modelAttributeCount++;
    }

    public ModelAttributes() {
        this("Model Attribute " + modelAttributeCount);
    }

    public String name() {
        return name;
    }

    public ModelProperties getProperties() {
        return properties;
    }

    public ModelEvents getPreEvents() {
        return preEvents;
    }

    public ModelEvents getPostEvents() {
        return postEvents;
    }

    public void run() {
        preEvents.run();
        properties.run();
        postEvents.run();
    }
}
