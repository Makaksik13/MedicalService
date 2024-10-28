package MedicalService.demo.model.dto.disease;

import MedicalService.demo.validation.Marker;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "Сущность болезни")
@Data
public class DiseaseDto {

    @Schema(description = "Идентификатор", example = "1")
    @Null(groups = Marker.OnCreate.class, message = "The id '${validatedValue}' must be null")
    @NotNull(groups = Marker.OnUpdate.class, message = "The id must be not null")
    private Long id;

    @Schema(description = "Дата начала заболевания")
    @PastOrPresent(message = "The starting '${validatedValue}' must not be in the future")
    private LocalDate starting;

    @Schema(description = "Дата окончания заболевания")
    @PastOrPresent(message = "The ending '${validatedValue}' must not be in the future")
    private LocalDate ending;

    @Schema(description = "Название болезни", example = "Брюшной тиф")
    @NotBlank(message = "The name '${validatedValue}' must be not blank")
    @Size(max = 256, message = "The description '${validatedValue}' must be no more than {max}")
    private String name;

    @Schema(description = "Идентификатор заболевшего")
    @Valid
    private long patientId;

    @Schema(description = "Назначения")
    @NotBlank(message = "The description '${validatedValue}' must be not blank")
    @Size(max = 2048, message = "The description '${validatedValue}' must be no more than {max}")
    private String description;
}
