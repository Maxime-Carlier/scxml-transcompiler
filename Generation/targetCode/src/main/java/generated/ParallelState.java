package generated;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Maxime
 */
public class ParallelState extends AbstractState {
    private ArrayList<HierarchicState> childrenStates;

    public ParallelState(String name) {
        super(name);
        childrenStates = new ArrayList<>();
    }


    /**
     * Look at each child recursively to see if the event is mapped onto any child
     * @param eventName the name of the event
     * @return true if any child handle this event, false otherwise
     */
    @Override
    protected boolean eventIsMappedOnChilds(String eventName) {
        for (AbstractState s : childrenStates) {
            if (s.eventIsMappedOnChilds(eventName)|| s.eventIsMapped(eventName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * if any child handle this event, the event is delegated, otherwise it is handled here
     * @param eventName the name of the event
     * @return the new State of the StateMachine
     */
    @Override
    protected AbstractState handleEvent(String eventName) {
        if (eventIsMappedOnChilds(eventName)) {
            for (AbstractState s : childrenStates) {
                s.handleEvent(eventName);
            }
            return this;
        } else {
            return handleEventAsOwn(eventName);
        }
    }

    public void addChildState(HierarchicState s) {
        Objects.requireNonNull(s);
        childrenStates.add(s);
    }
}
