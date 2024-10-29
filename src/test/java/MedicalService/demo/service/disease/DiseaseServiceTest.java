package MedicalService.demo.service.disease;

import MedicalService.demo.exception.NotFoundException;
import MedicalService.demo.mapper.disease.DiseaseMapper;
import MedicalService.demo.mapper.patient.PatientMapper;
import MedicalService.demo.model.dto.disease.DiseaseDto;
import MedicalService.demo.model.entity.disease.Disease;
import MedicalService.demo.model.entity.icd.Icd;
import MedicalService.demo.model.entity.patient.Patient;
import MedicalService.demo.repository.DiseaseRepository;
import org.junit.jupiter.api.BeforeAll;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiseaseServiceTest {

    @InjectMocks
    private DiseaseServiceImpl diseaseService;

    @Mock
    private DiseaseRepository diseaseRepository;
    @Spy
    private DiseaseMapper diseaseMapper = Mappers.getMapper(DiseaseMapper .class);

    private Disease actualDisease;
    private DiseaseDto actualDiseaseDto;
    private LocalDate starting;
    private LocalDate ending;

    @BeforeEach
    public void init(){
        starting = LocalDate.of(2023, Month.APRIL, 12);
        ending = starting.plusDays(4);

        actualDisease = Disease.builder()
                .id(1)
                .icd(new Icd("A00.0", "Холера, вызванная холерным вибрионом 01, биовар cholerae"))
                .description("description")
                .patient(Patient.builder().id(1).build())
                .starting(starting)
                .ending(ending)
                .build();

        actualDiseaseDto = DiseaseDto.builder()
                .id(1L)
                .code("A00.0")
                .description("description")
                .starting(starting)
                .ending(ending)
                .patientId(1L)
                .build();
    }

    @Nested
    @DisplayName("")
    class GetDesign{

        @Test
        @DisplayName("getByIdAndPatientId() с отсутствующим пациентом")
        public void testGetByIdAndPatientIdWithNotFoundPatient(){
            when(diseaseRepository.findByIdAndPatientId(actualDiseaseDto.getId(), actualDiseaseDto.getPatientId()))
                    .thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class,
                    ()-> diseaseService.getByIdAndPatientId(actualDiseaseDto.getId(), actualDiseaseDto.getPatientId()));

            assertEquals(String.format("Disease with id %s not found in the patient with id %s", actualDiseaseDto.getId(), actualDiseaseDto.getPatientId()),
                    exception.getMessage());
        }

        @Test
        @DisplayName("getByIdAndPatientId()")

    }

    @Nested
    @DisplayName("")
    class PutDesign{
        @Test
        @DisplayName("update() с отсутствующим пациентом")
        public void testUpdateWithNotFoundPatient(){
            when(diseaseRepository.findByIdAndPatientId(actualDiseaseDto.getId(), actualDiseaseDto.getPatientId()))
                    .thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class,
                    ()-> diseaseService.getByIdAndPatientId(actualDiseaseDto.getId(), actualDiseaseDto.getPatientId()));

            assertEquals(String.format("Disease with id %s not found in the patient with id %s", actualDiseaseDto.getId(), actualDiseaseDto.getPatientId()),
                    exception.getMessage());
        }
    }

    @Nested
    @DisplayName("")
    class PostDesign{

    }

    @Nested
    @DisplayName("")
    class DeleteDesign{
        @Test
        @DisplayName("deleteByIdAndPatientId() с отсутствующим пациентом")
        public void testDeleteByIdAndPatientIdWithNotFoundPatient(){
            when(diseaseRepository.findByIdAndPatientId(actualDiseaseDto.getId(), actualDiseaseDto.getPatientId()))
                    .thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class,
                    ()-> diseaseService.deleteByIdAndPatientId(actualDiseaseDto.getId(), actualDiseaseDto.getPatientId()));

            assertEquals(String.format("Disease with id %s not found in the patient with id %s", actualDiseaseDto.getId(), actualDiseaseDto.getPatientId()),
                    exception.getMessage());
        }
    }
}
