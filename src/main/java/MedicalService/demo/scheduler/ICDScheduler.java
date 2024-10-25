package MedicalService.demo.scheduler;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Async("ICDExecutor")
public class ICDScheduler {

    @Scheduled(cron = "${post.user-ban.scheduler.cron}")
    public void fillICD(){

    }
}
