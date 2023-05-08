package arms.api.controller;

import lombok.extern.slf4j.Slf4j;
import arms.scheduler.DynamicScheduleExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class DynamicScheduleController {
  private final DynamicScheduleExecutor dynamicScheduleExecutor;

  public DynamicScheduleController(DynamicScheduleExecutor dynamicScheduleExecutor) {
    this.dynamicScheduleExecutor = dynamicScheduleExecutor;
  }

  private static final String ENGINE_REST_URL = "http://arms-engine:8080";


  @PostMapping("/auth-user/engine/start/{service-name}/{host-type}")
  public Mono<String> start(
       @PathVariable("service-name") String serviceName
      ,@PathVariable("host-type") String hostType
      ,@RequestBody String cronExpression
  ) {

      dynamicScheduleExecutor.changeSchedule(serviceName,cronExpression,()->{
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForLocation(ENGINE_REST_URL+"/engine/"+hostType+"/start",null);
      });

    return Mono.just("test");

  }


}
