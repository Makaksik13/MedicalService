package MedicalService.demo.service.disease;

import MedicalService.demo.exception.NotFoundException;
import MedicalService.demo.mapper.disease.DiseaseMapper;
import MedicalService.demo.model.dto.disease.DiseaseDto;
import MedicalService.demo.model.entity.disease.Disease;
import MedicalService.demo.repository.DiseaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService{

    private final DiseaseRepository diseaseRepository;
    private final DiseaseMapper diseaseMapper;

    @Override
    public DiseaseDto getByPatientIdAndDiseaseId(long patientId, long diseaseId){
        Disease disease = findDiseaseByIdAndPatientId(diseaseId, patientId);

        log.info("A disease with an id {} in the patient with id {} has been received", diseaseId, patientId);
        return diseaseMapper.toDto(disease);
    }

    @Override
    public DiseaseDto create(DiseaseDto diseaseDto, long patientId){
        diseaseDto.setPatientId(patientId);
        Disease disease = diseaseRepository.save(diseaseMapper.toEntity(diseaseDto));

        log.info("A disease with an id {} in the patient with id {} has been created", disease.getId(), patientId);
        return diseaseMapper.toDto(disease);
    }

    @Override
    public DiseaseDto update(DiseaseDto diseaseDto, long patientId) {
        Disease disease = findDiseaseByIdAndPatientId(diseaseDto.getId(), patientId);
        diseaseMapper.update(disease, diseaseDto);

        log.info("A disease with an id {} in the patient with id {} has been updated", disease.getId(), patientId);
        return diseaseMapper.toDto(disease);
    }

    @Override
    public void deleteById(long diseaseId, long patientId) {
        Disease disease = findDiseaseByIdAndPatientId(diseaseId, patientId);
        diseaseRepository.deleteById(diseaseId);

        log.info("A disease with an id {} in the patient with id {} has been deleted", diseaseId, patientId);
    }

    private Disease findDiseaseByIdAndPatientId(long diseaseId, long patientId) {
        return diseaseRepository.findByIdAndPatientId(diseaseId, patientId)
                .orElseThrow(() -> new NotFoundException(String.format("Disease with id %s not found in the patient with id %s", diseaseId, patientId)));
    }
}
