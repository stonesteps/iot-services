# BWG IOT Services

This project represents the REST service layer for the BWG Spa IoT solution.
It contains the RESTful Service API to retrieve info, manage, and control spas and spa related items.

Basic CRUD (create, read, update, delete) services are provided for several entities as well as custom methods.

The API documentation can be found here: http://iotdev03:8800/api-guide.html


## Git Repository: http://iotdev05.bi.local/bwg.iot/iot-services


## Installation Instructions

Follow the following steps to download, build, and run the iot-services application locally.

Pre-Requisites:   Java8, MongoDB, Maven

    1. Clone Repository $ mkdir myworkspace
                        $ cd myworkspace
                        $ git clone http://iotdev05.bi.local/bwg.iot/iot-services.git
                        $ cd iot-services
    2. Build:           $ mvn clean install
    3. Run:             $ cd rest/target
                        $ java -Dspring.profiles.active=dev -jar iot-services-0.0.1-SNAPSHOT.jar IotServicesApplication


Access API Docs from running server:  http://localhost:8080/api-guide.html
Invoke Spa List API:   http://localhost:8080/spas


The application has SpringSecurity built in and is activated by default. When enabled, the application expects
all REST requests to be from authenticated users, and include a "remote_user" header containing the username of the user
performing the request.
Security will be disable in the dev environment, and enabled in QA environment.

To run the application with the security filter disabled, set the runtime environment variable
    spring.profiles.active=dev

The spring security filter is expecting a "remote_user" header in http requests. If the remote_user header is not present,
the API will return error code 401 - Unauthorized.


## Artifacts
The project produces two software artifacts:
* __iot-services.jar__  Contains the executable services and API documentation and persistence layer.
* __bwg-iot-model.jar__  Contains the model objects for the project. May be used by other software modules.


        <dependency>
            <groupId>com.bwg</groupId>
            <artifactId>bwg-iot-model</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

## Development Test Environment:

Latest API Documentation: http://iotdev03:8800/api-guide.html

Live running example url: http://localhost:8080/spas


## Jenkins: http://iotdev01:8080/job/iot-services-build/