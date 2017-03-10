import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import model.statics.State;
import model.statics.Transition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maxime
 */
public class Generator {

    private static Configuration cfg;
    private static Map<String, Object> root;

    public static void main(String[] args) {
        buildConfiguration();
        buildDataModel();
        generateTemplate();
    }


    public static void buildDataModel() {
        root = new HashMap<>();
        Map<String, Object> fsm = new HashMap<>();
        List<State> states = new ArrayList<>();
        List<Transition> transitions = new ArrayList<>();

        State state1 = new State("state1");
        State state2 = new State("state2");
        states.add(state1);
        states.add(state2);

        Transition transition1_2 = new Transition(state1, state2, "b1", "Start");
        transitions.add(transition1_2);

        state1.addTransition(transition1_2);

        fsm.put("states", states);
        fsm.put("transitions", transitions);
        fsm.put("initialState", state1.getName());
        root.put("fsm", fsm);
    }

    public static void buildConfiguration() {
        cfg = new Configuration(Configuration.VERSION_2_3_25);
        try {
            cfg.setDirectoryForTemplateLoading(new File("generator/src/main/resources"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateTemplate() {
        try {
            Template stateMachineTemplate = cfg.getTemplate("StateMachineTemplate.ftl");
            Template eventTemplate = cfg.getTemplate("EventTemplate.ftl");
            Template runnerTemplate = cfg.getTemplate("RunnerTemplate.ftl");
            Template stateTemplate = cfg.getTemplate("StateTemplate.ftl");
            Template transitionTemplate = cfg.getTemplate("TransitionTemplate.ftl");

            Writer out;

            out = new FileWriter("production/src/main/java/generated/StateMachine.java");
            stateMachineTemplate.process(root, out);
            out.close();

            out = new FileWriter("production/src/main/java/generated/Event.java");
            eventTemplate.process(root, out);
            out.close();

            out = new FileWriter("production/src/main/java/generated/Runner.java");
            runnerTemplate.process(root, out);
            out.close();

            out = new FileWriter("production/src/main/java/generated/State.java");
            stateTemplate.process(root, out);
            out.close();

            out = new FileWriter("production/src/main/java/generated/Transition.java");
            transitionTemplate.process(root, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
