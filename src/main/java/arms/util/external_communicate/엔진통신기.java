package arms.util.external_communicate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "engine-fire", url = "http://engine-fire:33333")
public interface 엔진통신기 {

    @PostMapping("/engine/jira/0/issue/index/backup")
    ResponseEntity 지라이슈_인덱스백업();

    @PostMapping("/engine/serverinfo/backup/scheduler")
    ResponseEntity 서버정보백업_스케줄러();

}
