package integration.modelUsageTest3.attributes.agent.properties;

import agentarium.ModelElementAccessor;
import agentarium.attributes.Property;
import agentarium.environments.Environment;

public class SeenEnvTick extends Property<Integer> {
    private int seen = -1;
    public SeenEnvTick() { super("SeenEnvTick", true, Integer.TYPE); }

    @Override public Integer get() { return seen; }
    @Override public void set(Integer v) { seen = v; }

    @Override
    public void run() {
        ModelElementAccessor acc = getAssociatedModelElement().getModelElementAccessor();
        Environment env = acc.getEnvironment();  // goes through coordinator/cache
        if (env != null) {
            Integer envTick = (Integer) env.getAttributeSetCollection()
                    .get("climate").getProperties().get("EnvTick").get();
            seen = envTick;
        }
    }
}
