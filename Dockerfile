FROM openjdk:8-jre-alpine
MAINTAINER 313DEVGRP <313@313.co.kr>

RUN apk update && apk add wget

RUN wget http://www.313.co.kr/nexus/content/groups/public/313devgrp/packetbeat/7.4.2-linux/packetbeat-7.4.2-linux-x86_64.tar.gz
RUN tar zxvf packetbeat-7.4.2-linux-x86_64.tar.gz
RUN wget http://www.313.co.kr/nexus/content/repositories/StandardProject/313devgrp/packetbeat/1.0.0/packetbeat-1.0.0.yml
RUN mv packetbeat-1.0.0.yml ./packetbeat-7.4.2-linux-x86_64/packetbeat.yml

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} javaServiceTreeFramework.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=live", "-Djava.security.edg=file:/dev/./urandom","-jar","/javaServiceTreeFramework.jar"]