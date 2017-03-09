package model.statics;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author Maxime
 */

public class State {
    private String name;
    private HashMap<String, Transition> transitionMap;

    public State(String name) {
        this.name = name;
        this.transitionMap = new HashMap<>();
    }

    public void addTransition(Transition t) {
        transitionMap.put(t.getEvent(), t);
    }

    public Optional<Transition> getTransition(String s) {
        Optional<Transition> t = Optional.of(transitionMap.get(s));
        return t;
    }

    public void onExit() {
        System.out.println("onExit state : "+name);
    }

    public void onEntry() {
        System.out.println("onExit state : " + name);
    }

    public String getName() {
        return name;
    }
}