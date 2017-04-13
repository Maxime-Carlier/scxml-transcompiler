package generated;

/**
 * @author Maxime
 */
public class SimpleTransition extends AbstractTransition {
    public SimpleTransition(StateMachine context, AbstractState stateFrom, AbstractState stateTo, String event, String action) {
        super(context, stateFrom, stateTo, event, action);
    }

    @Override
    public void execute() {
        System.out.println("Executing action : "+action);
    }
}
