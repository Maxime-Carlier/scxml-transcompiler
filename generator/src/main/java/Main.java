import java.io.IOException;

/**
 * @author Maxime
 */
public class Main {
    public static void main(String[] args) {
        try {
            Generator g = new Generator.GeneratorBuilder()
                    .withDefaultConfig()
                    .templatesDirectory(Generator.GeneratorBuilder.DEFAULT_RESOURCE_DIRECTORY)
                    .outputDirectory(Generator.GeneratorBuilder.DEFAULT_OUTPUT_DIRECTORY)
                    .build();
            g.testDataModel();
            g.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
