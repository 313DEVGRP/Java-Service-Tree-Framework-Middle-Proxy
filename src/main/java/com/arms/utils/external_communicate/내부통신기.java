package com.arms.utils.external_communicate;

import com.arms.utils.external_communicate.vo.서버정보_엔티티;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "loopback", url = "http://127.0.0.1:13131")
public interface 내부통신기 {

    @GetMapping("/auth-anon/schedule/server_info_backup")
    Iterable<서버정보_엔티티> 서버정보백업_스케줄러();

    @GetMapping("/auth-anon/schedule/jiraissue_index_backup")
    boolean 지라이슈_인덱스백업();

    @GetMapping("/auth-anon/schedule/reqissue_es_store")
    String 각_제품서비스_별_요구사항이슈_조회_및_ES저장();

    @GetMapping("/auth-anon/schedule/increment/reqissue_es_store")
    String 각_제품서비스_별_요구사항_증분이슈_조회_및_ES저장();

    @GetMapping("/auth-anon/schedule/issue_es_load")
    String 각_제품서비스_별_요구사항_Status_업데이트_From_ES();

}
