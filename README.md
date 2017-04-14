# Finite State Machine
# SCXML Transcompiler

### Author

* Maxime Carlier

### Requirement

This project uses Maven for dependency management.

### Dependency Overview

* `Apache FreeMarker` is the template Engine
* `JDOM 2` will be used to parse the SCXML files

### What is this ?

This repository contains the source code for a Maven plugin that is aimed to be used to generate executable FSM code from an SCXML input.

### Project Layout

There are two directory at the root of this project :
* `Generation/` is the developper side of the application. This contains the projects that are used to implement functionnalities in the plugin
* `Utilisation/` is the user side of the plugin. It shows example on how this plugin should be used.

### Implementation Overview

The FSM generator will use the template engine to produce a standalone code that can be then dynamically compiled and reflected upon to invoke method such as `connectToEvent` or `submitEvent`.
The code produced will be the Object Oriented implementation of an FSM implementing the functionality described by the input SCXML file.
It will make use of Java reflections to invoke method on non specific object and Java functionalities such as Dynamic Compilation, Dynamic instantiation.

### Applicative Domain

I've developped this SCXML generator so that it can be used as a Maven dependency inside a Maven project. For now the user should navigate to the `generator/` maven project and execute `maven install` so that the project
is installed inside the user local repository. This then allow the user to create a project and add this dependency to use the generator :
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
Working example are given inside the `Utilisation/` folder.