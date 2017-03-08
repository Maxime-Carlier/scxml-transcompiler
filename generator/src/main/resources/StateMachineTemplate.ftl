package functionnal;

//TODO Import every state required here
//import functionnal.states.State1;

import model.AbstractState;

/**
 * Generated container class for FSM ${Fsm.name}
 */
public class ${Fsm.name} {
    // Initial State, will be setup here at generation
    private Class<? extends AbstractState> initialState;

    public StateMachine() {
        // Template::Param::initialState
        initialState=${Fsm.States.initialStateName}.class;
    }
}
