FROM openjdk:8-jre-alpine
MAINTAINER 313DEVGRP <313@313.co.kr>

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} javaServiceTreeFramework.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=live", "-Djava.security.edg=file:/dev/./urandom","-jar","/javaServiceTreeFramework.jar"]