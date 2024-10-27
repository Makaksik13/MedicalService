package MedicalService.demo.service;

import MedicalService.demo.dto.patient.PatientDto;

public interface PatientService {

    PatientDto getById(long patientId);

    PatientDto create(PatientDto patientDto);

    PatientDto update(PatientDto patientDto);

    void deleteById(long patientId);
}
