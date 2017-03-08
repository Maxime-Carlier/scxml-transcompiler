# Finite State Machine
# SCXML Transcompiler

### Author
* Maxime Carlier

### Requirement

This project uses Maven for dependency management.

### Targeted Functionalities

#### Easy
* Has a state - OK
* Can define a state as initial - OK

#### Hard
* the StateMachine class provides a bridge for event communication

### Implementation Details

This FSM generator will use templated generation to create several state class extending the same abstract class.
A Class named StateMachine will be used as a container to store current states, as well as to provide in later versions,
a bridge for event I/O.