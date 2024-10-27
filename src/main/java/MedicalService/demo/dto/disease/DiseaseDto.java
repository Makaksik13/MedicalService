package MedicalService.demo.dto.disease;

import MedicalService.demo.entity.patient.Patient;
import MedicalService.demo.validation.Marker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DiseaseDto {

    @Null(groups = Marker.OnCreate.class, message = "The id '${validatedValue}' must be null")
    @NotNull(groups = Marker.OnUpdate.class, message = "The id must be not null")
    private Long id;

    @PastOrPresent(message = "The starting '${validatedValue}' must not be in the future")
    private LocalDate starting;

    @PastOrPresent(message = "The ending '${validatedValue}' must not be in the future")
    private LocalDate ending;

    @NotBlank(message = "The name '${validatedValue}' must be not blank")
    @Size(max = 256, message = "The description '${validatedValue}' must be no more than {max}")
    private String name;

    @Valid
    private Patient patientId;

    @NotBlank(message = "The description '${validatedValue}' must be not blank")
    @Size(max = 2048, message = "The description '${validatedValue}' must be no more than {max}")
    private String description;
}
