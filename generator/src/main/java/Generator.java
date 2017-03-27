import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.scxml.env.SimpleErrorHandler;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.io.SCXMLParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.URLClassLoader;
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

    // This will hold the final path of all generated class
    //It is needed by the compile() method to compile generated sources
    private String[] outputPath;

    private final Configuration cfg;
    private final String outputDirectory;
    private Map<String, Object> root;

    private Generator(Configuration cfg, String outputDirectory) {
        this.cfg=cfg;
        this.outputDirectory = outputDirectory;

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
    }

    private void compile() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, outputPath);
        URLClassLoader classLoader = URLClassLoader.newInstance()
    }

    public static class GeneratorBuilder {
        private static final String DEFAULT_OUTPUT_DIRECTORY = "generator/src/main/java/generated/";
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

        public GeneratorBuilder outputPackage(String packageName) {
            if (packageName.charAt(packageName.length() - 1) != '/') {
                packageName += '/';
            }
            outputDirectory = DEFAULT_OUTPUT_DIRECTORY + packageName;
            return this;
        }

        public GeneratorBuilder fromXML(String filePath) {
            try {
                InputSource source = new InputSource(new BufferedReader(new FileReader(filePath)));
                SCXML scxml = SCXMLParser.parse(source, new SimpleErrorHandler());
                scxml.getDatamodel().getData();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ModelException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Generator build() {
            return new Generator(cfg, outputDirectory);
        }
    }
}
