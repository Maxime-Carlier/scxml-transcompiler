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

    // Static association 2d array to link template input name to class output name
    private final static String[][] TEMPLATES_IO_ASSOC = {
            {"StateMachineTemplate.ftl", "StateMachine.java"},
            {"EventTemplate.ftl","Event.java"},
            {"RunnerTemplate.ftl","Runner.java"},
            {"StateTemplate.ftl","State.java"},
            {"TransitionTemplate.ftl", "Transition.java"}
    };

    private final Configuration cfg;
    private final String outputDirectory;
    private Map<String, Object> root;

    private Generator(Configuration cfg, String outputDirectory) {
        this.cfg=cfg;
        this.outputDirectory = outputDirectory;
    }

    public void testDataModel() {
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

    public void generate() {
        try {
            for (String[] pairing : TEMPLATES_IO_ASSOC) {
                Template t = cfg.getTemplate(pairing[0]);
                Writer out = new FileWriter(outputDirectory + pairing[1]);
                t.process(root, out);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    public static class GeneratorBuilder {
        public static final String DEFAULT_OUTPUT_DIRECTORY = "production/src/main/java/generated/";
        public static final String DEFAULT_RESOURCE_DIRECTORY = "generator/src/main/resources/";

        private Configuration cfg;
        private String outputDirectory;

        public GeneratorBuilder withDefaultConfig() {
            cfg = new Configuration(Configuration.VERSION_2_3_25);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            return this;
        }

        public GeneratorBuilder templatesDirectory(String directory) throws IOException {
            cfg.setDirectoryForTemplateLoading(new File(directory));
            return this;
        }

        public GeneratorBuilder outputDirectory(String directory) {
            outputDirectory = directory;
            return this;
        }

        public Generator build() {
            return new Generator(cfg, outputDirectory);
        }
    }
}
