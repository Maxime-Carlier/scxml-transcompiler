package unit_testing;

import generator.Generator;

import model.statics.State;
import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
        g.fromXML(cl.getResource("course_fsm.scxml").getPath());

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

        ArrayList<String> actual = new ArrayList<>();
        for (State s : g.getStates()) {
            actual.add(s.getName());
        }

        Collections.sort(expected);
        Collections.sort(actual);

        assertEquals(expected, actual);
    }
}
