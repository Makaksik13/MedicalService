package MedicalService.demo.controller.disease;

import MedicalService.demo.exception.response.ErrorResponse;
import MedicalService.demo.exception.response.ValidationErrorResponse;
import MedicalService.demo.model.dto.disease.DiseaseDto;
import MedicalService.demo.service.disease.DiseaseService;
import MedicalService.demo.validation.Marker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Контроллер болезней", description="Взаимодействие с болезнями")
@ApiResponses({@ApiResponse(responseCode = "200", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "500", useReturnTypeSchema = true)})
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/patient")
public class DiseaseController {

    private final DiseaseService diseaseService;

    @ApiResponse(responseCode = "404", description = "Болезнь у пациента не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(
            summary = "Получение данных о болезни пациента",
            description = "Позволяет получить данные о болезни пациента"
    )
    @GetMapping("/{patient_id}/disease/{disease_id}")
    public DiseaseDto getDisease(
            @PathVariable(name = "disease_id")
            @Parameter(description = "Идентификатор болезни", required = true, example = "1")
            long diseaseId,
            @PathVariable(name = "patient_id")
            @Parameter(description = "Идентификатор пациента", required = true, example = "1")
            long patientId

    ){
        return diseaseService.getByIdAndPatientId(diseaseId, patientId);
    }

    @ApiResponse(responseCode = "400",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
    @Operation(
            summary = "Создание информации о болезни у пациента",
            description = "Позволяет создать информацию о болезни у пациента"
    )
    @Validated({Marker.OnCreate.class})
    @PostMapping("/{patient_id}/disease")
    public DiseaseDto createDisease(
            @RequestBody @Valid DiseaseDto diseaseDto,
            @PathVariable(name = "patient_id")
            @Parameter(description = "Идентификатор пациента", required = true, example = "1")
            long patientId){
        return diseaseService.create(diseaseDto, patientId);
    }

    @ApiResponses({@ApiResponse( responseCode = "400",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Болезнь у пациента не найдена",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @Operation(
            summary = "Обновление информации о болезни у пациента",
            description = "Позволяет обновить информацию о болезни у пациента"
    )
    @Validated({Marker.OnUpdate.class})
    @PutMapping("/{patient_id}/disease")
    public DiseaseDto updateDisease(
            @RequestBody @Valid DiseaseDto diseaseDto,
            @PathVariable(name = "patient_id")
            @Parameter(description = "Идентификатор пациента", required = true, example = "1")
            long patientId){
        return diseaseService.update(diseaseDto, patientId);
    }

    @ApiResponse(responseCode = "404", description = "Болезнь у пациента не найдена",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(
            summary = "Удаление информации о болезни у пациента",
            description = "Позволяет удалить информации о болезни у пациента"
    )
    @DeleteMapping("/{patient_id}/disease/{disease_id}")
    public void deleteDisease(
            @PathVariable(name = "disease_id")
            @Parameter(description = "Идентификатор болезни", required = true, example = "1")
            long diseaseId,
            @PathVariable(name = "patient_id")
            @Parameter(description = "Идентификатор пациента", required = true, example = "1")
            long patientId
    ){
        diseaseService.deleteByIdAndPatientId(diseaseId, patientId);
    }
}
