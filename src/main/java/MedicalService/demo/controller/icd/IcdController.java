package MedicalService.demo.controller.icd;

import MedicalService.demo.model.dto.icd.IcdDto;
import MedicalService.demo.service.icd.IcdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Контроллер МКБ-10 кодов болезней")
@ApiResponses({@ApiResponse(responseCode = "200", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "500", useReturnTypeSchema = true)})
@RestController
@RequestMapping("/dictionary/mkb10")
@RequiredArgsConstructor
@Validated
public class IcdController {

    private final IcdService icdService;

    @Operation(
            summary = "Получение страницы МЛБ-10 кодов"
    )
    @GetMapping
    public Page<IcdDto> getAll(
            @Parameter(description = "Страница", example = "0")
            @RequestParam(value = "offset", defaultValue = "0") @Min(0)
            Integer offset
    ) {
        return icdService.findAll(offset);
    }

}
