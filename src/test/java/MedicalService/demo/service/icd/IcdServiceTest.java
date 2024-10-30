package MedicalService.demo.service.icd;

import MedicalService.demo.mapper.icd.IcdMapper;
import MedicalService.demo.model.dto.icd.IcdDto;
import MedicalService.demo.model.entity.icd.Icd;
import MedicalService.demo.repository.IcdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование IcdService")
@ExtendWith(MockitoExtension.class)
public class IcdServiceTest {

    @InjectMocks
    private IcdServiceImpl icdService;

    @Mock
    private IcdRepository icdRepository;
    @Spy
    private IcdMapper icdMapper = Mappers.getMapper(IcdMapper.class);

    private PageImpl<Icd> page;
    private List<Icd> content;

    @BeforeEach
    public void init(){
        content = new ArrayList<>(List.of(
                new Icd("A01.1", "Паратиф A"),
                new Icd("A01.2", "Паратиф B"),
                new Icd("A01.3", "Паратиф C"),
                new Icd("A01.4", "Паратиф неуточненный"),
                new Icd("A02",	"Другие сальмонеллезные инфекции"),
                new Icd("A02.0", "Сальмонеллезный энтерит"),
                new Icd("A02.1", "Сальмонеллезная септицемия"),
                new Icd("A02.2", "Локализованная сальмонеллезная инфекция"),
                new Icd("A02.8", "Другая уточненная сальмонеллезная инфекция"),
                new Icd("A02.9", "Сальмонеллезная инфекция неуточненная"),
                new Icd("A03", "Шигеллез")
        ));

        page = new PageImpl<>(content);

        ReflectionTestUtils.setField(icdService, "sizeOfPage", content.size());
    }

    @Nested
    class GetDesign{

        @Test
        @DisplayName("Получение данных о кодах заболеваний")
        public void testFindAllWittGetting(){
            when(icdRepository.findAll(any(Pageable.class))).thenReturn(page);

            Page<?> actualPage = icdService.findAll(0);

            assertThat(actualPage.getContent()).hasOnlyElementsOfType(IcdDto.class);
            assertThat(actualPage.getContent()).hasSize(content.size());
        }
    }
}
