package generated;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

/**
 * @author Maxime
 */
public class StateMachine {
    private State initialState;
    private State currentState;
    private LinkedList<Event> internalEventQueue;
    private LinkedList<Event> externalEventQueue;
    private HashMap<String, ArrayList<MethodExecutor>> subscribers;

    public StateMachine() {
        internalEventQueue = new LinkedList<>();
        externalEventQueue = new LinkedList<>();
        subscribers = new HashMap<>();

        // Generated states
        <#list fsm.states as s>
        State ${s.name} = new State("${s.name}");
        </#list>

        // Generated Transition
        <#list fsm.transitions as t>
        ${t.stateFrom.name}.addTransition(new Transition(${t.stateFrom.name}, ${t.stateTo.name}, "${t.event}", "${t.action}"));
        </#list>

        initialState = ${fsm.initialState};
        currentState = ${fsm.initialState};

        System.out.println("StateMachine initialized");
        System.out.println("Initial State : " + initialState.getName());
    }

    /**
    * Handle event until both internal and external event queues are empty.
    * The handling is priority based. Internal first then if no more interval are present, handle the next external.
    * Before executing the following external (if it exist) make sure the internal queue is empty (it could have been
    * filled during the handling of an external event). In the event that an internal event is present in the internal
    * queue before the handling of the second external event, the internal queue must be emptied.
    */
    public void activate() {
        // Exit when statemachine is once again stable
        while (! (internalEventQueue.isEmpty() && externalEventQueue.isEmpty()) ) {
            // Handle internal events first
            while (!internalEventQueue.isEmpty()) {
                handleEvent(internalEventQueue.removeFirst());
            }
            // Handle the next external event
            if (!externalEventQueue.isEmpty()) {
                handleEvent(externalEventQueue.removeFirst());
            }
        }
    }

    /**
    * Internal method to handle event
    */
    private void handleEvent(Event e) {
        Optional<Transition> t = currentState.getTransition(e.getName());
        if (t.isPresent()) {
            System.out.println("Exiting state : "+currentState.getName());
            currentState.onExit();
            t.get().execute();
            send(t.get().getAction());
            t.get().getStateTo().onEntry();
            currentState = t.get().getStateTo();
            System.out.println("Entering state : "+currentState.getName());
        }
    }

    /**
    * Submit an event to the internal queue of the state machine
    * @param s the event to be submitted
    */
    public void submitEvent(String s) {
        Event e = new Event(s);
        internalEventQueue.add(e);
        activate();
    }

    public State getCurrentState() {
        return currentState;
    }

    /**
    * subscribe the Method targetMethod of instance instanceListener to the event eventName
    * @param eventName The event that trigger Method call
    * @param targetInstance The Object holding the target method
    * @param targetMethod The method to be called upon event raise
    */
    public void connectToEvent(String eventName, Object targetInstance, Method targetMethod) {
        if (!subscribers.containsKey(eventName)) {
            ArrayList<MethodExecutor> me = new ArrayList<>();
            me.add(new MethodExecutor(targetMethod, targetInstance));
            subscribers.put(eventName, me);
        } else {
            ArrayList<MethodExecutor> me = subscribers.get(eventName);
            me.add(new MethodExecutor(targetMethod, targetInstance));
        }
    }

    /**
    * Submit an event in the external queue of the state machine
    * @param s the event to be submitted
    */
    private void raise(String s) {
        Event e = new Event(s);
        externalEventQueue.add(e);
        activate();
    }

    /**
    * Submit an event to the external queue of the state machine and call asynchronously every method that were
    * subscribed to this event
    * @param s The event to be sent
    */
    private void send(String s) {
        Event e = new Event(s);
        externalEventQueue.add(e);
        ArrayList<MethodExecutor> meArray = subscribers.get(s);
        if (meArray != null) {
            for (MethodExecutor me : meArray) {
                // We don't want to manage the threads. Action executed in them are under user responsibility
                new Thread(() -> {
                    me.execute();
                }).start();
            }
        }
        activate();
    }
}
