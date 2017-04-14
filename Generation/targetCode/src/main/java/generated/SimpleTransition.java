package generated;

/**
 * @author Maxime
 */
public class SimpleTransition extends AbstractTransition {
    public SimpleTransition(StateMachine context, AbstractState stateFrom, AbstractState stateTo, String event) {
        super(context, stateFrom, stateTo, event, null);
    }

    @Override
    public void execute() {
        System.out.println("Executing transition");
    }
}
