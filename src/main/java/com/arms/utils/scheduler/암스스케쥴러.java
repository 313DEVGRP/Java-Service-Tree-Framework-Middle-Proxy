package com.arms.utils.scheduler;

import com.arms.utils.external_communicate.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Slf4j
@Component
@AllArgsConstructor
public class 암스스케쥴러 {

    @Autowired
    private 엔진통신기 엔진통신기;

    @Autowired
    private 백엔드코어통신기 백엔드코어통신기;


    @Scheduled(cron="0 30 16 * * *") // 매일 오전 01(새벽)시에 실행
    public void 서버정보백업_스케줄러() {
        log.info("[ 암스스케쥴러 :: 서버정보백업_스케줄러 ] 동작 : {}", Calendar.getInstance().getTime());
        ResponseEntity 결과 = 엔진통신기.서버정보백업_스케줄러();
        log.info(결과.toString());
    }

    @Scheduled(cron="0 30 1 * * *") // 매일 오전 01(새벽)시 30분에 실행
    public void 지라이슈_인덱스백업() {
        log.info("[ 암스스케쥴러 :: 지라이슈_인덱스백업 ] 동작 : {}", Calendar.getInstance().getTime());
        ResponseEntity 결과 = 엔진통신기.지라이슈_인덱스백업();
        log.info(결과.toString());
    }

    @Scheduled(cron="0 0 3 * * *") // 매일 오전 03(새벽)시에 실행
    public void 각_제품서비스_별_요구사항이슈_조회_및_ES저장() {
        log.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항이슈_조회_및_ES저장 ] 동작 : {}", Calendar.getInstance().getTime());
        ResponseEntity 결과 = 백엔드코어통신기.각_제품서비스_별_요구사항이슈_조회_및_ES저장();
        log.info(결과.toString());
    }

    @Scheduled(cron="0 0 6 * * *") // 매일 오전 06(새벽)시에 실행
    public void 각_제품서비스_별_요구사항_Status_업데이트_From_ES() {
        log.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항_Status_업데이트_From_ES ] 동작 : {}", Calendar.getInstance().getTime());
        ResponseEntity 결과 = 백엔드코어통신기.각_제품서비스_별_요구사항_Status_업데이트_From_ES();
        log.info(결과.toString());
    }
}
