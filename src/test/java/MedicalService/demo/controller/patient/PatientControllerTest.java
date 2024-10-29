package MedicalService.demo.controller.patient;

import MedicalService.demo.SpringContextTest;
import MedicalService.demo.exception.response.ErrorResponse;
import MedicalService.demo.exception.response.ValidationErrorResponse;
import MedicalService.demo.exception.response.Violation;
import MedicalService.demo.mapper.patient.PatientMapper;
import MedicalService.demo.model.dto.patient.PatientDto;
import MedicalService.demo.model.entity.patient.Gender;
import MedicalService.demo.model.entity.patient.Patient;
import MedicalService.demo.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@AutoConfigureMockMvc
@DisplayName("Тестирование PatientController")
@Sql(
        scripts = {"/database/fill_patient.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS,
        config = @SqlConfig(encoding = "utf-8")
)
public class PatientControllerTest extends SpringContextTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    private PatientDto validPatientDto;
    private PatientDto fullInvalidPatientDto;
    private List<Violation> violations = new ArrayList<>();

    @BeforeEach
    public void init() {
        validPatientDto = PatientDto.builder()
                .name("Иван")
                .surname("Иванов")
                .patronymic("Иванович")
                .birthDate(LocalDate.of(2000, Month.MAY, 14))
                .gender(Gender.MALE)
                .policyNumber("1234123412341234")
                .build();

        LocalDate invalidDate = LocalDate.now().plusDays(10);
        fullInvalidPatientDto = PatientDto.builder()
                .surname(" ")
                .name(" ")
                .birthDate(invalidDate)
                .policyNumber("qw")
                .build();

        violations.add(new Violation("birthDate", "The date of birth '" + invalidDate+ "' must not be in the future"));
        violations.add(new Violation("gender", "The gender must be not null"));
        violations.add(new Violation("name", "The name '" + fullInvalidPatientDto.getName() +"' must be not blank"));
        violations.add(new Violation("policyNumber", "The policy number '"+fullInvalidPatientDto.getPolicyNumber()+"' must consist of only 16 digits"));
        violations.add(new Violation("surname", "The surname '" + fullInvalidPatientDto.getSurname() + "' must be not blank"));
    }

    @Test
    @DisplayName("Тест штатной работы post-эндпоинта")
    public void testPostEndpointWithSuccessfulCreationPatient() throws Exception {
        String result = mockMvc.perform(post("/patient")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validPatientDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        PatientDto patient = objectMapper.readValue(result, PatientDto.class);
        Optional<Patient> OptionalPatientFromDB = patientRepository.findById(patient.getId());

        assertTrue(OptionalPatientFromDB.isPresent());
        Patient patientFromDB = OptionalPatientFromDB.get();
        assertEquals(patientFromDB, patientMapper.toEntity(patient));
        validPatientDto.setId(patientFromDB.getId());
        assertEquals(validPatientDto, patientMapper.toDto(patientFromDB));
    }

    @Test
    @DisplayName("Тест post-эндпоинта с передачей полностью невалидного пациента")
    public void testPostEndpointWithFailedCreationFullInvalidPatient() throws Exception{
        String result = mockMvc.perform(post("/patient")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(fullInvalidPatientDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ValidationErrorResponse response = objectMapper.readValue(result, ValidationErrorResponse.class);
        List<Violation> responseViolations = response.getViolations();

        assertTrue(responseViolations.containsAll(violations));
    }

    @Test
    @DisplayName("Тест post-эндпоинта с передачей пациента с идентификатором")
    public void testPostEndpointWithFailedCreationPatient() throws Exception{
        validPatientDto.setId(Long.MAX_VALUE);
        String result = mockMvc.perform(post("/patient")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validPatientDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ValidationErrorResponse response = objectMapper.readValue(result, ValidationErrorResponse.class);
        List<Violation> responseViolations = response.getViolations();
        Violation violationId = new Violation("createPatient.patientDto.id", "The id '" +Long.MAX_VALUE + "' must be null");
        assertTrue(responseViolations.contains(violationId));
    }

    @Test
    @DisplayName("Тест штатной работы get-эндпоинта")
    public void testGetEndpointWithSuccessfulGettingPatient() throws Exception{
        String result = mockMvc.perform(get("/patient/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        PatientDto patient = objectMapper.readValue(result, PatientDto.class);
        assertEquals(patient.getId(), 1);
    }

    @Test
    @DisplayName("Тест get-эндпоинта с передачей id несуществующего пациента")
    public void testGetEndpointWithNotFoundPatient() throws Exception{
        String result = mockMvc.perform(get("/patient/100")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse response = objectMapper.readValue(result, ErrorResponse.class);
        assertEquals("Patient with id 100 not found", response.getMessage());
    }

    @Test
    @DisplayName("Тест штатной работы put-эндпоинта")
    public void testPutEndpointWithSuccessfulUpdatingPatient() throws Exception {
        validPatientDto.setId(1L);

        String result = mockMvc.perform(put("/patient")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validPatientDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        PatientDto patientDto = objectMapper.readValue(result, PatientDto.class);
        Optional<Patient> OptionalPatientFromDB = patientRepository.findById(patientDto.getId());

        assertTrue(OptionalPatientFromDB.isPresent());
        Patient patientFromDB = OptionalPatientFromDB.get();
        assertEquals(patientFromDB, patientMapper.toEntity(patientDto));
        assertEquals(validPatientDto, patientMapper.toDto(patientFromDB));
    }

    @Test
    @DisplayName("Тест put-эндпоинта с передачей полностью невалидного пациента")
    public void testPutEndpointWithFailedUpdatingPatient() throws Exception{
        String result = mockMvc.perform(put("/patient")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(fullInvalidPatientDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ValidationErrorResponse response = objectMapper.readValue(result, ValidationErrorResponse.class);
        List<Violation> responseViolations = response.getViolations();

        assertTrue(responseViolations.containsAll(violations));
    }

    @Test
    @DisplayName("Тест put-эндпоинта с передачей пациента без идентификатора")
    public void testPutEndpointWithFailedUpdatingPatientWithoutId() throws Exception{
        validPatientDto.setId(null);
        String result = mockMvc.perform(put("/patient")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validPatientDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ValidationErrorResponse response = objectMapper.readValue(result, ValidationErrorResponse.class);
        List<Violation> responseViolations = response.getViolations();
        Violation violationId = new Violation("updatePatient.patientDto.id", "The id must be not null");
        assertTrue(responseViolations.contains(violationId));
    }

    @Test
    @DisplayName("Тест штатной работы delete-эндпоинта")
    public void testDeleteEndpointWithSuccessfulDeletingPatient() throws Exception{
        String result = mockMvc.perform(delete("/patient/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Optional<Patient> optionalPatient = patientRepository.findById(1L);
        assertTrue(optionalPatient.isEmpty());
    }

    @Test
    @DisplayName("Тест delete-эндпоинта с передачей id несуществующего пациента")
    public void testDeleteEndpointWithNotFoundPatient() throws Exception{
        String result = mockMvc.perform(delete("/patient/1000")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse response = objectMapper.readValue(result, ErrorResponse.class);
        assertEquals("Patient with id 1000 not found", response.getMessage());
    }
}
