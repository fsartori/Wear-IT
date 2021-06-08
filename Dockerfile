FROM gradle:jdk11-openj9 AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

#RUN gradle build || return 0
COPY . .
RUN gradle clean build

FROM openjdk:11
ENV ARTIFACT_NAME=wearable-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app/
MAINTAINER reds-lab

WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

ENTRYPOINT exec java -jar $ARTIFACT_NAME