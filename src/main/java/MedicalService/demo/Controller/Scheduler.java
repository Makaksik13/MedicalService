package MedicalService.demo.Controller;

import MedicalService.demo.scheduler.ICDScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class Scheduler {

    private final ICDScheduler icdScheduler;

    @GetMapping
    public void test(){
        icdScheduler.fillICD();
    }
}
