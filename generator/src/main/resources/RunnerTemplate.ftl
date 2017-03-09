package model.statics;

import generatedStateMachine;


public class Runner {
    private static StateMachine sm;

    public static void main(String[] args) {
        StateMachine.build();
        sm.submitEvent(new Event("b1"));
    }
}
