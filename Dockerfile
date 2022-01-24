FROM gradle:7.2.0-jdk11 AS build
ENV DOCKER_ENV=dev
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon -x test
RUN ls -lrta /home/gradle/src/build/libs/*.jar

FROM openjdk:11-jre-slim
RUN apt-get update && apt-get install -y curl dnsutils iputils-ping
RUN mkdir -p /apps/OpsERA/components/ansible-services
COPY --from=build /home/gradle/src/build/libs/ansible-service-0.0.1-SNAPSHOT.jar /apps/OpsERA/components/ansible-services/ansible-services.jar
ENV TINI_VERSION v0.19.0
ADD https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini /tini
RUN chmod +x /tini
EXPOSE 9080
ENTRYPOINT exec /tini -- java \
            -XshowSettings:vm \
            -XX:+UseContainerSupport \
            -Dspring.profiles.active=$DOCKER_ENV \
            -Djava.security.egd=file:/dev/./urandom \
            -jar /apps/OpsERA/components/ansible-services/ansible-services.jar