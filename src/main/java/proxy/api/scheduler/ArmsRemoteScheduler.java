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

    @Scheduled(initialDelay = 1 * 60 * 1000, fixedDelay = 30 * 60 * 1000) //1m 딜레이, 5m 단위
    public void miningJiraProject() throws Exception {

        logger.info("ArmsScheduler :: miningJiraProject");
        String targetUrl = backendURL + "/arms/jiraProject/miningJiraProject.do";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
    }

}