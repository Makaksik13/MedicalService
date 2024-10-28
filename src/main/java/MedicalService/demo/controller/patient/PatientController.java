package MedicalService.demo.controller.patient;

import MedicalService.demo.model.dto.patient.PatientDto;
import MedicalService.demo.exception.response.ErrorResponse;
import MedicalService.demo.exception.response.ValidationErrorResponse;
import MedicalService.demo.service.patient.PatientService;
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

@Tag(name="Контроллер пациентов", description="Взаимодействие с пациентами")
@ApiResponses({@ApiResponse(responseCode = "200", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "500", useReturnTypeSchema = true)})
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    @ApiResponse(responseCode = "404", description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(
            summary = "Получение пациента",
            description = "Позволяет получить пациента"
    )
    @GetMapping("{patientId}")
    public PatientDto getPatient(
            @PathVariable @Parameter(description = "Идентификатор пациента", required = true, example = "1")
            long patientId
    ){
        return patientService.getById(patientId);
    }

    @ApiResponse(responseCode = "400",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
    @Operation(
            summary = "Создание пациента",
            description = "Позволяет создать пациента"
    )
    @Validated({Marker.OnCreate.class})
    @PostMapping
    public PatientDto createPatient(@RequestBody @Valid PatientDto patientDto){
        return patientService.create(patientDto);
    }

    @ApiResponses({@ApiResponse( responseCode = "400",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
    @ApiResponse(responseCode = "404", description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @Operation(
            summary = "Обновление пациента",
            description = "Позволяет обновить пациента"
    )
    @Validated({Marker.OnUpdate.class})
    @PutMapping
    public PatientDto updatePatient(@RequestBody @Valid PatientDto patientDto){
        return patientService.update(patientDto);
    }

    @ApiResponse(responseCode = "404", description = "Пациент не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @Operation(
            summary = "Удаление пациента",
            description = "Позволяет удалить пациента"
    )
    @DeleteMapping("{patientId}")
    public void deletePatient(@PathVariable long patientId){
        patientService.deleteById(patientId);
    }
}
