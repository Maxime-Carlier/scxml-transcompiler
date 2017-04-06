package bridge;

import model.statics.Event;
import model.statics.State;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * This class aims to provide a tangible interface to call method onto dynamically generated
 * StateMachine Class. At instantiation time, the Object parameter will be introspected to get
 * Method objects and encapsulate the reflexion logic.
 * @author Maxime
 */
public class Bridge {
    private final static String STATE_MACHINE_CLASS_NAME = "generated.StateMachine";
    private final static String SUBMIT_EVENT_METHOD_NAME = "submitEvent";
    private final static String GET_CURRENT_STATE_METHOD_NAME = "getCurrentState";
    private final static String GET_NAME_STATE_METHOD_NAME = "getName";
    private final static String CONNECT_TO_EVENT_METHOD_NAME = "connectToEvent";

    private Method submitEventMethod;
    private Method getCurrentStateMethod;
    private Method getName_StateMethod;
    private Method connectToEventMethod;

    private Object stateMachineInstance;


    public Bridge(Object o) {
        if(!o.getClass().getName().equals(STATE_MACHINE_CLASS_NAME))
            throw new IllegalArgumentException("Object underlying class is not " + STATE_MACHINE_CLASS_NAME);
        stateMachineInstance = o;

        for (Method m : o.getClass().getMethods()) {
            switch (m.getName()) {
                case SUBMIT_EVENT_METHOD_NAME:
                    submitEventMethod = m;
                    break;
                case GET_CURRENT_STATE_METHOD_NAME:
                    getCurrentStateMethod = m;
                    break;
                case CONNECT_TO_EVENT_METHOD_NAME:
                    connectToEventMethod = m;
                    break;
            }
        }
    }

    public void submitEvent(String s) {
        try {
            submitEventMethod.invoke(stateMachineInstance, s);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
    }

    public String getCurrentStateName() {
        try {
            Object o = getCurrentStateMethod.invoke(stateMachineInstance);
            // If the getName method as not yet been initialized do the lookup on the Object
            if (getName_StateMethod == null) {
                for (Method m : o.getClass().getMethods()) {
                    if (m.getName().equals(GET_NAME_STATE_METHOD_NAME)) {
                        getName_StateMethod = m;
                        break;
                    }
                }
            }
            String s = (String) getName_StateMethod.invoke(o);
            return s;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Current state name could not be obtained from StateMachine");
    }

    public void connectToEvent(String eventName, Object targetInstance, Method targetMethod) {
        try {
            connectToEventMethod.invoke(stateMachineInstance, eventName, targetInstance, targetMethod);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
