import bridge.Bridge;
import generator.Generator;

import java.io.IOException;

/**
 * @author Maxime
 */
public class Main {
    //TODO transitive dependecy
    public static void main(String[] args) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Generator g = null;
        try {
            g = new Generator.GeneratorBuilder()
                    .withDefaultConfig()
                    .templatesDirectory(Generator.GeneratorBuilder.DEFAULT_RESOURCE_DIRECTORY)
                    .outputDirectory("StateMachine1")
                    .fromXML(cl.getResource("portal.scxml").getPath())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.generate();
        Bridge b = new Bridge(g.compile());

        b.submitEvent("close");
    }
}
