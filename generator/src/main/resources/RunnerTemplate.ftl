package generated;

public class Runner {
    private static StateMachine sm;

    public static void main(String[] args) {
        StateMachine.build();
        sm.submitEvent(new Event("b1"));
    }
}
