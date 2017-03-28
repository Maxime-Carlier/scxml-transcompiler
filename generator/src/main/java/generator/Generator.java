package generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import model.statics.State;
import model.statics.Transition;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
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

    private final static String SM_QUALIFIED_CLASSNAME = "generated.StateMachine";

    // This will hold the final path of all generated class
    //It is needed by the compile() method to compile generated sources
    private String[] outputPath;

    private final Configuration cfg;
    private final String outputDirectory;
    private Map<String, Object> root;

    private Generator(Configuration cfg, String outputDirectory, HashMap<String, Object> dataModel) {
        this.cfg=cfg;
        this.outputDirectory = outputDirectory;
        this.root = dataModel;

        outputPath = new String[TEMPLATES_IO_ASSOC.length];
        for(int i=0;i<outputPath.length;i++) {
            outputPath[i] = outputDirectory + TEMPLATES_IO_ASSOC[i][1];
        }
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
        compile();
    }

    private void compile() {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            compiler.run(null, null, null, outputPath);
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(outputDirectory).toURI().toURL()});
            Class<?> cls = Class.forName(SM_QUALIFIED_CLASSNAME);
            Object o = cls.newInstance();
            System.out.println(o);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static class GeneratorBuilder {
        private static final String DEFAULT_OUTPUT_DIRECTORY = "target/generated-sources/";
        public static final String DEFAULT_RESOURCE_DIRECTORY = "src/main/resources/";

        private Configuration cfg;
        private String outputDirectory;
        private HashMap<String, Object> dataModel;
        private HashMap<String, Object> fsm;
        private ArrayList<State> states;
        private ArrayList<Transition> transitions;


        public GeneratorBuilder() {
            dataModel = new HashMap<>();
            fsm = new HashMap<>();
            states = new ArrayList<>();
            transitions = new ArrayList<>();
        }

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

        public GeneratorBuilder outputPackage(String packageName) {
            if (packageName.charAt(packageName.length() - 1) != '/') {
                packageName += '/';
            }
            outputDirectory = DEFAULT_OUTPUT_DIRECTORY + packageName;
            return this;
        }

        public GeneratorBuilder fromXML(String filePath) throws JDOMException, IOException {
            // XML Parser init
            SAXBuilder sxb = new SAXBuilder();
            Document document = sxb.build(new File(filePath));

            // Parse all states in the xml and create instance
            handleElement(document.getRootElement());

            // Arranging DataModel
            fsm.put("states", states);
            fsm.put("transitions", transitions);
            //fsm.put("initialState", state1.getName());
            dataModel.put("fsm", fsm);
            return this;
        }

        private void handleElement(Element e) {
            if (e.getName() == "state") {
                states.add(new State(e.getAttribute("id").getValue()));
            }
            for (Element subElement : e.getChildren()) {
                handleElement(subElement);
            }
        }

        /*public static void buildDataModel() {
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
    }*/

        public ArrayList<State> getStates() {
            return states;
        }

        public ArrayList<Transition> getTransitions() {
            return transitions;
        }

        public Generator build() {
            return new Generator(cfg, outputDirectory, dataModel);
        }
    }
}
