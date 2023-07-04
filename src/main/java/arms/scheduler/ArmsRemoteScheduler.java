package arms.scheduler;

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

    @Scheduled(initialDelay = 1 * 60 * 1000, fixedDelay = 30 * 60 * 1000)
    public void miningJiraProject() throws Exception {

        logger.info("ArmsScheduler :: miningJiraProject");
        String targetUrl = backendURL + "/arms/jiraProject/miningDataToaRMS.do";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
    }

    @Scheduled(initialDelay = 2 * 60 * 1000, fixedDelay = 30 * 60 * 1000)
    public void miningJiraIssuePriority() throws Exception {

        logger.info("ArmsScheduler :: miningJiraIssuePriority");
        String targetUrl = backendURL + "/arms/jiraIssuePriority/miningDataToaRMS.do";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
    }

    @Scheduled(initialDelay = 3 * 60 * 1000, fixedDelay = 30 * 60 * 1000)
    public void miningJiraIssueResolution() throws Exception {

        logger.info("ArmsScheduler :: miningJiraIssueResolution");
        String targetUrl = backendURL + "/arms/jiraIssueResolution/miningDataToaRMS.do";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
    }

    @Scheduled(initialDelay = 4 * 60 * 1000, fixedDelay = 30 * 60 * 1000)
    public void miningJiraIssueStatus() throws Exception {

        logger.info("ArmsScheduler :: miningJiraIssueStatus");
        String targetUrl = backendURL + "/arms/jiraIssueStatus/miningDataToaRMS.do";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
    }

}
