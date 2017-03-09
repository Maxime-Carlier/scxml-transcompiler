package generated;

import model.Event;
import model.State;
import model.Transition;

import java.util.LinkedList;
import java.util.Optional;

/**
 * @author Maxime
 */
public class StateMachine {
    private static State initialState;
    private static State currentState;
    private static LinkedList<Event> eventQueue = new LinkedList<>();

    public static void build() {
        // Generate state here
        State state1 = new State("State1");
        State state2 = new State("State2");

        // Then generate transition here
        state1.addTransition("b1", new Transition(state1, state2, "Start"));

        // Then set the initial node
        initialState = state1;
        currentState = state1;
    }

    public static void activate() {
        while (!eventQueue.isEmpty()) {
            Event e = eventQueue.removeFirst();
            Optional<Transition> t = currentState.getTransition(e.getName());
            if (t.isPresent()) {
                System.out.println("Exiting state : "+currentState.getName());
                currentState.onExit();
                t.get().execute();
                t.get().getStateTo().onEntry();
                System.out.println("Entering state : "+currentState.getName());
                currentState = t.get().getStateTo();
            }
        }
    }

    public static void submitEvent(Event e) {
        eventQueue.add(e);
        activate();
    }
}
