package generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import model.statics.ParallelHierarchie;
import model.statics.SimpleHierarchie;
import model.statics.State;
import model.statics.Transition;
import org.jdom2.Attribute;
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
import java.util.*;

/**
 * @author Maxime
 */
public class Generator {

    // Static association 2d array to link template input name to class output name
    private final static String[][] TEMPLATES_IO_ASSOC = {
            {"AbstractStateTemplate.ftl" , "AbstractState.java"},
            {"AbstractTransitionTemplate.ftl" , "AbstractTransition.java"},
            {"EventTemplate.ftl" , "Event.java"},
            {"HierarchicStateTemplate.ftl" , "HierarchicState.java"},
            {"MethodExecutorTemplate.ftl" , "MethodExecutor.java"},
            {"ParallelStateTemplate.ftl" , "ParallelState.java"},
            {"SendTransitionTemplate.ftl" , "SendTransition.java"},
            {"SimpleStateTemplate.ftl" , "SimpleState.java"},
            {"SimpleTransitionTemplate.ftl" , "SimpleTransition.java"},
            {"StateMachineTemplate.ftl" , "StateMachine.java"}
    };

    // Fully Qualified class name for dynamic instantiation after generation by the ClassLoader
    private final static String SM_QUALIFIED_CLASSNAME = "generated.StateMachine";

    // This will hold the final path of all generated class
    // It is needed by the compile() method to compile generated sources
    private String[] outputPath;

    // FreeMarker library template generation config and datamodel
    private final Configuration cfg;
    private final String outputDirectory;
    private final String outputDirectoryWithpackageName;
    private Map<String, Object> root;

    private Generator(Configuration cfg, String outputDirectory, String packageName, HashMap<String, Object> dataModel) {
        this.cfg=cfg;
        this.outputDirectory = outputDirectory;
        this.outputDirectoryWithpackageName = outputDirectory + packageName;
        this.root = dataModel;

        outputPath = new String[TEMPLATES_IO_ASSOC.length];
        for(int i=0;i<outputPath.length;i++) {
            outputPath[i] = outputDirectoryWithpackageName + TEMPLATES_IO_ASSOC[i][1];
        }
    }

    /**
     * Generate all the freemarker templates contained in the folder defined in the Configuration {@link #cfg} attribute with the Datamodel {@link #root}
     * to the directory {@link #outputDirectory}. Then compile generated sources.
     */
    public void generate() {
        try {
            for (String[] pairing : TEMPLATES_IO_ASSOC) {
                Template t = cfg.getTemplate(pairing[0]);
                File fileOutputDirectory = new File(outputDirectoryWithpackageName);
                fileOutputDirectory.mkdirs();
                Writer out = new FileWriter(outputDirectoryWithpackageName + pairing[1]);
                t.process(root, out);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compile the sources that matches the names in {@link #outputPath} and instantiate a state machine
     */
    public Object compile() {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            compiler.run(null, null, null, outputPath);
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(outputDirectory).toURI().toURL()});
            Class<?> cls = Class.forName(SM_QUALIFIED_CLASSNAME, true, classLoader);
            Object o = cls.newInstance();
            return o;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Builder nested class for Builder Pattern
     */
    public static class GeneratorBuilder {
        private static final String DEFAULT_OUTPUT_DIRECTORY = "target/generated-sources/";
        private static final String DEFAULT_PACKAGE = "generated/";
        public static final String DEFAULT_RESOURCE_DIRECTORY = "src/main/resources/";

        private Configuration cfg;
        private String outputDirectory;
        private HashMap<String, Object> dataModel;
        private HashMap<String, Object> fsm;
        private ArrayList<State> states;
        private ArrayList<Transition> transitions;
        private ArrayList<SimpleHierarchie> simpleHierarchies;
        private ArrayList<ParallelHierarchie> parallelHierarchies;
        private State initialState;
        private String initialStateName;
        private boolean initialStateAbsent=true;
        private boolean resolveInitialState=false;
        private HashMap<String, State> stateMap;

        public GeneratorBuilder() {
            dataModel = new HashMap<>();
            fsm = new HashMap<>();
            states = new ArrayList<>();
            transitions = new ArrayList<>();
            stateMap = new HashMap<>();
            simpleHierarchies = new ArrayList<>();
            parallelHierarchies = new ArrayList<>();
        }

        /**
         * Create the configuration with default parameters
         * @return this
         */
        public GeneratorBuilder withDefaultConfig() {
            cfg = new Configuration(Configuration.VERSION_2_3_25);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setClassForTemplateLoading(getClass(), "/");
            return this;
        }

        /**
         * Create the configuration without the classLoader folder. This is for development purpose
         * @return this
         */
        public GeneratorBuilder withDebugConfig() {
            cfg = new Configuration(Configuration.VERSION_2_3_25);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            return this;
        }

        /**
         * Set the folder containing the .ftl templates. This is for developpment purpose only
         * @param directory the directory that contains the templates
         * @return this
         * @throws IOException
         */
        public GeneratorBuilder templatesDirectory(String directory) throws IOException {
            cfg.setDirectoryForTemplateLoading(new File(directory));
            return this;
        }

        /**
         * Set the ouput package
         * @param outDirectory the output package
         * @return this
         */
        public GeneratorBuilder outputDirectory(String outDirectory) {
            if (outDirectory.charAt(outDirectory.length() - 1) != '/') {
                outDirectory += '/';
            }
            outputDirectory = DEFAULT_OUTPUT_DIRECTORY + outDirectory;
            return this;
        }

        /**
         * Create a DataModel from a .scxml file
         * @param filePath the path to the scxml file
         * @return thsi
         * @throws JDOMException
         * @throws IOException
         */
        public GeneratorBuilder fromXML(String filePath) throws JDOMException, IOException {
            // XML Parser init
            SAXBuilder sxb = new SAXBuilder();
            Document document = sxb.build(new File(filePath));

            // Parse all states element, create instance, fills Map and List
            handleStatesOnly(document.getRootElement());
            // Parse other element
            handleOtherElement(document.getRootElement());

            // If initial state was specified in <scxml> root element
            if (resolveInitialState) {
                initialState = stateMap.get(initialStateName);
            }

            // If no initial state has been found set the first state as initial (spec 355)
            if(initialStateAbsent) {
                initialState = states.get(0);
            }

            // Arranging DataModel
            fsm.put("states", states);
            fsm.put("transitions", transitions);
            fsm.put("initialState", initialState.getName());
            fsm.put("simplehierarchie" , simpleHierarchies);
            fsm.put("parallelhierarchie" , parallelHierarchies);
            dataModel.put("fsm", fsm);
            return this;
        }

        /**
         * Parse all elements in scxml and handle only states element
         * @param e the element to be handled
         */
        private void handleStatesOnly(Element e) {
            switch (e.getName()) {
                case "state":
                    State s;
                    List<Element> stateChildren = e.getChildren("state", e.getNamespace());
                    List<Element> parallelChildren = e.getChildren("parallel", e.getNamespace());
                     if (stateChildren.size() > 0 || parallelChildren.size() > 0) {
                        s = new State(e.getAttribute("id").getValue(), "hierarchic");
                        for (Element el : e.getChildren()) {
                            if (el.getName().equals("parallel") || el.getName().equals("state")) {
                                SimpleHierarchie sh = new SimpleHierarchie(s.getName(), el.getAttribute("id").getValue());
                                simpleHierarchies.add(sh);
                                break;
                            }
                        }
                    } else {
                        s = new State(e.getAttribute("id").getValue(), "simple");
                    }
                    states.add(s);
                    stateMap.put(s.getName(), s);
                    break;
                case "parallel":
                    State s2 = new State(e.getAttribute("id").getValue(), "parallel");
                    List<Element> stateChildrens = e.getChildren("state", e.getNamespace());
                    if (stateChildrens.size() > 0) {
                        ParallelHierarchie p = new ParallelHierarchie(s2.getName());
                        for (Element el : stateChildrens) {
                            p.addChild(el.getAttribute("id").getValue());
                        }
                        parallelHierarchies.add(p);
                    }
                    states.add(s2);
                    stateMap.put(s2.getName(), s2);
                    break;
                case "initial":
                    // to uniquely identifiate initial tag, we will name them after their parent.
                    // For example the initial element inside the LampOff State will generate
                    // a state named initialLampOff
                    State s3 = new State(e.getParentElement().getAttribute("id").getValue()+"Initial", "simple");
                    states.add(s3);
                    stateMap.put(s3.getName(), s3);
                    break;
                case "final":
                    State s4 = new State(e.getAttribute("id").getValue(), "simple");
                    states.add(s4);
                    stateMap.put(s4.getName(), s4);
            }

            for (Element subElement : e.getChildren()) {
                handleStatesOnly(subElement);
            }
        }

        /**
         * Parse all elements that are not states.
         * @param e current element to be processed. On first call, should always be the root of the xml tree
         */
        private void handleOtherElement(Element e) {
            switch (e.getName()) {
                // Check if the initial state is specified inside the scxml element and set it as initial
                case "scxml":
                    Attribute initialAttribute = e.getAttribute("initial");
                    if (initialAttribute!=null) {
                        String initial = initialAttribute.getValue();
                        if (initial != null && !initial.equals("")) {
                            initialStateAbsent=false;
                            resolveInitialState=true;
                            initialStateName = initial;
                        }
                    }
                    break;
                case "transition":
                    Element parentStateElement = e.getParentElement();
                    String fromID=null;
                    String event;

                    // Checks if the transition attribute is inside a state element or inside a initial element
                    if (parentStateElement.getName().equals("state")) {
                        fromID = parentStateElement.getAttribute("id").getValue();
                    } else if (parentStateElement.getName().equals("initial")) {
                        fromID = parentStateElement.getParentElement().getAttribute("id").getValue() + "Initial";
                    } else if (parentStateElement.getName().equals("parallel")) {
                        fromID = parentStateElement.getAttribute("id").getValue();
                    }

                    Objects.requireNonNull(fromID);
                    String toID = e.getAttribute("target").getValue();

                    if (e.getAttribute("event") != null) {
                        event =  e.getAttribute("event").getValue();
                    } else {
                        // Usefull for initial transition
                        event = "";
                    }

                    // Parse action ('send' only for now)
                    String sendAction = null;
                    String type = "simple";
                    List<Element> sendChildrens = e.getChildren("send", e.getNamespace());
                    if (sendChildrens.size() > 0) {
                        sendAction = sendChildrens.get(0).getAttributeValue("event");
                        type = "send";
                    }

                    // TODO: parse action
                    transitions.add(new Transition(stateMap.get(fromID), stateMap.get(toID), event, sendAction, type));
                    break;
            }

            // Recursion for infix traversal
            for (Element subElement : e.getChildren()) {
                handleOtherElement(subElement);
            }
        }

        public ArrayList<State> getStates() {
            return states;
        }

        public ArrayList<Transition> getTransitions() {
            return transitions;
        }

        /**
         * Instantiate a generator with the current parameters
         * @return A generator
         */
        public Generator build() {
            return new Generator(cfg, outputDirectory, DEFAULT_PACKAGE, dataModel);
        }
    }
}
