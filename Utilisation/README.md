# Utilisation Example

### Applicative Domain

The code inside this Maven project expand upon the applicative domain that I have chosen.
I've chosen to offer a Maven dependency that allows the user to generate an executable State Machine from
a given SCXML file.

The user can then choose to use the generated executable to control their code logic. Just like what QT Creator offers.

The Applicative Contexte can be summed up as oriented toward developper to control a program logic.

### What is this ?

You can find inside this repository a maven project with a main class launching several scenario that for me
could be real utilisation made of my plugin.

The following scenario are implemented :
* The standard code generation as per the portal exemple
* The usage of connectToEvent to call user defined method.
* The code generation for parallel and hierarchic state

### How do I use this for my own project ?

If you want to use the `../Generation/generator` maven dependency you should :
1. Go to the Maven generator/ plugin `cd ../Generation/generator`
1. Build the project and install it to your local repository `mvn clean install`
1. You can then create your own maven project and add the followind dependency :
```xml
<dependencies>
	<dependency>
		<groupId>polytech.fsm</groupId>
		<artifactId>scxml-transcompiler.generator</artifactId>
		<version>1.0-SNAPSHOT</version>
	</dependency>
	...
</dependencies>
```
4. Next you should create the generator object that allows you to generate executable content from a SCXML input.
The generator implements the Builder pattern so you should instantiate a generator like that :
```java
Generator g = null;
        try {
            g = new Generator.GeneratorBuilder()
                    .withDefaultConfig()
                    .outputDirectory("<StateMachineName>")
                    .fromXML(cl.getResource("<InputSCXMLfile>").getPath())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
```
  * `.withDefaultConfig()` set up the Freemarker engine template so it is mandatory.
  * `.outputDirectory("<StateMachineName>")` will make it so that executable content is generated under
`target/generated-sources/<StateMachineName>`
  * `.build()` tells the builder that the configuration is termined and that you want to have a handle on the Generator

5. Now you should generate the source file from the scxml content. To do that call : `g.generate();`

6. Then you need to set up what I call the __Bridge__ that will allow you to have an interface to communicate at run time
with your state machine. To do that you need to tell the generator to compile the sources generated at step five and to
instantiate a new __Bridge__ class with the return value of the compile call. 
  * To briefly expand on that, `g.compile()` use
    a Java Compiler object to compile the code and after compiling it, instantiate the __StateMachine__ class that was
    generated. The __StateMachine__ class has in its constructor, all the call to add the states and the transition, so
    we just need to instantiate this class using a __ClassLoader__. `g.compile()` return the instantiated class as an Object
    and the bridge can then use the __Reflection__ mecanism of Java to interface the method present inside the __Bridge__
    class with the one that we know exist inside the generated __StateMachine__ class.
7. To do all the step 6, you have to call `Bridge b = new Bridge(g.compile());`
8. You can know use the method documented inside the __Bridge__ class (`submitEvent`, `connectToEvent`) to talk with your
state machine.