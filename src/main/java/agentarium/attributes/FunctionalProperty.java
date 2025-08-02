package agentarium.attributes;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A property whose behaviour is defined using functional interfaces.
 *
 * <p>This implementation allows dynamic configuration of property behaviour,
 * useful when working with external systems or when subclassing is not feasible
 * (e.g., in Python via JPype).</p>
 *
 * @param <T> the type of value this property holds
 */
public class FunctionalProperty<T> extends Property<T> {

    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private final Runnable runLogic;

    /**
     * Constructs a functional property with the given name, type, recording flag, and logic.
     *
     * @param name        the property name
     * @param isRecorded  whether the property is recorded
     * @param type        the value type of the property
     * @param getter      logic to retrieve the property value
     * @param setter      logic to update the property value
     * @param runLogic    logic to execute during the {@link #run()} call
     */
    public FunctionalProperty(
            String name,
            boolean isRecorded,
            Class<T> type,
            Supplier<T> getter,
            Consumer<T> setter,
            Runnable runLogic
    ) {
        super(name, isRecorded, type);
        this.getter = getter;
        this.setter = setter;
        this.runLogic = runLogic;
    }

    @Override
    public T get() {
        return getter.get();
    }

    @Override
    public void set(T value) {
        setter.accept(value);
    }

    @Override
    public void run() {
        runLogic.run();
    }

    @Override
    public FunctionalProperty<T> deepCopy() {
        return new FunctionalProperty<>(
                getName(),
                isRecorded(),
                getType(),
                getter,
                setter,
                runLogic
        );
    }
}
