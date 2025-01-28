package agentarium.attributes;

import java.util.List;

public class Properties extends Attributes {
    public <T> void add(Property<T> property) {
        addAttribute(property);
    }

    public void add(List<Property<?>> properties) {
        for (Property<?> property : properties)
            add(property);
    }

    public Property<?> get(String name) {
        return (Property<?>) getAttribute(name);
    }

    public Property<?> get(int index) {
        return (Property<?>) getAttribute(index);
    }

    @Override
    public void run() {
        for (int i = 0; i < size(); i++)
            get(i).run();
    }
}
