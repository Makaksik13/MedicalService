package MedicalService.demo.model.dto.icd;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Сущность МКБ-10 кода болезни")
@Data
public class IcdDto {

    @Schema(description = "Код", example = "A01.0")
    @NotBlank(message = "The code '${validatedValue}' must be not blank")
    @Size(max = 8, message = "The code '${validatedValue}' must be no more than {max}")
    private String code;

    @Schema(description = "Название болезни", example = "Брюшной тиф")
    @NotBlank(message = "The name '${validatedValue}' must be not blank")
    @Size(max = 256, message = "The name '${validatedValue}' must be no more than {max}")
    private String name;
}
