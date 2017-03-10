package generated;

import model.statics.Event;
import model.statics.State;
import model.statics.Transition;

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
        // Generated states
        <#list fsm.states as s>
            State ${s.name} = new State("${s.name}");
        </#list>

        //Generated Transition
        <#list fsm.transitions as t>
            ${t.stateFrom.name}.addTransition("${t.event}", new Transition(${t.stateFrom.name}, ${t.stateTo.name}, "${t.action}"));
        </#list>

        initialState = ${fsm.initialState};
        currentState = ${fsm.initialState};
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
