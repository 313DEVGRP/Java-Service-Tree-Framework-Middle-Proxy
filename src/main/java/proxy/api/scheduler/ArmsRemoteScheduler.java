package proxy.api.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ArmsRemoteScheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${backendURL}")
    private String backendURL;

    @Scheduled(initialDelay = 10 * 60 * 1000, fixedDelay = 5 * 60 * 1000) //10m 딜레이, 5m 단위
    public void set_jiraProject_toPdServiceJira() throws Exception {

        logger.info("ArmsScheduler :: set_jiraProject_toPdServiceJira");
        String targetUrl = backendURL + "/auth-anon/api/arms/armsScheduler/set_jiraProject_toPdServiceJira.do";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
    }

    @Scheduled(initialDelay = 5 * 60 * 1000, fixedDelay = 2 * 60 * 1000) //5m 딜레이, 2m 단위
    public void set_PdServiceVersion_toJiraProjectVersion() throws Exception {

        logger.info("ArmsScheduler :: set_PdServiceVersion_toJiraProjectVersion");
        String targetUrl = backendURL + "/auth-anon/api/arms/armsScheduler/set_PdServiceVersion_toJiraProjectVersion.do";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
    }

    @Scheduled(initialDelay = 1 * 60 * 1000, fixedDelay = 10 * 60 * 1000) //1m 딜레이, 10m 단위
    public void set_jiraPriority_toPdServiceJiraPriority() throws Exception {

        logger.info("ArmsScheduler :: set_jiraPriority_toPdServiceJiraPriority");
        String targetUrl = backendURL + "/auth-anon/api/arms/armsScheduler/set_jiraPriority_toPdServiceJiraPriority.do";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
    }
}
