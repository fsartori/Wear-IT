FROM openjdk:11
MAINTAINER unimib-disco
ARG JAR_FILE
COPY ${JAR_FILE} /wearable/application.jar
WORKDIR /wearable
ENTRYPOINT ["java", "-jar", "application.jar"]