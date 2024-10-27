package MedicalService.demo.mapper.disease;

import MedicalService.demo.dto.disease.DiseaseDto;
import MedicalService.demo.entity.disease.Disease;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiseaseMapper {

    @Mapping(source = "name", target = "icdCode.name")
    Disease toEntity(DiseaseDto diseaseDto);

    @Mapping(source = "icdCode.name", target = "name")
    DiseaseDto toDto(Disease disease);
}
