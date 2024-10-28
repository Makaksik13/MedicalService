package MedicalService.demo.mapper.disease;

import MedicalService.demo.model.dto.disease.DiseaseDto;
import MedicalService.demo.model.entity.disease.Disease;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiseaseMapper {

    @Mapping(source = "patientId", target = "patient.id")
    @Mapping(source = "name", target = "icdCode.name")
    Disease toEntity(DiseaseDto diseaseDto);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "icdCode.name", target = "name")
    DiseaseDto toDto(Disease disease);

    @Mapping(source = "patientId", target = "patient.id")
    @Mapping(source = "name", target = "icdCode.name")
    void update(@MappingTarget Disease disease, DiseaseDto diseaseDto);
}
