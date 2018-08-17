# Big Green Button Demo

These software modules and their associated Arduino-based hardware component (the eponymous "big green button") demonstrate a complete SPA application / RESTful service combination deployed to Cloud Foundry in a zero-downtime fashion using Concourse pipelines.

## Architecture

The software modules are comprised of:

1. A Spring Boot back-end RESTful service application (fussball-service)
2. An Angular 2 front-end application (www)
3. The Arduino firmware for the button hardware (arduino)
4. The Java program that detects button presses and kicks off the Concourse pipeline (controller)
5. The Concoure pipeline that builds and deploys the fussball-service and www components to Cloud Foundry (ci)

## Building and Running

### fussball-service

This module is a vanilla Spring Boot application that can be built with Gradle:

``gradle build``

The resulting JAR file can be pushed to Cloud Foundry as-is. By default, it uses an embedded HSQL database so is fully self-contained.

### www

This module is an Angular 2 application that can be built for production using the Angular CLI:

``ng build --configuration=production``

The default "production" configuration builds the German localized version of the app. You can build the English and Dutch versions by running:

``ng build --configuration=production-en``
``ng build --configuration=production-nl``

Note that there are "en", "de" and "nl" configurations that can be used for development for each localization (for example, with "ng serve").

### controller

This module is a Java command-line application written using Spring Shell that can be built with Gradle:

``gradle build``

The build will produce a JAR file with dependencies and can be run with the following (example):

``java -jar build/libs/controller-0.0.1-SNAPSHOT.jar``

When it is run, the application will present you with a shell prompt. Type "help" to get a list of commands.

#### Connecting to the button hardware

1. Make sure the button hardware is plugged in via USB.
2. Run the controller application and type "ports" at the shell prompt.
3. Identify the name of the port that corresponds to the button hardware (e.g. cu.wchusbserial14511240)
4. Type "start PORT_NAME" where PORT_NAME is the string identified in step 3.
5. Type "status" to see the status of the button hardware.

#### Concourse related variables

There are a number of property values used by the controller when executing the Concourse pipeline. There are defaults for each and you can use the "config" command in the shell to view the current values. They can be overridden using the standard Spring value override mechanisms (e.g. via environment variables).

| Name        | Description                                              |
| ----------- | -----------                                              |
| FLY_PATH    | The fully-qualified path to the Concourse fly executable |
| FLY_TARGET  | The Concourse target to use                              |
| FLY_PIPELINE| The Concourse pipeline to use                            |
| FLY_JOB     | The Concourse job within the pipeline to execute         |

### arduino

This module is the Arduino INO project file for the big green button hardware. The firmware is extremely simple and does only 2 things:

1. Sends a 'P' character over the serial port every second. This is basically a "ping" that the button-controller application uses to detect the presence of the hardware.
2. Sends a 'B' character over the serial port when the button is pressed. This is what the button-controller application listens for to kick off the Concourse jobs.


### ci

The Concourse pipeline that does the build and deployment to Cloud Foundry.

It does the following:

1. If a change to the www module is detected in Github, the pipeline will build and deploy the application to the ekfg-blue Cloud Foundry application. This is considered the "non-production" endpoint that can be used for application testing and UAT.
2. The job-www-prod job must be manually executed. It will switch the ekfg-blue (non-production) application to the ekfg (production) application. This is the job that is executed by the button-controller application when the physical green button is pressed.