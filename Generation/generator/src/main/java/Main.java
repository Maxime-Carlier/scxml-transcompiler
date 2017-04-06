import bridge.Bridge;
import generator.Generator;
import org.jdom2.JDOMException;

import java.io.IOException;

/**
 * @author Maxime
 */
public class Main {
    public static void main(String[] args) {
        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            Generator g = new Generator.GeneratorBuilder()
                    .withDefaultConfig()
                    .templatesDirectory(Generator.GeneratorBuilder.DEFAULT_RESOURCE_DIRECTORY)
                    .outputDirectory("StateMachine1")
                    .fromXML(cl.getResource("stopwatch_states.scxml").getPath())
                    .build();
            g.generate();
            Bridge b = new Bridge(g.compile());
            b.submitEvent("b1");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }

    }
}
