package generated;

import java.util.Objects;

/**
 * @author Maxime
 */
public class Transition {
    private State stateFrom;
    private State stateTo;
    private String event;
    private String action;

    public Transition(State stateFrom, State stateTo, String event, String action) {
    Objects.requireNonNull(stateFrom, "stateFrom cannot be null");
    Objects.requireNonNull(stateFrom, "stateTo cannot be null");
        this.stateFrom = stateFrom;
        this.stateTo = stateTo;
        this.event = event;
        this.action = action;
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

    public void execute() {
        System.out.println("Executing action : "+action);
    }
}