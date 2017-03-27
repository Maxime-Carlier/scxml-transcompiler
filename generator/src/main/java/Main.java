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
                    .outputPackage("StateMachine1")
                    .build();
            //g.testDataModel();
            //g.generate();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
