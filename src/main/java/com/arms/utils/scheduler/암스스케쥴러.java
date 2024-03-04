package com.arms.utils.scheduler;

import com.arms.notification.slack.SlackNotificationService;
import com.arms.notification.slack.SlackProperty;
import com.arms.utils.external_communicate.*;
import com.arms.utils.external_communicate.vo.서버정보_엔티티;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Slf4j
@Component
@RequiredArgsConstructor
public class 암스스케쥴러 {

    private final 내부통신기 내부통신기;
    private final SlackNotificationService slackNotificationService;

    @Scheduled(cron="0 0 1 * * *") // 매일 오전 01(새벽)시에 실행
    public void 서버정보백업_스케줄러() {
        String startMessage = String.format("[ 암스스케쥴러 :: 서버정보백업_스케줄러 ] 동작 시작 : %s", Calendar.getInstance().getTime());
        log.info(startMessage);
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, startMessage);

        Iterable<서버정보_엔티티> 결과 = 내부통신기.서버정보백업_스케줄러();

        String endMessage = String.format("[ 암스스케쥴러 :: 서버정보백업_스케줄러 ] 동작 종료 : %s", Calendar.getInstance().getTime()) + "\n\n" + 결과.toString();
        log.info(endMessage);
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, endMessage);
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

    @Scheduled(cron="0 0 3 * * *") // 매일 오전 03(새벽)시에 실행
    public void 각_제품서비스_별_요구사항이슈_조회_및_ES저장() {
        String startMessage = String.format("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항이슈_조회_및_ES저장 ] 동작 시작 : %s", Calendar.getInstance().getTime());
        log.info(startMessage);
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, startMessage);

        String 결과 = 내부통신기.각_제품서비스_별_요구사항이슈_조회_및_ES저장();

        String endMessage = String.format("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항이슈_조회_및_ES저장 ] 동작 종료 : %s", Calendar.getInstance().getTime()) + "\n\n" + 결과.toString();
        log.info(endMessage);
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, endMessage);
    }

    /* 스케줄러 적용 필요 */
    public void 각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장() {
        log.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장 ] 동작 : {}", Calendar.getInstance().getTime());
        String 결과 = 내부통신기.각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장();
        log.info(결과.toString());
    }

    @Scheduled(cron="0 0 6 * * *") // 매일 오전 06(새벽)시에 실행
    public void 각_제품서비스_별_요구사항_Status_업데이트_From_ES() {
        String startMessage = String.format("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항_Status_업데이트_From_ES ] 동작 시작 : %s", Calendar.getInstance().getTime());
        log.info(startMessage);
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, startMessage);

        String 결과 = 내부통신기.각_제품서비스_별_요구사항_Status_업데이트_From_ES();

        String endMessage = String.format("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항_Status_업데이트_From_ES ] 동작 종료 : %s", Calendar.getInstance().getTime()) + "\n\n" + 결과.toString();
        log.info(endMessage);
        slackNotificationService.sendMessageToChannel(SlackProperty.Channel.schedule, endMessage);
    }
}
