package com.arms.utils.scheduler;

import com.arms.utils.external_communicate.*;
import com.arms.utils.external_communicate.vo.서버정보_엔티티;
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
    private 내부통신기 내부통신기;


    @Scheduled(cron="0 0 1 * * *") // 매일 오전 01(새벽)시에 실행
    public void 서버정보백업_스케줄러() {
        log.info("[ 암스스케쥴러 :: 서버정보백업_스케줄러 ] 동작 : {}", Calendar.getInstance().getTime());
        Iterable<서버정보_엔티티> 결과 = 내부통신기.서버정보백업_스케줄러();
        log.info(결과.toString());
    }

    // @Scheduled(cron="0 30 1 * * *") // 매일 오전 01(새벽)시 30분에 실행
    // 엘라스틱 서치 롤링 인덱스를 사용 하기 때문에 자동 백업 중단 24.02.11
    public void 지라이슈_인덱스백업() {
        log.info("[ 암스스케쥴러 :: 지라이슈_인덱스백업 ] 동작 : {}", Calendar.getInstance().getTime());
        boolean 결과 = 내부통신기.지라이슈_인덱스백업();
        if(결과){
            log.info("성공");
        }else{
            log.info("실패");
        }
    }


    // 최초 한번 실행은 직접 실행으로 한다.
    public void 각_제품서비스_별_요구사항이슈_조회_및_ES저장() {
        log.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항이슈_조회_및_ES저장 ] 동작 : {}", Calendar.getInstance().getTime());
        String 결과 = 내부통신기.각_제품서비스_별_요구사항이슈_조회_및_ES저장();
        log.info(결과.toString());
    }

    @Scheduled(cron="0 0 3 * * *") // 매일 오전 03(새벽)시에 실행
    public void 각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장() {
        log.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장 ] 동작 : {}", Calendar.getInstance().getTime());
        String 결과 = 내부통신기.각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장();
        log.info(결과.toString());
    }

    @Scheduled(cron="0 0 6 * * *") // 매일 오전 06(새벽)시에 실행
    public void 각_제품서비스_별_요구사항_Status_업데이트_From_ES() {
        log.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항_Status_업데이트_From_ES ] 동작 : {}", Calendar.getInstance().getTime());
        String 결과 = 내부통신기.각_제품서비스_별_요구사항_Status_업데이트_From_ES();
        log.info(결과.toString());
    }
}
