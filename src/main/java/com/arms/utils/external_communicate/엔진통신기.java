package com.arms.utils.external_communicate;

import com.arms.utils.external_communicate.vo.서버정보_엔티티;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "engine-fire", url = "http://engine-fire:33333")
public interface 엔진통신기 {

    @PostMapping("/engine/jira/0/issue/index/backup")
    boolean 지라이슈_인덱스백업();

    @PostMapping("/engine/serverinfo/backup/scheduler")
    Iterable<서버정보_엔티티> 서버정보백업_스케줄러();

}
