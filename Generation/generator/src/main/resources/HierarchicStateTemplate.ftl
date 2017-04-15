package generated;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Maxime
 */
public class HierarchicState extends AbstractState {
    private AbstractState childState;

    public HierarchicState(String name) {
        super(name);
    }

    /**
     * return true if event is mapped on any child of childState or if it's mapped on
     * child state.
     * @param eventName the event to look for
     * @return true if the event is mapped onto any child od this. false otherwise
     */
    @Override
    protected boolean eventIsMappedOnChilds(String eventName) {
        if (childState.eventIsMappedOnChilds(eventName) || childState.eventIsMapped(eventName)) {
            return true;
        }
        return false;
    }

    @Override
    protected AbstractState handleEvent(String eventName) {
        if (eventIsMappedOnChilds(eventName)) {
            childState.handleEvent(eventName);
            return this;
        }
        else {
            return handleEventAsOwn(eventName);
        }
    }

    protected void setChildState(AbstractState childState) {
        Objects.requireNonNull(childState);
        this.childState = childState;
    }
}
