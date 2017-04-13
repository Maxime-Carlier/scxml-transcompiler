package generated;

import java.util.Objects;

/**
 * @author Maxime
 */
public abstract class AbstractTransition {
    protected AbstractState stateFrom;
    protected AbstractState stateTo;
    protected String event;
    protected String action;
    protected StateMachine context;

    public AbstractTransition(StateMachine context, AbstractState stateFrom, AbstractState stateTo, String event, String action) {
    Objects.requireNonNull(stateFrom, "stateFrom cannot be null");
    Objects.requireNonNull(stateFrom, "stateTo cannot be null");
        this.stateFrom = stateFrom;
        this.stateTo = stateTo;
        this.event = event;
        this.action = action;
    }

    public AbstractState getStateFrom() {
        return stateFrom;
    }

    public AbstractState getStateTo() {
        return stateTo;
    }

    public String getEvent() {
        return event;
    }

    public String getAction() {
        return action;
    }

    public abstract void execute();
}
