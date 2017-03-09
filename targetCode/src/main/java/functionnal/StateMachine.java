package functionnal;

import functionnal.states.State1;
import model.AbstractEvent;
import model.AbstractState;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Maxime
 */
public class StateMachine {
    // Initial State, will be setup here at generation
    private Class<? extends AbstractState> initialState;

    // Event Queue
    private Queue<AbstractEvent> eventQueue;

    public StateMachine() {
        eventQueue = new LinkedList<AbstractEvent>();
        initialState=State1.class;
    }

    public void activate() {

    }
}
