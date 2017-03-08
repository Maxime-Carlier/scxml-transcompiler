# Finite State Machine
# SCXML Transcompiler

### Author
* Maxime Carlier

### Requirement

This project uses Maven for dependency management.

### Dependency Overview

* `Apache FreeMarker` is the template Engine
* `Apache commons-scxml` will be used as a SCXML parser

### Project Layout

There are two Maven module in this project :
* `generator/` is a template based engine aimed at producing standalone code of a FSM from a given SCXML input file.
* `targetCode/` is a hand made implementation. It is the object model of the code that I'm aiming to produce with
the generator.

### Implementation Overview

The FSM generator will use the template engine to produce a standalone code that can be compiled as is.
The code produced will be the Object Oriented implementation of an FSM implementing the functionality 
described by the input SCXML file.