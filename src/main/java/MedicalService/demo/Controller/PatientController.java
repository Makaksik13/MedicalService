package MedicalService.demo.Controller;

import MedicalService.demo.dto.patient.PatientDto;
import MedicalService.demo.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Контроллер пациентов", description="Взаимодействие с пациентами")
@RequiredArgsConstructor
@RestController("patient")
public class PatientController {

    private final PatientService patientService;

    @Operation(
            summary = "Получение пациента",
            description = "Позволяет получить пациента"
    )
    @GetMapping("{patientId}")
    public PatientDto getPatient(
            @PathVariable @Parameter(description = "Идентификатор пациента", required = true, example = "1") long patientId
    ){
        return patientService.getById(patientId);
    }

    @PostMapping
    public PatientDto createPatient(@RequestBody PatientDto patientDto){
        return patientService.create(patientDto);
    }

    @PutMapping
    public PatientDto updatePatient(@RequestBody PatientDto patientDto){
        return patientService.update(patientDto);
    }

    @DeleteMapping("{patientId}")
    public void deletePatient(@PathVariable long patientId){
        patientService.deleteById(patientId);
    }
}
