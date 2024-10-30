package MedicalService.demo.service.patient;

import MedicalService.demo.exception.NotFoundException;
import MedicalService.demo.mapper.patient.PatientMapper;
import MedicalService.demo.model.dto.patient.PatientDto;
import MedicalService.demo.model.entity.patient.Gender;
import MedicalService.demo.model.entity.patient.Patient;
import MedicalService.demo.repository.PatientRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование PatientService")
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private PatientRepository patientRepository;
    @Spy
    private PatientMapper patientMapper = Mappers.getMapper(PatientMapper.class);

    private Patient actualPatient;
    private PatientDto actualPatientDto;

    @BeforeEach
    public void init() {
        actualPatient = Patient.builder().id(1L).name("Иван").surname("Иванов").patronymic("Иванович").birthDate(LocalDate.of(2000, Month.MAY, 14)).gender(Gender.MALE).policyNumber("1234123412341234").build();

        actualPatientDto = PatientDto.builder().id(1L).name("Иван").surname("Иванов").patronymic("Иванович").birthDate(LocalDate.of(2000, Month.MAY, 14)).gender(Gender.MALE).policyNumber("1234123412341234").build();
    }

    @Nested
    class GetDesign {

        @Test
        @DisplayName("удаление существующего пациента")
        public void testGetByIdWithSuccessfulGetting() {
            when(patientRepository.findById(actualPatient.getId())).thenReturn(Optional.of(actualPatient));

            PatientDto patientDto = patientService.getById(actualPatient.getId());

            InOrder inOrder = inOrder(patientRepository, patientMapper);
            inOrder.verify(patientRepository, times(1)).findById(actualPatient.getId());
            inOrder.verify(patientMapper, times(1)).toDto(actualPatient);
            assertEquals(patientMapper.toDto(actualPatient), patientDto);
        }

        @Test
        @DisplayName("попытка удалить отсутствующего пациента")
        public void testGetByIdWithNotFoundUser(){
            when(patientRepository.findById(actualPatient.getId())).thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class, ()-> patientService.getById(actualPatient.getId()));

            assertEquals(String.format("Patient with id %s not found", actualPatient.getId()), exception.getMessage());
        }
    }

    @Nested
    class PostDesign {

        @Test
        @DisplayName("создание пациента")
        public void testCreateWithSuccessfulCreation() {
            when(patientRepository.save(actualPatient)).thenReturn(actualPatient);

            PatientDto createdPatient = patientService.create(actualPatientDto);

            InOrder inOrder = inOrder(patientRepository, patientMapper);
            inOrder.verify(patientMapper, times(1)).toEntity(actualPatientDto);
            inOrder.verify(patientRepository, times(1)).save(actualPatient);
            inOrder.verify(patientMapper, times(1)).toDto(actualPatient);
            assertEquals(actualPatientDto, createdPatient);
        }
    }

    @Nested
    class PutDesign {

        @Test
        @DisplayName("обновление пациента")
        public void testUpdateWithSuccessfulUpdating() {
            when(patientRepository.findById(actualPatientDto.getId())).thenReturn(Optional.of(actualPatient));

            PatientDto updatedPatient = patientService.update(actualPatientDto);

            InOrder inOrder = inOrder(patientRepository, patientMapper);
            inOrder.verify(patientRepository, times(1)).findById(actualPatientDto.getId());
            inOrder.verify(patientMapper, times(1)).update(actualPatient, actualPatientDto);
            inOrder.verify(patientRepository, times(1)).save(actualPatient);
            inOrder.verify(patientMapper, times(1)).toDto(actualPatient);
            assertEquals(actualPatientDto, updatedPatient);
        }

        @Test
        @DisplayName("попытка обновить несуществующего пациента")
        public void testUpdateWithNotFoundPatient() {
            when(patientRepository.findById(actualPatientDto.getId())).thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class, () -> patientService.update(actualPatientDto));

            assertEquals(String.format("Patient with id %s not found", actualPatient.getId()), exception.getMessage());
        }
    }

    @Nested
    class DeleteDesign {

        @Test
        @DisplayName("удаление пациента")
        public void testDeleteByIdWithSuccessfulDeleting() {
            when(patientRepository.findById(actualPatientDto.getId())).thenReturn(Optional.of(actualPatient));
            doNothing().when(patientRepository).deleteById(actualPatientDto.getId());

            patientService.deleteById(actualPatientDto.getId());

            InOrder inOrder = inOrder(patientRepository);
            inOrder.verify(patientRepository, times(1)).findById(actualPatientDto.getId());
            inOrder.verify(patientRepository, times(1)).deleteById(actualPatientDto.getId());
        }

        @Test
        @DisplayName("попытка удалить несуществующего пациента")
        public void testDeleteByIdWithNotFoundPatient() {
            when(patientRepository.findById(actualPatientDto.getId())).thenReturn(Optional.empty());

            var exception = assertThrows(NotFoundException.class, () -> patientService.deleteById(actualPatientDto.getId()));

            assertEquals(String.format("Patient with id %s not found", actualPatient.getId()), exception.getMessage());
        }
    }
}
