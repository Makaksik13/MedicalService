package MedicalService.demo.scheduler;

import MedicalService.demo.entity.icd.Icd;
import MedicalService.demo.repository.IcdRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
@Async("ICDExecutor")
@Component
public class ICDScheduler {

    @Value("${sources.url.ICDs}")
    private String urlToICDs;

    @Value("${batch.icd.size}")
    private int batchSize;

    private final RestTemplate CsvRestTemplate;
    private final IcdRepository icdRepository;
    private final Executor ICDExecutor;

    @CacheEvict(value = "icd", allEntries = true)
    @Retryable(retryFor = { RestClientException.class }, maxAttempts = 5, backoff = @Backoff(delay = 500, multiplier = 3))
    @Scheduled(cron = "${scheduler.icd.cron}")
    public void fillICD(){

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<Icd>> response = this.CsvRestTemplate.exchange(
                urlToICDs,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>(){}
        );

        List<Icd> icds = response.getBody();
        if(icds != null && icds.size() > batchSize){
            ListUtils.partition(icds, batchSize).forEach(sublist->
                    CompletableFuture.runAsync(()-> icdRepository.deleteAll(sublist), ICDExecutor)
                            .thenRunAsync(()->icdRepository.saveAll(sublist), ICDExecutor)
            );
        }
    }
}
