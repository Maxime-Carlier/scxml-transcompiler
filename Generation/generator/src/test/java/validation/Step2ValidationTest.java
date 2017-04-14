package validation;

import bridge.Bridge;
import generator.Generator;
import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Maxime
 */
public class Step2ValidationTest {
    Generator.GeneratorBuilder g;

    @Before
    public void setup() {
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
    public void Step2ValidationTest() throws JDOMException, IOException {
        ClassLoader cl = getClass().getClassLoader();
        g.fromXML(cl.getResource("Step2.scxml").getPath());
        Generator gen=g.build();
        gen.generate();

        Bridge b = new Bridge(gen.compile());
    }
}
