package MedicalService.demo.service.disease;

import MedicalService.demo.model.dto.disease.DiseaseDto;

public interface DiseaseService {

    DiseaseDto getByIdAndPatientId(long diseaseId, long patientId);

    DiseaseDto create(DiseaseDto diseaseDto, long patientId);

    DiseaseDto update(DiseaseDto diseaseDto, long patientId);

    void deleteByIdAndPatientId(long diseaseId, long patientId);
}
