import bridge.Bridge;
import generator.Generator;
import org.jdom2.JDOMException;

import java.io.IOException;

/**
 * @author Maxime
 */
public class Main {
    public static void main(String[] args) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Generator g = null;
        try {
            g = new Generator.GeneratorBuilder()
                    .withDefaultConfig()
                    .outputDirectory("StateMachine1")
                    .fromXML(cl.getResource("portal.scxml").getPath())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        g.generate();
        Bridge b = new Bridge(g.compile());

        b.submitEvent("close");
    }
}
