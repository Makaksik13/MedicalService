package MedicalService.demo.model.dto.patient;

import MedicalService.demo.model.entity.patient.Gender;
import MedicalService.demo.validation.Marker;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "Сущность пациента")
@Data
public class PatientDto {

    @Schema(description = "Идентификатор", example = "1")
    @Null(groups = Marker.OnCreate.class, message = "The id '${validatedValue}' must be null")
    @NotNull(groups = Marker.OnUpdate.class, message = "The id must be not null")
    private Long id;

    @Schema(description = "Имя", example = "Иван")
    @NotBlank(message = "The name '${validatedValue}' must be not blank")
    @Size(max = 64, message = "The name '${validatedValue}' must be no more than {max}")
    private String name;

    @Schema(description = "Фамилия", example = "Иванов")
    @NotBlank(message = "The surname '${validatedValue}' must be not blank")
    @Size(max = 128, message = "The surname '${validatedValue}' must be no more than {max}")
    private String surname;

    @Schema(description = "Отчество", example = "Иванович")
    private String patronymic;

    @Schema(description = "Пол человека")
    @NotNull(message = "The gender '${validatedValue}' must be not null")
    private Gender gender;

    @Schema(description = "Дата рождения")
    @PastOrPresent(message = "The date of birth '${validatedValue}' must not be in the future")
    private LocalDate birthDate;

    @Schema(description = "Номер полиса ОМС", example = "1234567891234567", minLength = 16, maxLength = 16)
    @Pattern(regexp = "\\d{16}", message = "The policy number '${validatedValue}' must consist of only 16 digits")
    private String policyNumber;
}
