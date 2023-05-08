package arms.scheduler;

import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class DynamicScheduleExecutor {

  private final TaskScheduler taskScheduler;
  private final DynamicScheduleRepository dynamicScheduleRepository;


  public DynamicScheduleExecutor(TaskScheduler taskScheduler,
      DynamicScheduleRepository dynamicScheduleRepository) {
    this.taskScheduler = taskScheduler;
    this.dynamicScheduleRepository = dynamicScheduleRepository;
  }


  public void stop(String serviceName) {
    //if (scheduledFuture != null) {


//      dynamicScheduleRepository
//          .findById(serviceName)
//          .ifPresent(a->a.getScheduledFuture().cancel(true));

    //}
  }

  //@PostConstruct
  //@Scheduled(fixedDelay = )
  public void init() {
    //레디스에서 읽어서 해당 corn 시간을 가져와야함
    //changeSchedule("0/10 * * * * ?");
  }

  public void changeSchedule(String serviceName, String cronExpression, Runnable runnable) {

    //1. 레디스에 배치 명과 cronExpression 를 저장해서 나중에 어플리케이션이 재시작시에 읽어 갈수 있게 해야함
    //2. open feign 배치->엔진 직접 호출

    ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(runnable, new CronTrigger(cronExpression));

    if(scheduledFuture!=null){
      System.out.println("cronExpression"+cronExpression);

      dynamicScheduleRepository.save(
          DynamicSchedule.builder()
              .serviceName(serviceName)
              .cronExpression(cronExpression)
              .build()
      );
    }
  }

}
