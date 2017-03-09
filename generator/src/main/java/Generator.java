import freemarker.template.*;
import model.statics.State;
import model.statics.Transition;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
        cfg = new Configuration(Configuration.VERSION_2_3_25);
        try {
            cfg.setDirectoryForTemplateLoading(new File("generator/src/main/resources"));
            buildDataModel();
            Template stateMachineTemplate = cfg.getTemplate("StateMachineTemplate.ftl");
            Writer out = new OutputStreamWriter(System.out);
            stateMachineTemplate.process(root, out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }


    public static void buildDataModel() {
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);

        root = new HashMap<>();
        Map<String, Object> fsm = new HashMap<>();
        List<State> states = new ArrayList<>();
        List<Transition> transitions = new ArrayList<>();

        State state1 = new State("state1");
        State state2 = new State("state2");
        states.add(state1);
        states.add(state2);

        Transition transition1_2 = new Transition(state1, state2, "Start", "b1");
        transitions.add(transition1_2);

        state1.addTransition(transition1_2);

        fsm.put("states", states);
        fsm.put("transitions", transitions);
        fsm.put("initialState", state1);
        root.put("fsm", fsm);
    }
}
