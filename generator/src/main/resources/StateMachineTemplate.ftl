package generated;

import java.util.LinkedList;
import java.util.Optional;

/**
 * @author Maxime
 */
public class StateMachine {
    private State initialState;
    private State currentState;
    private LinkedList<Event> eventQueue = new LinkedList<>();

    public StateMachine() {
        // Generated states
        <#list fsm.states as s>
        State ${s.name} = new State("${s.name}");
        </#list>

        //Generated Transition
        <#list fsm.transitions as t>
        ${t.stateFrom.name}.addTransition(new Transition(${t.stateFrom.name}, ${t.stateTo.name}, "${t.event}", "${t.action}"));
        </#list>

        initialState = ${fsm.initialState};
        currentState = ${fsm.initialState};

        System.out.println("StateMachine initialized");
        System.out.println("Initial State : " + initialState.getName());
    }

    public void activate() {
        while (!eventQueue.isEmpty()) {
            Event e = eventQueue.removeFirst();
            Optional<Transition> t = currentState.getTransition(e.getName());
            if (t.isPresent()) {
                System.out.println("Exiting state : "+currentState.getName());
                currentState.onExit();
                t.get().execute();
                t.get().getStateTo().onEntry();
                currentState = t.get().getStateTo();
                System.out.println("Entering state : "+currentState.getName());
            }
        }
    }

    public void submitEvent(String s) {
        Event e = new Event(s);
        eventQueue.add(e);
        activate();
    }
}
