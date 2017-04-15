package generated;

import java.util.HashMap;

/**
 * @author Maxime
 */

public class SimpleState extends AbstractState{

    public SimpleState(String name) {
        super(name);
    }

    @Override
    protected boolean eventIsMappedOnChilds(String eventName) {
        return false;
    }

    @Override
    protected AbstractState handleEvent(String eventName) {
        return handleEventAsOwn(eventName);
    }
}