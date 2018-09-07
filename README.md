# Titan CCP - Record Bridge

The [Titan Control Center Prototype](http://eprints.uni-kiel.de/43910) (CCP) is a
scalable monitoring infrastructure for [Industrial DevOps](https://industrial-devops.org/).
It allows to monitor, analyze and visualize the electrical power consumption of
devices and machines in production environments such as factories.

This repository contains the following subprojects related to Titan CPP **Record Bridges**:

* The [Record Bridge framework](#titan-ccp-record-bridge-framework) to simply create concrete, Java-based Record Bridge microservices.
* The [Raritan Record Bridge](kieker-bridge-raritan), a concrete Record Bridge mircoservice for power distribution units from the manufacturer Raritan
* The [Raritan Simulator](kieker-bridge-raritan/raritan-simulator) to tool to simulate Raritan power distribution units

The projects are organized in a Gradle multi-project.


# Titan CCP - Record Bridge Framework

The Record Bridge Framework simplifies the development of Record Bridge microservices.

## Build and Run

We use Gradle as a build tool. In order to build the project run 
`./gradlew build` on Linux/macOS or `./gradlew.bat build` on Windows.
