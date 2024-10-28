package MedicalService.demo.service.patient;

import MedicalService.demo.model.dto.patient.PatientDto;

public interface PatientService {

    PatientDto getById(long patientId);

    PatientDto create(PatientDto patientDto);

    PatientDto update(PatientDto patientDto);

    void deleteById(long patientId);
}
