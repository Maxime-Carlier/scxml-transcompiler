package model;

/**
 * @author Maxime
 */
public class Transition {
    private State stateFrom;
    private State stateTo;
    private String event;

    public Transition(State stateFrom, State stateTo, String event) {
        this.stateFrom = stateFrom;
        this.stateTo = stateTo;
        this.event = event;
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

    public void execute() {
        System.out.println("Executing transition : "+event);
    }
}
