package MedicalService.demo.mapper.icd;

import MedicalService.demo.model.dto.icd.IcdDto;
import MedicalService.demo.model.entity.icd.Icd;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IcdMapper {

    IcdDto toDto(Icd icd);

    Icd toEntity(IcdDto icdDto);
}
