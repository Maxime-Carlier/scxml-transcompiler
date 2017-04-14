package unitary;

import generator.Generator;

import model.statics.State;
import model.statics.Transition;
import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * @author Maxime
 */
public class ParsingTest {

    Generator.GeneratorBuilder g;

    @Before
    public void setup() {
        try {
            g = new Generator.GeneratorBuilder()
                    .withDefaultConfig()
                    .templatesDirectory(Generator.GeneratorBuilder.DEFAULT_RESOURCE_DIRECTORY)
                    .outputDirectory("StateMachine1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void stateNameParsingTest1() throws JDOMException, IOException {
        ClassLoader cl = getClass().getClassLoader();
        g.fromXML(cl.getResource("Step2.scxml" ).getPath());

        ArrayList<String> expected = new ArrayList<>();
        expected.add("normalOperation");
        expected.add("LampControl");
        expected.add("opened");
        expected.add("isClosing");
        expected.add("closed");
        expected.add("isOpening");
        expected.add("LampOff");
        expected.add("LampBlinking");
        expected.add("LampBlinkOff");
        expected.add("LampBlinkOn");
        expected.add("DoorControl");
        expected.add("DoorControlInitial");
        expected.add("LampControlInitial");
        expected.add("parallelState");
        expected.add("Final_2");


        ArrayList<String> actual = new ArrayList<>();
        for (State s : g.getStates()) {
            actual.add(s.getName());
        }

        Collections.sort(expected);
        Collections.sort(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void stateNameParsingTest2() throws JDOMException, IOException {
        ClassLoader cl = getClass().getClassLoader();
        g.fromXML(cl.getResource("stopwatch_states.scxml").getPath());

        ArrayList<String> expected = new ArrayList<>();
        expected.add("Count");
        expected.add("Counting");
        expected.add("Paused");
        expected.add("Save");
        expected.add("Reseting");
        expected.add("ReadyToSave");
        expected.add("Saving");
        expected.add("Stopped");
        expected.add("Idle");
        expected.add("CountAndSave");
        expected.add("Final_1");

        ArrayList<String> actual = new ArrayList<>();
        for (State s : g.getStates()) {
            actual.add(s.getName());
        }

        Collections.sort(expected);
        Collections.sort(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void sendActionIsParsed() throws JDOMException, IOException {
        ClassLoader cl = getClass().getClassLoader();
        g.fromXML(cl.getResource("Step1.scxml").getPath());

        State opened = null, isClosing = null, closed = null, isOpening = null, final_1 = null;
        for (State s : g.getStates()) {
            switch (s.getName()) {
                case "opened":
                    opened = s;
                    break;
                case "isClosing":
                    isClosing = s;
                    break;
                case "closed":
                    closed = s;
                    break;
                case "isOpening":
                    isOpening = s;
                    break;
                case "Final_1":
                    final_1 = s;
                    break;
            }
        }
        Objects.requireNonNull(opened);
        Objects.requireNonNull(isClosing);
        Objects.requireNonNull(closed);
        Objects.requireNonNull(isOpening);
        Objects.requireNonNull(final_1);


        ArrayList<Transition> expected = new ArrayList<>();
        expected.add(new Transition(opened, isClosing, "close", "startClosingMotor", "send"));
        expected.add(new Transition(opened, final_1, "stop", null, "simple"));
        expected.add(new Transition(isClosing, closed, "isClosed", "stopClosingMotor", "send"));
        expected.add(new Transition(closed, isOpening, "open", "startOpeningMotor", "send"));
        expected.add(new Transition(closed, final_1, "stop", null, "simple"));
        expected.add(new Transition(isOpening, opened, "isOpen", "stopOpeningMotor", "send"));

        ArrayList<Transition> actual = new ArrayList<>();
        for (Transition t : g.getTransitions()) {
            actual.add(t);
        }

        Collections.sort(expected);
        Collections.sort(actual);

        assertEquals(expected, actual);
    }
}
