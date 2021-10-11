FROM openjdk:8-jre-alpine
MAINTAINER 313DEVGRP <313@313.co.kr>

RUN apk update && apk add wget


RUN wget http://www.313.co.kr/nexus/content/groups/public/313devgrp/packetbeat/7.4.2-linux/packetbeat-7.4.2-linux-x86_64.tar.gz
RUN tar zxvf packetbeat-7.4.2-linux-x86_64.tar.gz
RUN wget http://www.313.co.kr/nexus/content/repositories/StandardProject/313devgrp/packetbeat/1.0.0/packetbeat-1.0.0.yml
RUN mv packetbeat-1.0.0.yml ./packetbeat-7.4.2-linux-x86_64/packetbeat.yml


RUN wget http://www.313.co.kr/nexus/content/groups/public/313devgrp/topbeat/1.3.1/topbeat-1.3.1-x86_64.tar.gz
RUN tar zxvf topbeat-1.3.1-x86_64.tar.gz
RUN wget http://www.313.co.kr/nexus/content/repositories/StandardProject/313devgrp/topbeat/1.0.0/topbeat-1.0.0.yml
RUN mv topbeat-1.0.0.yml ./topbeat-1.3.1-x86_64/topbeat.yml


ENV JAVA_OPTS="-Xmx4g -Xms4g -javaagent:/usr/local/tomcat/lib/scouter.agent.jar -Dscouter.config=/usr/local/tomcat/conf/scouter.conf -Dobj_name=JSTF-Allinone -javaagent:/usr/local/tomcat/lib/elastic-apm-agent.jar -Delastic.apm.service_name=JSTF-Allinone -Delastic.apm.application_packages=proxy.api -Delastic.apm.server_urls=http://313.co.kr:8200"

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} javaServiceTreeFramework.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=live", "-Djava.security.edg=file:/dev/./urandom","-jar","/javaServiceTreeFramework.jar"]