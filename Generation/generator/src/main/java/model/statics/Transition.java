package model.statics;

import java.util.Objects;

/**
 * @author Maxime
 */
public class Transition implements Comparable<Transition> {
    private State stateFrom;
    private State stateTo;
    private String event;
    private String action;
    private String type;

    public Transition(State stateFrom, State stateTo, String event, String action, String type) {
        Objects.requireNonNull(stateFrom, "stateFrom cannot be null");
        Objects.requireNonNull(stateFrom, "stateTo cannot be null");
        Objects.requireNonNull(type);
        this.stateFrom = stateFrom;
        this.stateTo = stateTo;
        this.event = event;
        this.action = action;
        this.type = type;
    }

    public State getStateFrom() {
        return stateFrom;
    }

    public State getStateTo() {
        return stateTo;
    }

    public String getEvent() {
        return event;
    }

    public String getAction() {
        return action;
    }

    public String getType() {
        return type;
    }

    @Override
    public int compareTo(Transition o) {
        StringBuilder thisStringRepresentation = new StringBuilder();
        thisStringRepresentation.append(stateFrom.getName());
        thisStringRepresentation.append(stateTo.getName());
        thisStringRepresentation.append(event);
        thisStringRepresentation.append(action);
        thisStringRepresentation.append(type);

        StringBuilder otherStringRepresentation = new StringBuilder();
        otherStringRepresentation.append(stateFrom.getName());
        otherStringRepresentation.append(stateTo.getName());
        otherStringRepresentation.append(event);
        otherStringRepresentation.append(action);
        otherStringRepresentation.append(type);

        return thisStringRepresentation.toString().compareTo(otherStringRepresentation.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(stateFrom.getName());
        builder.append(", ");
        builder.append(stateTo.getName());
        builder.append(", ");
        builder.append(event);
        builder.append(", ");
        builder.append(action);
        builder.append(", ");
        builder.append(type);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        Transition t = (Transition) o;

        // Action can be null, we will circumvent this by doing that for now
            return stateFrom.getName().equals(t.getStateFrom().getName()) &&
                    stateTo.getName().equals(t.getStateTo().getName()) &&
                    event.equals(t.getEvent()) &&
                    ( (action == t.getAction()) || (action!=null && action.equals(t.getAction())) ) &&
                    type.equals(t.getType());
    }
}
