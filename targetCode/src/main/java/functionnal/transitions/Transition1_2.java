package functionnal.transitions;

import functionnal.states.State1;
import functionnal.states.State2;
import model.AbstractTransition;

/**
 * @author Maxime
 */
public class Transition1_2 extends AbstractTransition{
    public Transition1_2() {
        stateFrom= State1.class;
        stateTo=State2.class;
    }

    @Override
    public void execute() {
        System.out.println("Executing transition");
    }
}
