package MedicalService.demo.config.async;

import MedicalService.demo.property.AsyncProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
public class AsyncConfig {

    private final AsyncProperty asyncProperty;

    @Bean(name = "ICDExecutor")
    public Executor ICDExecutor(@Value("${async.settings.icd}") String key) {
        AsyncProperty.AsyncSettings asyncSetting = asyncProperty.getSettings().get(key);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncSetting.getCorePoolSize());
        executor.setMaxPoolSize(asyncSetting.getMaxPoolSize());
        executor.setQueueCapacity(asyncSetting.getQueueCapacity());
        executor.setThreadNamePrefix("AsyncICDThread-");
        executor.initialize();
        return executor;
    }
}
