package generated;

/**
 * @author Maxime
 */
public class SendTransition extends AbstractTransition {
    public SendTransition(StateMachine context, AbstractState stateFrom, AbstractState stateTo, String event, String action) {
        super(context, stateFrom, stateTo, event, action);
    }

    @Override
    public void execute() {
        System.out.println("Executing action : "+action);
        context.send(action);
    }
}
