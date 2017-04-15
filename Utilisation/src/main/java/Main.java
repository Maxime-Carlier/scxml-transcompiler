import bridge.Bridge;
import generator.Generator;
import org.jdom2.JDOMException;

import java.io.IOException;

/**
 * @author Maxime
 */
public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        demo_step1();
        demo_connectToEvent();
        demo_step2();
    }

    public static void demo_step1() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Generator g = null;
        try {
            g = new Generator.GeneratorBuilder()
                    .withDefaultConfig()
                    .outputDirectory("StateMachineStep1")
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
        b.submitEvent("isClosed");
        b.submitEvent("open");
        b.submitEvent("isOpen");
        b.submitEvent("stop");
    }

    // Demonstrate the use of connectToEvent
    public static void demo_connectToEvent() throws NoSuchMethodException {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Generator g = null;
        try {
            g = new Generator.GeneratorBuilder()
                    .withDefaultConfig()
                    .outputDirectory("StateMachinePortal")
                    .fromXML(cl.getResource("portal.scxml").getPath())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        g.generate();
        Bridge b = new Bridge(g.compile());
        b.connectToEvent("startClosingMotor", null, Main.class.getMethod("sayHello"));
        b.submitEvent("close");
    }

    public static void demo_step2() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Generator g = null;
        try {
            g = new Generator.GeneratorBuilder()
                    .withDefaultConfig()
                    .outputDirectory("StateMachineStep2")
                    .fromXML(cl.getResource("Step2.scxml").getPath())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        g.generate();
        Bridge b = new Bridge(g.compile());
        b.submitEvent("close");
        b.submitEvent("isClosed");
    }

    public static void sayHello() {
        System.out.println("Hello World !");
    }
}
