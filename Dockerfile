FROM gradle:7.2.0-jdk11 AS build
ENV DOCKER_ENV=dev
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon -x test

FROM openjdk:11-jre-slim
RUN apt-get update && apt-get install -y curl dnsutils iputils-ping
RUN mkdir -p /apps/OpsERA/components/ansible-services
COPY --from=build /home/gradle/src/build/libs/*.jar /apps/OpsERA/components/ansible-services/ansible-services.jar
EXPOSE 9080
ENTRYPOINT java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Dspring.profiles.active=$DOCKER_ENV -Djava.security.egd=file:/dev/./urandom -jar /apps/OpsERA/components/ansible-services/ansible-services.jar


