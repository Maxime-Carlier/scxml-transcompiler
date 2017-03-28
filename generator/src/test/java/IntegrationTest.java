import generator.Generator;
import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author Maxime
 */
public class IntegrationTest {

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
    public void test355() throws JDOMException, IOException {
        ClassLoader cl = getClass().getClassLoader();
        g.fromXML(cl.getResource("test355.txml").getPath());
        Generator gen=g.build();
        gen.generate();
    }
}
