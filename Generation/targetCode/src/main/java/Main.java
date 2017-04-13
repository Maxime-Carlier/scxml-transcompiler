import generated.StateMachine;

/**
 * @author Maxime
 */
public class Main {
    public static void main(String[] args) {
        StateMachine sm = new StateMachine();
        sm.submitEvent("close");
    }
}
