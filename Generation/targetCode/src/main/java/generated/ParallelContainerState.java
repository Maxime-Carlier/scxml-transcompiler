package generated;

import java.util.Objects;

/**
 * This class represent a state that is direcly under a parrallel state
 * @author Maxime
 */
public class ParallelContainerState extends AbstractState {
    private AbstractState currentState;

    public ParallelContainerState(String name) {
        super(name);
    }

    @Override
    protected boolean eventIsMappedOnChilds(String eventName) {
        if (currentState.eventIsMappedOnChilds(eventName) || currentState.eventIsMapped(eventName)) {
            return true;
        }
        return false;
    }

    @Override
    protected AbstractState handleEvent(String eventName) {
        if (eventIsMappedOnChilds(eventName)) {
            currentState.handleEvent(eventName);
            return this;
        } else {
            return handleEventAsOwn(eventName);
        }
    }

    protected void setCurrentState(AbstractState currentState) {
        Objects.requireNonNull(currentState);
        this.currentState = currentState;
    }
}
