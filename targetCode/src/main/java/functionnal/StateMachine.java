package functionnal;

import functionnal.states.State1;
import model.AbstractState;

/**
 * @author Maxime
 */
public class StateMachine {
    // Initial State, will be setup here at generation
    private Class<? extends AbstractState> initialState;

    public StateMachine() {
        // @Template::Param::initialState
        initialState=State1.class;
    }
}
