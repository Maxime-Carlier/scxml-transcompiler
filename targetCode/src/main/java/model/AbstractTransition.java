package model;

/**
 * @author Maxime
 */
public abstract class AbstractTransition {
    protected Class<?extends AbstractState> stateFrom;
    protected Class<? extends AbstractState> stateTo;

    public abstract void execute();
}
