package MedicalService.demo.config.restTemplate;

import MedicalService.demo.converter.CsvHttpMessageConverter;
import MedicalService.demo.entity.icd.ICD;
import MedicalService.demo.parser.CsvParser;
import MedicalService.demo.parser.DefaultCsvParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebClientConfig {

    @Bean
    public CsvParser<ICD> csvParser() {
        return new DefaultCsvParser<>(ICD.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate result = new RestTemplate();
        CsvHttpMessageConverter<ICD> messageConverter = new CsvHttpMessageConverter<>(
                MediaType.ALL,
                this.csvParser()
        );

        result.getMessageConverters().add(messageConverter);
        return result;
    }

}
