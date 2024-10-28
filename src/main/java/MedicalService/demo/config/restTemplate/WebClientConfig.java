package MedicalService.demo.config.restTemplate;

import MedicalService.demo.converter.CsvHttpMessageConverter;
import MedicalService.demo.model.entity.icd.Icd;
import MedicalService.demo.parser.CsvParser;
import MedicalService.demo.parser.DefaultCsvParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebClientConfig {

    @Bean
    public CsvParser<Icd> csvParser() {
        return new DefaultCsvParser<>(Icd.class);
    }

    @Bean
    public RestTemplate CsvRestTemplate(CsvParser<Icd> csvParser) {
        RestTemplate result = new RestTemplate();
        CsvHttpMessageConverter<Icd> messageConverter = new CsvHttpMessageConverter<>(
                MediaType.ALL,
                csvParser
        );

        result.getMessageConverters().add(messageConverter);
        return result;
    }
}
