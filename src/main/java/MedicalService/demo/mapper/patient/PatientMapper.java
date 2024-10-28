package MedicalService.demo.mapper.patient;

import MedicalService.demo.model.dto.patient.PatientDto;
import MedicalService.demo.model.entity.patient.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientMapper {

    PatientDto toDto(Patient patient);

    Patient toEntity(PatientDto patientDto);

    void update(@MappingTarget Patient patient, PatientDto patientDto);
}
