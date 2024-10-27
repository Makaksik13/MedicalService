package MedicalService.demo.dto.icd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IcdDto {

    @NotBlank(message = "The code '${validatedValue}' must be not blank")
    @Size(max = 8, message = "The code '${validatedValue}' must be no more than {max}")
    private String code;

    @NotBlank(message = "The name '${validatedValue}' must be not blank")
    @Size(max = 256, message = "The name '${validatedValue}' must be no more than {max}")
    private String name;
}
