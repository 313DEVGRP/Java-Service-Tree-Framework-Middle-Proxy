package proxy.api.index.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import proxy.api.scheduler.ArmsRemoteScheduler;

@Controller
public class CallBackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ArmsRemoteScheduler armsRemoteScheduler;

    @Value("${backendURL}")
    private String backendURL;

    @RequestMapping(value = "/callback/api/arms/armsScheduler/forceExec/set_PdServiceVersion_toJiraProjectVersion.do", method = RequestMethod.GET)
    @ResponseBody
    public String set_PdServiceVersion_toJiraProjectVersion() throws Exception {
        //armsRemoteScheduler.set_PdServiceVersion_toJiraProjectVersion();
        return "CallForwardBackendController :: set_PdServiceVersion_toJiraProjectVersion";
    }

    @RequestMapping(value = "/callback/api/arms/reqStatus/{reqStatusTableName}/updateStatusNode.do", method = RequestMethod.GET)
    @ResponseBody
    public String putJiraIssue(@PathVariable("reqStatusTableName") String reqStatusTableName) throws Exception {
        logger.info("ArmsScheduler :: putJiraIssue");
        String targetUrl = backendURL + "/auth-anon/api/arms/reqStatus/"+ reqStatusTableName + "/updateStatusNode.do";

        AsyncRestTemplate restTemplate = new AsyncRestTemplate();
        ListenableFuture<ResponseEntity<String>> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
        return "CallForwardBackendController :: putJiraIssue";
    }

    @RequestMapping(value = "/callback/api/arms/reqStatus/{reqStatusTableName}/reqIssueDisable/updateStatusNode.do", method = RequestMethod.GET)
    @ResponseBody
    public String updateReqIssueDisable(@PathVariable("reqStatusTableName") String reqStatusTableName) throws Exception {
        logger.info("ArmsScheduler :: updateReqIssueDisable");
        String targetUrl = backendURL + "/auth-anon/api/arms/reqStatus/"+ reqStatusTableName + "/reqIssueDisable/updateStatusNode.do";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
        return "CallForwardBackendController :: updateReqIssueDisable";
    }

    @RequestMapping(value = "/callback/api/arms/reqStatus/{reqStatusTableName}/issueCrawler/updateStatusNode.do", method = RequestMethod.GET)
    @ResponseBody
    public String updateJiraIssueCrawler(@PathVariable("reqStatusTableName") String reqStatusTableName) throws Exception {
        logger.info("ArmsScheduler :: updateJiraIssueCrawler");
        String targetUrl = backendURL + "/auth-anon/api/arms/reqStatus/"+ reqStatusTableName + "/issueCrawler/updateStatusNode.do";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        logger.info("response = " + response);
        return "CallForwardBackendController :: updateJiraIssueCrawler";
    }
}
