package MedicalService.demo.service.icd;

import MedicalService.demo.mapper.icd.IcdMapper;
import MedicalService.demo.model.dto.icd.IcdDto;
import MedicalService.demo.repository.IcdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IcdServiceImpl implements IcdService{

    private final IcdRepository icdRepository;
    private final IcdMapper icdMapper;

    @Override
    @Cacheable(value = "icd", key = "{#offset, #limit}")
    public Page<IcdDto> findAll(int offset, int limit) {
        return icdRepository.findAll(PageRequest.of(offset, limit))
                .map(icdMapper::toDto);
    }
}
