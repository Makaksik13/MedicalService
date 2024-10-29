package MedicalService.demo.service.patient;

import MedicalService.demo.exception.NotFoundException;
import MedicalService.demo.mapper.patient.PatientMapper;
import MedicalService.demo.model.dto.patient.PatientDto;
import MedicalService.demo.model.entity.patient.Patient;
import MedicalService.demo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientDto getById(long patientId){
        Patient patient = findPatientById(patientId);

        log.info("A patient with an id {} has been received", patientId);
        return patientMapper.toDto(patient);
    }

    @Override
    public PatientDto create(PatientDto patientDto){
        Patient patient = patientRepository.save(patientMapper.toEntity(patientDto));

        log.info("A patient with an id {} has been created", patient.getId());
        return patientMapper.toDto(patient);
    }

    @Override
    public PatientDto update(PatientDto patientDto) {
        Patient patient = findPatientById(patientDto.getId());
        patientMapper.update(patient, patientDto);
        patientRepository.save(patient);

        log.info("A patient with an id {} has been updated", patient.getId());
        return patientMapper.toDto(patient);
    }

    @Override
    public void deleteById(long patientId) {
        Patient patient = findPatientById(patientId);
        patientRepository.deleteById(patientId);

        log.info("A patient with an id {} has been deleted", patientId);
    }

    private Patient findPatientById(long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Patient with id %s not found", id)));
    }
}
