#WearableSensorAPI

WearableSensorAPI is a REST API which communicates with wearable 
devices such smartwatches through a dedicated android app and micro-controllers
such arduino interfaces via MQTT protocol.

The purpose of this REST API is to provide a more flexible way of communication and
manipulation of the data sent by android devices or micro-controllers since the 
native KaaIoT API lacks of functionalities such input validation, data processing or 
output formats choice like csv or xml.

#Getting Started

This is an example of how you may give instructions on setting up your project locally. To get a local copy up and 
running follow these simple example steps.

#Prerequisites

Docker must be installed on your local machine

https://docs.docker.com/get-docker/

A Redis docker image is required for a correct boot of the application

https://hub.docker.com/_/redis

Docker compose is required in order to start the application with a single command

https://docs.docker.com/compose/install/

#Usage

In order to build and start the application with a single command,
move to the root of the project and run 

```docker-compose up --build```

you can also run the application without docker-compose by running the following commands

create the docker image
* ```./gradlew bootBuildImage --imageName=WearSensorAPI ```

start Redis
* ```docker run --name some-redis -d redis```

start WearSensorAPI
* ```docker run --name wearsensor-api```

#Play

Use the attached Postman collection to get an idea how to use the application

#Swagger UI

```http://localhost:8080/wearsensor-api/swagger-ui/```
