FROM openjdk:8-jre-alpine
MAINTAINER 313DEVGRP <313@313.co.kr>

RUN apk update && apk add wget

RUN wget http://www.313.co.kr/nexus/content/groups/public/313devgrp/packetbeat/7.4.2-linux/packetbeat-7.4.2-linux-x86_64.tar.gz
RUN tar zxvf packetbeat-7.4.2-linux-x86_64.tar.gz
ADD script/yml/monitoring/client/packetbeat.yml ./packetbeat-7.4.2-linux-x86_64/packetbeat.yml

RUN wget http://www.313.co.kr/nexus/content/groups/public/313devgrp/topbeat/1.3.1/topbeat-1.3.1-x86_64.tar.gz
RUN tar zxvf topbeat-1.3.1-x86_64.tar.gz
ADD script/yml/monitoring/client/topbeat.yml ./topbeat-1.3.1-x86_64/topbeat.yml

RUN wget http://www.313.co.kr/nexus/content/groups/public/313devgrp/metricbeat/7.4.2-linux/metricbeat-7.4.2-linux-x86_64.tar.gz
RUN tar zxvf metricbeat-7.4.2-linux-x86_64.tar.gz
ADD script/yml/monitoring/client/metricbeat.yml ./metricbeat-7.4.2-linux-x86_64/metricbeat.yml

RUN wget http://www.313.co.kr/nexus/content/groups/public/313devgrp/heartbeat/7.4.2-linux/heartbeat-7.4.2-linux-x86_64.tar.gz
RUN tar zxvf heartbeat-7.4.2-linux-x86_64.tar.gz
ADD script/yml/monitoring/client/heartbeat.yml ./heartbeat-7.4.2-linux-x86_64/heartbeat.yml

RUN wget http://www.313.co.kr/nexus/content/groups/public/313devgrp/filebeat/7.4.2-linux/filebeat-7.4.2-linux-x86_64.tar.gz
RUN tar zxvf filebeat-7.4.2-linux-x86_64.tar.gz
ADD script/yml/monitoring/client/filebeat.yml ./filebeat-7.4.2-linux-x86_64/filebeat.yml

RUN wget http://www.313.co.kr/nexus/content/groups/public/313devgrp/scouter/2.8.1/scouter-2.8.1.jar
RUN mv scouter-2.8.1.jar /usr/local/tomcat/lib/scouter.agent.jar
ADD script/yml/monitoring/client/scouter-2.8.1.conf /usr/local/tomcat/conf/scouter.conf

RUN wget http://www.313.co.kr/nexus/content/groups/public/313devgrp/elastic-apm-agent/1.18.1/elastic-apm-agent-1.18.1.jar
RUN mv elastic-apm-agent-1.18.1.jar /usr/local/tomcat/lib/elastic-apm-agent.jar

ENV JAVA_OPTS="-Xmx4g -Xms4g -javaagent:/usr/local/tomcat/lib/scouter.agent.jar -Dscouter.config=/usr/local/tomcat/conf/scouter.conf -Dobj_name=JSTF-Allinone -javaagent:/usr/local/tomcat/lib/elastic-apm-agent.jar -Delastic.apm.service_name=JSTF-Allinone -Delastic.apm.application_packages=proxy.api -Delastic.apm.server_urls=http://313.co.kr:8200"

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} javaServiceTreeFramework.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=live", "-Djava.security.edg=file:/dev/./urandom","-jar","/javaServiceTreeFramework.jar"]