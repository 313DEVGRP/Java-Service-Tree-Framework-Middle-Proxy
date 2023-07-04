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
    //changeSchedule("0/10 * * * * ?");
  }

  public void changeSchedule(String serviceName, String cronExpression, Runnable runnable) {

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
