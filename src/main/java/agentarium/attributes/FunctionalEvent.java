package agentarium.attributes;

import java.util.function.BooleanSupplier;

/**
 * An event whose logic is defined via functional interfaces.
 *
 * <p>Useful for defining simple event logic without requiring full subclassing,
 * particularly when integrating with languages like Python.</p>
 */
public class FunctionalEvent extends Event {

    private final Runnable runLogic;
    private final BooleanSupplier triggerLogic;

    /**
     * Constructs a functional event with the given name, recording flag, and behaviour.
     *
     * @param name the name of the event
     * @param isRecorded whether to record the event in output
     * @param runLogic logic to execute when {@link #run()} is called
     * @param triggerLogic logic to evaluate when {@link #isTriggered()} is called
     */
    public FunctionalEvent(String name, boolean isRecorded, Runnable runLogic, BooleanSupplier triggerLogic) {
        super(name, isRecorded);
        this.runLogic = runLogic;
        this.triggerLogic = triggerLogic;
    }

    @Override
    public boolean isTriggered() {
        return triggerLogic.getAsBoolean();
    }

    @Override
    public void run() {
        runLogic.run();
    }

    @Override
    public FunctionalEvent deepCopy() {
        return new FunctionalEvent(getName(), isRecorded(), runLogic, triggerLogic);
    }
}
