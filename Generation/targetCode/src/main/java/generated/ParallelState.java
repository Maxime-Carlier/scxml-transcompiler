package generated;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Maxime
 */
public class ParallelState extends AbstractState {
    private ArrayList<AbstractState> states;

    public ParallelState(String name) {
        this.name = name;
        transitionMap = new HashMap<>();
        states = new ArrayList<>();
    }

    /**
     * Look at each child recursively to see if the event is mapped onto any child
     * @param eventName the name of the event
     * @return true if any child handle this event, false otherwise
     */
    @Override
    protected boolean eventIsMappedOnChilds(String eventName) {
        for (AbstractState s : states) {
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
            for (AbstractState s : states) {
                s.handleEvent(eventName);
            }
            return this;
        } else {
            return handleEventAsOwn(eventName);
        }
    }
}
