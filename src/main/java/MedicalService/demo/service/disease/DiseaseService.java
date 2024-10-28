package MedicalService.demo.service.disease;

import MedicalService.demo.model.dto.disease.DiseaseDto;

public interface DiseaseService {

    DiseaseDto getByPatientIdAndDiseaseId(long patientId, long diseaseId);

    DiseaseDto create(DiseaseDto diseaseDto, long patientId);

    DiseaseDto update(DiseaseDto diseaseDto, long patientId);

    void deleteById(long diseaseId, long patientId);
}
