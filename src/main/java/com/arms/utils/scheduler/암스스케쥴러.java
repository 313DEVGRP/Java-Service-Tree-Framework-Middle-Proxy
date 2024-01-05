package com.arms.utils.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

@Slf4j
@Component
@AllArgsConstructor
public class 암스스케쥴러 {

    @Scheduled(cron="0 0 1 * * *") // 매일 오전 01(새벽)시에 실행
    public void 서버정보백업_스케줄러() {
        log.info("[ 암스스케쥴러 :: 서버정보백업_스케줄러 ] 동작 : {}", Calendar.getInstance().getTime());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> 결과 = restTemplate.exchange("http://engine-fire:33333/engine/serverinfo/backup/scheduler", HttpMethod.POST, null, String.class);
        log.info(결과.toString());
    }

    @Scheduled(cron="0 30 1 * * *") // 매일 오전 01(새벽)시 30분에 실행
    public void 지라이슈_인덱스백업() {
        log.info("[ 암스스케쥴러 :: 지라이슈_인덱스백업 ] 동작 : {}", Calendar.getInstance().getTime());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> 결과 = restTemplate.exchange("http://engine-fire:33333/engine/jira/0/issue/index/backup", HttpMethod.POST, null, String.class);
        log.info(결과.toString());
    }

    @Scheduled(cron="0 0 3 * * *") // 매일 오전 03(새벽)시에 실행
    public void 각_제품서비스_별_요구사항이슈_조회_및_ES저장() {
        log.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항이슈_조회_및_ES저장 ] 동작 : {}", Calendar.getInstance().getTime());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> 결과 = restTemplate.exchange("http://backend-core:31313/arms/scheduler/pdservice/reqstatus/loadToES", HttpMethod.GET, null, String.class);
        log.info(결과.toString());
    }

    @Scheduled(cron="0 0 6 * * *") // 매일 오전 06(새벽)시에 실행
    public void 각_제품서비스_별_요구사항_Status_업데이트_From_ES() {
        log.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항_Status_업데이트_From_ES ] 동작 : {}", Calendar.getInstance().getTime());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> 결과 = restTemplate.exchange("http://backend-core:31313/engine/jira/0/issue/index/backup", HttpMethod.GET, null, String.class);
        log.info(결과.toString());
    }
}
