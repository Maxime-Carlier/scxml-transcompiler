package bridge;

import model.statics.Event;

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

    private Method submitEventMethod;

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
}
