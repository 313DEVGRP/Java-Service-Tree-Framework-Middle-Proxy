package arms.scheduler;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash("regular-task")
@Builder
@Getter
public class DynamicSchedule {

  @Id
  private String serviceName;
  private String cronExpression;

}
