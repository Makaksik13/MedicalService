package MedicalService.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
		info = @Info(
				title = "Medical Service",
				description = "Test task", version = "1.0.0",
				contact = @Contact(
						name = "Maxim Sukhanov",
						email = "maksimka_13.05.2004@mail.ru"
				)
		)
)
@ConfigurationPropertiesScan("MedicalService.demo.property")
@EnableAsync
@EnableRetry
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
