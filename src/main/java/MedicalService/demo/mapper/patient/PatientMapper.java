package MedicalService.demo.mapper.patient;

import MedicalService.demo.dto.patient.PatientDto;
import MedicalService.demo.entity.patient.Patient;
import MedicalService.demo.mapper.disease.DiseaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DiseaseMapper.class})
public interface PatientMapper {

    PatientDto toDto(Patient patient);

    Patient toEntity(PatientDto patientDto);
}
