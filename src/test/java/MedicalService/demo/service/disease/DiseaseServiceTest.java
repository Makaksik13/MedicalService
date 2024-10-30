package MedicalService.demo.service.disease;

import MedicalService.demo.exception.NotFoundException;
import MedicalService.demo.mapper.disease.DiseaseMapper;
import MedicalService.demo.model.dto.disease.DiseaseDto;
import MedicalService.demo.model.dto.patient.PatientDto;
import MedicalService.demo.model.entity.disease.Disease;
import MedicalService.demo.model.entity.icd.Icd;
import MedicalService.demo.model.entity.patient.Patient;
import MedicalService.demo.repository.DiseaseRepository;
import MedicalService.demo.service.patient.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование DiseaseService")
@ExtendWith(MockitoExtension.class)
public class DiseaseServiceTest {

    @InjectMocks
    private DiseaseServiceImpl diseaseService;

    @Mock
    private DiseaseRepository diseaseRepository;
    @Mock
    private PatientService patientService;
    @Spy
    private DiseaseMapper diseaseMapper = Mappers.getMapper(DiseaseMapper .class);

    private Disease expectedDisease;
    private DiseaseDto expectedDiseaseDto;
    private LocalDate starting;
    private LocalDate ending;

    @BeforeEach
    public void init(){
        starting = LocalDate.of(2023, Month.APRIL, 12);
        ending = starting.plusDays(4);

        expectedDisease = Disease.builder()
                .id(1)
                .icd(new Icd("A00.0", "Холера, вызванная холерным вибрионом 01, биовар cholerae"))
                .description("description")
                .patient(Patient.builder().id(1).build())
                .starting(starting)
                .ending(ending)
                .build();

        expectedDiseaseDto = DiseaseDto.builder()
                .id(1L)
                .code("A00.0")
                .description("description")
                .starting(starting)
                .ending(ending)
                .patientId(1L)
                .build();
    }

    @Nested
    class GetDesign{

        @Test
        @DisplayName("Попытка получения отсутствующей болезни у пациента")
        public void testGetByIdAndPatientIdWithNotFoundDisease(){
            PatientDto patientDto = PatientDto.builder().id(1L).build();
            when(diseaseRepository.findByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()))
                    .thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class,
                    ()-> diseaseService.getByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()));

            assertEquals(String.format("Disease with id %s not found in the patient with id %s", expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()),
                    exception.getMessage());
        }

        @Test
        @DisplayName("Получение заболевания пациента")
        public void testGetByIdAndPatientIdWithSuccessfulGetting(){
            when(diseaseRepository.findByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()))
                    .thenReturn(Optional.of(expectedDisease));

            DiseaseDto diseaseDto = diseaseService.getByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId());

            InOrder inOrder = inOrder(diseaseMapper, diseaseRepository);
            inOrder.verify(diseaseRepository, times(1)).findByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId());
            inOrder.verify(diseaseMapper, times(1)).toDto(expectedDisease);
            assertEquals(expectedDiseaseDto, diseaseDto);
        }
    }

    @Nested
    class PutDesign{

        @Test
        @DisplayName("обновление заболевания несуществующего пациента")
        public void testUpdateWithNotFoundDisease(){
            when(diseaseRepository.findByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()))
                    .thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class,
                    ()-> diseaseService.getByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()));

            assertEquals(String.format("Disease with id %s not found in the patient with id %s", expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()),
                    exception.getMessage());
        }

        @Test
        @DisplayName("обновление заболевания у существующего пациента")
        public void testUpdateWithUpdating(){
            when(diseaseRepository.findByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()))
                    .thenReturn(Optional.of(expectedDisease));

            DiseaseDto actualDiseaseDto = diseaseService.update(expectedDiseaseDto, expectedDiseaseDto.getPatientId());

            InOrder inOrder = inOrder(diseaseRepository, diseaseMapper);
            inOrder.verify(diseaseRepository, times(1))
                    .findByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId());
            inOrder.verify(diseaseMapper, times(1)).update(expectedDisease, expectedDiseaseDto);
            inOrder.verify(diseaseRepository, times(1)).save(expectedDisease);
            inOrder.verify(diseaseMapper, times(1)).toDto(expectedDisease);
            assertEquals(expectedDiseaseDto, actualDiseaseDto);
        }
    }

    @Nested
    class PostDesign{

        @Test
        @DisplayName("создание заболевания у существующего пациента")
        public void testCreateWithCreating(){
            PatientDto patientDto = PatientDto.builder().id(1L).build();
            when(patientService.getById(expectedDiseaseDto.getPatientId())).thenReturn(patientDto);
            when(diseaseRepository.save(any(Disease.class))).thenReturn(expectedDisease);

            DiseaseDto actualDiseaseDto = diseaseService.create(expectedDiseaseDto, expectedDiseaseDto.getPatientId());

            InOrder inOrder = inOrder(diseaseRepository, diseaseMapper, patientService);
            inOrder.verify(patientService, times(1)).getById(expectedDiseaseDto.getPatientId());
            inOrder.verify(diseaseMapper, times(1)).toEntity(expectedDiseaseDto);
            inOrder.verify(diseaseRepository, times(1)).save(any(Disease.class));
            inOrder.verify(diseaseMapper, times(1)).toDto(expectedDisease);
            assertEquals(expectedDiseaseDto, actualDiseaseDto);
        }
    }

    @Nested
    class DeleteDesign{

        @Test
        @DisplayName("удаление отсутствующего заболевания")
        public void testDeleteByIdAndPatientIdWithNotFoundPatient(){
            when(diseaseRepository.findByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()))
                    .thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class,
                    ()-> diseaseService.deleteByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()));

            assertEquals(String.format("Disease with id %s not found in the patient with id %s", expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()),
                    exception.getMessage());
        }

        @Test
        @DisplayName("удаление заболевания у пациента")
        public void testDeleteByIdAndPatientIdWithDeletingDisease(){
            when(diseaseRepository.findByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId()))
                    .thenReturn(Optional.of(expectedDisease));
            doNothing().when(diseaseRepository).deleteById(expectedDisease.getId());

            diseaseService.deleteByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId());

            InOrder inOrder = inOrder(diseaseRepository);
            inOrder.verify(diseaseRepository, times(1))
                    .findByIdAndPatientId(expectedDiseaseDto.getId(), expectedDiseaseDto.getPatientId());
            inOrder.verify(diseaseRepository, times(1)).deleteById(expectedDisease.getId());
        }
    }
}
