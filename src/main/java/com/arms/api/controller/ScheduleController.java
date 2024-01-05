package com.arms.api.controller;

import com.arms.config.*;
import com.arms.utils.external_communicate.vo.서버정보_엔티티;
import com.arms.utils.external_communicate.내부통신기;
import org.keycloak.admin.client.resource.UserProfileResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.arms.utils.external_communicate.*;

import java.util.Calendar;
import java.util.List;

@Controller
public class ScheduleController {

    @Autowired
    private 엔진통신기 엔진통신기;

    @Autowired
    private 백엔드코어통신기 백엔드코어통신기;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/auth-anon/schedule/server_info_backup", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<서버정보_엔티티> 서버정보백업_스케줄러() {

        logger.info("[ 암스스케쥴러 :: 서버정보백업_스케줄러 ] 동작 : {}", Calendar.getInstance().getTime());
        Iterable<서버정보_엔티티> 결과 = 엔진통신기.서버정보백업_스케줄러();
        return 결과;
    }

    @RequestMapping(value = "/auth-anon/schedule/jiraissue_index_backup", method = RequestMethod.GET)
    @ResponseBody
    public boolean 지라이슈_인덱스백업() {

        logger.info("[ 암스스케쥴러 :: 지라이슈_인덱스백업 ] 동작 : {}", Calendar.getInstance().getTime());
        boolean 결과 = 엔진통신기.지라이슈_인덱스백업();
        return 결과;
    }

    @RequestMapping(value = "/auth-anon/schedule/reqissue_es_store", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity 각_제품서비스_별_요구사항이슈_조회_및_ES저장() {

        logger.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항이슈_조회_및_ES저장 ] 동작 : {}", Calendar.getInstance().getTime());
        ResponseEntity 결과 = 백엔드코어통신기.각_제품서비스_별_요구사항이슈_조회_및_ES저장();
        return 결과;
    }

    @RequestMapping(value = "/auth-anon/schedule/issue_es_load", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity 각_제품서비스_별_요구사항_Status_업데이트_From_ES() {

        logger.info("[ 암스스케쥴러 :: 각_제품서비스_별_요구사항_Status_업데이트_From_ES ] 동작 : {}", Calendar.getInstance().getTime());
        ResponseEntity 결과 = 백엔드코어통신기.각_제품서비스_별_요구사항_Status_업데이트_From_ES();
        return 결과;
    }

}