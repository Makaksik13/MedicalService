package MedicalService.demo.service.icd;

import MedicalService.demo.model.dto.icd.IcdDto;
import org.springframework.data.domain.Page;

public interface IcdService {

    Page<IcdDto> findAll(int offset, int limit);
}
