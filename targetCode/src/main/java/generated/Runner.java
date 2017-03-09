package generated;

import model.Event;

/**
 * @author Maxime
 */
public class Runner {
    private static StateMachine sm;

    public static void main(String[] args) {
        StateMachine.build();
        sm.submitEvent(new Event("b1"));
    }
}
