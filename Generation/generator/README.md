# Generator

This generator use FreeMarker template to generate a standalone FSM in maven target
folder under the path `target/generated-sources/<StateMachineName>/`. All sources are generated 
inside a package named `generated`

It should in the end, be able to produce the FSM that implement several functionality provided by SCXML

### Template extension points

#### Easy
* Can create the states  - ![tested](https://img.shields.io/badge/status-tested-green.svg)
* Can set the initial state - ![tested](https://img.shields.io/badge/status-tested-green.svg)

#### Medium
* Can create the Transition - ![tested](https://img.shields.io/badge/status-tested-green.svg)
* Can add transitions to their origin states - ![tested](https://img.shields.io/badge/status-tested-green.svg)
* Can decide which type of transition to create (no action, send, raise) - ![tested](https://img.shields.io/badge/status-tested-green.svg)

### Generator functionality

#### Easy
* Generate the configuration - ![implemented](https://img.shields.io/badge/status-implemented-yellowgreen.svg)
* Generate data model for a static case - ![tested](https://img.shields.io/badge/status-tested-green.svg)
* Output files in the target folder - ![tested](https://img.shields.io/badge/status-tested-green.svg)

#### Medium
* Parse all state instance from a SCXML file - ![tested](https://img.shields.io/badge/status-tested-green.svg)
* Parse transitions from a SCXML file - ![tested](https://img.shields.io/badge/status-tested-green.svg)
* Parse send actions from a SCXML file - ![tested](https://img.shields.io/badge/status-tested-green.svg)
* Parse raise actions from a SCXML file - ![todo](https://img.shields.io/badge/status-todo-red.svg)
* Dynamically Compile generated sources ![tested](https://img.shields.io/badge/status-tested-green.svg)
* Dynamically instantiate compiled file ![tested](https://img.shields.io/badge/status-tested-green.svg)

### Generated Code Functionnality

#### Easy
* Expose submitEvent functionnality ![tested](https://img.shields.io/badge/status-tested-green.svg)

#### Medium
* Handle standard scxml file (portal.scxml) - ![tested](https://img.shields.io/badge/status-tested-green.svg)
* Handle Hierarchical states - ![WIP](https://img.shields.io/badge/status-WIP-yellow.svg)
* Handle Parallel states - ![WIP](https://img.shields.io/badge/status-WIP-yellow.svg)

#### Hard
* Expose connectToEvent functionality - ![tested](https://img.shields.io/badge/status-tested-green.svg)

### Bridge functionnality

#### Medium
* Use reflection to dynamically invoke submitEvent ![tested](https://img.shields.io/badge/status-tested-green.svg)

#### Hard
* Use reflection to dynamically invoke connectToEvent ![tested](https://img.shields.io/badge/status-tested-green.svg)