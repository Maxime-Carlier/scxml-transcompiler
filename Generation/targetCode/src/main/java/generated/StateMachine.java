package generated;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Maxime
 */
public class StateMachine {
    private AbstractState initialState;
    private AbstractState currentState;
    private LinkedList<Event> internalEventQueue;
    private LinkedList<Event> externalEventQueue;
    private HashMap<String, ArrayList<MethodExecutor>> subscribers;

    public StateMachine() {
        internalEventQueue = new LinkedList<>();
        externalEventQueue = new LinkedList<>();
        subscribers = new HashMap<>();

        // Generated states

        HierarchicState normalOperation = new HierarchicState("normalOperation");
        ParallelState parallelState = new ParallelState("parallelState" );
        HierarchicState LampControl = new HierarchicState("LampControl" );
        SimpleState LampOff = new SimpleState("LampOff" );
        HierarchicState LampBlinking = new HierarchicState("LampBlinking" );
        SimpleState LampBlinkOff = new SimpleState("LampBlinkOff" );
        SimpleState LampBlinkOn = new SimpleState("LampBlinkOn" );
        SimpleState LampControlInitial = new SimpleState("LampControlInitial" );
        HierarchicState DoorControl = new HierarchicState("DoorControl" );
        SimpleState DoorControlInitial = new SimpleState("DoorControlInitial" );
        SimpleState opened = new SimpleState("opened" );
        SimpleState isClosing = new SimpleState("isClosing" );
        SimpleState isOpening = new SimpleState("isOpening" );
        SimpleState closed = new SimpleState("closed" );
        SimpleState Final_2 = new SimpleState("Final_2" );

        // Set Hierarchie
        normalOperation.setChildState(parallelState);
        parallelState.addChildState(DoorControl);
        parallelState.addChildState(LampControl);

        DoorControl.setChildState(opened);
        LampControl.setChildState(LampOff);
        LampBlinking.setChildState(LampBlinkOff);

        // Generated Transition
            LampOff.addTransition(new SimpleTransition(this, LampOff, LampBlinking, "startBlinking"));
            LampBlinkOff.addTransition(new SimpleTransition(this, LampBlinkOff, LampBlinkOn, "timeout"));
            LampBlinkOn.addTransition(new SimpleTransition(this, LampBlinkOn, LampBlinkOff, "timeout"));
            LampBlinking.addTransition(new SimpleTransition(this, LampBlinking, LampOff, "stopBlinking"));
            LampControlInitial.addTransition(new SimpleTransition(this, LampControlInitial, LampOff, ""));
            DoorControlInitial.addTransition(new SimpleTransition(this, DoorControlInitial, opened, ""));
            opened.addTransition(new SendTransition(this, opened, isClosing, "close" , "startBlinking" ));
            isClosing.addTransition(new SendTransition(this, isClosing, closed, "isClosed" , "stopBlinking" ));
            isClosing.addTransition(new SimpleTransition(this, isClosing, isOpening, "obstacleDetected"));
            isOpening.addTransition(new SendTransition(this, isOpening, opened, "osOpened" , "stopBlinking" ));
            closed.addTransition(new SendTransition(this, closed, isOpening, "open" , "startBlinking" ));
            normalOperation.addTransition(new SimpleTransition(this, normalOperation, Final_2, "stop"));

        initialState = normalOperation;
        currentState = normalOperation;

        System.out.println("StateMachine initialized" );
        System.out.println("Initial State : " + initialState.getName());
        System.out.println("");

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
        while (!(internalEventQueue.isEmpty() && externalEventQueue.isEmpty())) {
            // Handle internal events first
            while (!internalEventQueue.isEmpty()) {
                handleEvent(internalEventQueue.removeFirst());
            }
            // Handle the next external event
            if (!externalEventQueue.isEmpty()) {
                handleEvent(externalEventQueue.removeFirst());
            }
        }
        System.out.println("");
    }

    /**
    * Internal method to handle event
    */
    private void handleEvent(Event e) {
        currentState=currentState.handleEvent(e.getName());
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

    public AbstractState getCurrentState() {
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
    protected void raise(String s) {
        Event e = new Event(s);
        externalEventQueue.add(e);
        activate();
    }

    /**
    * Submit an event to the external queue of the state machine and call asynchronously every method that were
    * subscribed to this event
    * @param s The event to be sent
    */
    protected void send(String s) {
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