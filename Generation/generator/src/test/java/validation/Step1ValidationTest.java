package validation;

import bridge.Bridge;
import generator.Generator;
import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * @author Maxime
 */
public class Step1ValidationTest {

    Generator.GeneratorBuilder g;

    private boolean checkMark_startClosingMotor=false;
    private boolean checkMark_stopClosingMotor=false;
    private boolean checkMark_startOpeningMotor=false;
    private boolean checkMark_stopOpeningMotor=false;

    private Method validate_checkMark_startClosingMotor;
    private Method validate_checkMark_stopClosingMotor;
    private Method validate_checkMark_startOpeningMotor;
    private Method validate_checkMark_stopOpeningMotor;

    @Before
    public void setup() {
        // Setup methods reference for binding
        for (Method m : this.getClass().getMethods()) {
            switch (m.getName()) {
                case "validate_checkMark_startClosingMotor":
                    validate_checkMark_startClosingMotor=m;
                    break;
                case "validate_checkMark_stopClosingMotor":
                    validate_checkMark_stopClosingMotor=m;
                    break;
                case "validate_checkMark_startOpeningMotor":
                    validate_checkMark_startOpeningMotor=m;
                    break;
                case "validate_checkMark_stopOpeningMotor":
                    validate_checkMark_stopOpeningMotor = m;
                    break;
            }
        }
        Objects.requireNonNull(validate_checkMark_startClosingMotor);
        Objects.requireNonNull(validate_checkMark_stopClosingMotor);
        Objects.requireNonNull(validate_checkMark_startOpeningMotor);
        Objects.requireNonNull(validate_checkMark_stopOpeningMotor);

        // Set up instance
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
    public synchronized void Step1ValidationTest() throws JDOMException, IOException, InterruptedException {
        ClassLoader cl = getClass().getClassLoader();
        g.fromXML(cl.getResource("Step1.scxml").getPath());
        Generator gen=g.build();
        gen.generate();

        Bridge b = new Bridge(gen.compile());

        b.connectToEvent("startClosingMotor", this, validate_checkMark_startClosingMotor);
        b.connectToEvent("stopClosingMotor", this, validate_checkMark_stopClosingMotor);
        b.connectToEvent("startOpeningMotor", this, validate_checkMark_startOpeningMotor);
        b.connectToEvent("stopOpeningMotor", this, validate_checkMark_stopOpeningMotor);

        // Check initial state
        assertEquals("opened", b.getCurrentStateName());

        b.submitEvent("close");
        wait();
        assertEquals(true, checkMark_startClosingMotor);
        assertEquals("isClosing", b.getCurrentStateName());

        b.submitEvent("isClosed");
        wait();
        assertEquals(true, checkMark_stopClosingMotor);
        assertEquals("closed", b.getCurrentStateName());

        b.submitEvent("open");
        wait();
        assertEquals(true, checkMark_startOpeningMotor);
        assertEquals("isOpening", b.getCurrentStateName());

        b.submitEvent("isOpen");
        wait();
        assertEquals(true, checkMark_stopOpeningMotor);
        assertEquals("opened", b.getCurrentStateName());

        b.submitEvent("stop");
        // TODO final and terminated
    }

    public synchronized void validate_checkMark_startClosingMotor() {
        checkMark_startClosingMotor=true;
        notify();
    }

    public synchronized void validate_checkMark_stopClosingMotor() {
        checkMark_stopClosingMotor=true;
        notify();
    }

    public synchronized void validate_checkMark_startOpeningMotor() {
        checkMark_startOpeningMotor=true;
        notify();
    }

    public synchronized void validate_checkMark_stopOpeningMotor() {
        checkMark_stopOpeningMotor=true;
        notify();
    }
}
