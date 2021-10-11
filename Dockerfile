FROM openjdk:8-jre-alpine
MAINTAINER 313DEVGRP <313@313.co.kr>
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} jstf.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-Djava.security.edg=file:/dev/./urandom","-jar","/jstf.jar"]