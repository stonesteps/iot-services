# BWG IOT Services

This project represents the REST service layer for the BWG Spa IoT solution.
It contains the RESTful Service API to retrieve info, manage, and control spas and spa related items.

Basic CRUD (create, read, update, delete) services are provided for several entities as well as custom methods.

The API documentation can be found here: http://iotdev03:8800/api-guide.html


## Git Repository: http://iotdev05.bi.local/bwg.iot/iot-services


## Installation Instructions

Pre-Requisites:   Java8, MongoDB, Maven

    1. Clone Repository $ mkdir myworkspace
                        $ cd myworkspace
                        $ git clone http://iotdev05.bi.local/bwg.iot/iot-services.git
                        $ cd iot-services
    2. Build:           $ mvn clean install
    3. Run:             $ cd rest/target
                        $ java -jar IotServicesApplication.java

    Access API Docs from running server:  http://localhost:8080/api-guide.html
    Invoke Spa List API:   http://localhost:8080/spas


## Artifacts
The project produces two software artifacts:
* __iot-services.jar__  Contains the executable services and API documentation and persistence layer.
* __bwg-iot-model.jar__  Contains the model objects for the project. May be used by other software modules.


        <dependency>
            <groupId>com.bwg</groupId>
            <artifactId>bwg-iot-model</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

## Development Test Environment:  http://iotdev03:8800

## Jenkins: http://iotdev01:8080/job/iot-services-build/