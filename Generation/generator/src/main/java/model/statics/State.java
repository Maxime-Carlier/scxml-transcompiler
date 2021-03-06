package model.statics;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author Maxime
 */

public class State {
    private String name;
    private String type;
    private HashMap<String, Transition> transitionMap;

    public State(String name, String transitionType) {
        this.name = name;
        this.type = transitionType;
        this.transitionMap = new HashMap<>();
    }

    public void addTransition(Transition t) {
        transitionMap.put(t.getEvent(), t);
    }

    public Optional<Transition> getTransition(String s) {
        Optional<Transition> t = Optional.ofNullable(transitionMap.get(s));
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

    public String getType() {
        return type;
    }
}