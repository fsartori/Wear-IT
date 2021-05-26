FROM openjdk:11
MAINTAINER reds-lab
ARG JAR_FILE
COPY ${JAR_FILE} /wearable/application.jar
WORKDIR /wearable
ENTRYPOINT ["java", "-jar", "application.jar"]