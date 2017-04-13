package generated;

import java.util.HashMap;
import java.util.Optional;

/**
* @author Maxime
*/
public abstract class AbstractState {
    protected String name;
    protected HashMap<String, AbstractTransition> transitionMap;

    public void addTransition(AbstractTransition t) {
        transitionMap.put(t.getEvent(), t);
    }

    /**
    * Check if the current event is mapped onto any child
    * @return true is the event is handled by at least one child. False otherwise
    * @param eventName
    */
    protected abstract boolean eventIsMappedOnChilds(String eventName);

    /**
    * Check if the event is mapped at this State
    * @param eventName
    * @return
    */
    protected boolean eventIsMapped(String eventName) {
        return transitionMap.containsKey(eventName);
    }

    protected abstract AbstractState handleEvent(String eventName);

    /**
    * Handle the event at this AbstactState isntance level
    * (used when no child can consume the transition)
    * @param eventName
    * @return
    */
    protected AbstractState handleEventAsOwn(String eventName) {
        Optional<AbstractTransition> t = Optional.ofNullable(transitionMap.get(eventName));
        if (t.isPresent()) {
            AbstractTransition transition = t.get();
            System.out.println("Exiting state : " + name);
            onExit();
            transition.execute();
            transition.getStateTo().onEntry();
            System.out.println("Entering state : " + name);
            return transition.getStateTo();
        }
        return this;
    }

    public void onExit() {
        System.out.println("onExit state : " + name);
    }

    public void onEntry() {
        System.out.println("onEntry state : " + name);
    }

    public String getName() {
        return name;
    }
}
