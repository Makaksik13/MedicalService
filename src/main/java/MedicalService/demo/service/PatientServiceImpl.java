package MedicalService.demo.service;

import MedicalService.demo.dto.patient.PatientDto;
import MedicalService.demo.entity.patient.Patient;
import MedicalService.demo.exception.NotFoundException;
import MedicalService.demo.mapper.patient.PatientMapper;
import MedicalService.demo.repository.PatientRepository;
import MedicalService.demo.validation.Marker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientDto getById(long patientId){
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException(String.format("Post with id %s not found", patientId)));

        log.info("A patient with an id {} has been received", patientId);
        return patientMapper.toDto(patient);
    }

    @Override
    @Validated({Marker.OnCreate.class})
    public PatientDto create(PatientDto patientDto){
        Patient patient = patientRepository.save(patientMapper.toEntity(patientDto));

        log.info("A patient with an id {} has been created", patient.getId());
        return patientMapper.toDto(patient);
    }

    @Override
    @Validated({Marker.OnUpdate.class})
    public PatientDto update(PatientDto patientDto) {
        Patient patient = patientRepository.save(patientMapper.toEntity(patientDto));

        log.info("A patient with an id {} has been updated", patient.getId());
        return patientMapper.toDto(patient);
    }

    @Override
    public void deleteById(long patientId) {
        patientRepository.deleteById(patientId);

        log.info("A patient with an id {} has been deleted", patientId);
    }
}
