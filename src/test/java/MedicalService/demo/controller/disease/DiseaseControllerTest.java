package MedicalService.demo.controller.disease;

import MedicalService.demo.SpringContextTest;
import MedicalService.demo.exception.response.ErrorResponse;
import MedicalService.demo.exception.response.ValidationErrorResponse;
import MedicalService.demo.exception.response.Violation;
import MedicalService.demo.mapper.disease.DiseaseMapper;
import MedicalService.demo.model.dto.disease.DiseaseDto;
import MedicalService.demo.model.entity.disease.Disease;
import MedicalService.demo.repository.DiseaseRepository;
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
@DisplayName("Тестирование DiseaseController")
@Sql(scripts = {"/database/fill_patient.sql", "/database/fill_disease_and_icd.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS,
        config = @SqlConfig(encoding = "utf-8")
)
public class DiseaseControllerTest extends SpringContextTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private DiseaseMapper diseaseMapper;

    private DiseaseDto validDiseaseDto;
    private DiseaseDto fullInvalidDiseaseDto;
    private List<Violation> violations = new ArrayList<>();

    @BeforeEach
    public void init() {
        validDiseaseDto = DiseaseDto.builder()
                .starting(LocalDate.now().minusDays(10))
                .code("A00.0")
                .description("description")
                .patientId(1)
                .build();

        LocalDate invalidEndingDate = LocalDate.now().plusDays(10);
        fullInvalidDiseaseDto = DiseaseDto.builder()
                .ending(invalidEndingDate)
                .code(" ")
                .description(" ")
                .build();

        violations.addAll(List.of(
                new Violation("code", "The code '" + fullInvalidDiseaseDto.getCode() + "' must be not blank"),
                new Violation("starting", "The start of disease must be not null"),
                new Violation("ending", "The ending '"+ fullInvalidDiseaseDto.getEnding() +"' must not be in the future"),
                new Violation("description", "The description '" +fullInvalidDiseaseDto.getDescription()+ "' must be not blank")
        ));

    }

    @Test
    @DisplayName("Тест штатной работы post-эндпоинта")
    public void testPostEndpointWithSuccessfulCreationDisease() throws Exception {
        String result = mockMvc.perform(post("/patient/1/disease")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validDiseaseDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        DiseaseDto diseaseDto = objectMapper.readValue(result, DiseaseDto.class);
        Optional<Disease> OptionalDiseaseFromDB = diseaseRepository.findById(diseaseDto.getId());

        assertTrue(OptionalDiseaseFromDB.isPresent());
        Disease diseaseFromDB = OptionalDiseaseFromDB.get();
        assertEquals(diseaseMapper.toDto(diseaseFromDB), diseaseDto);
        validDiseaseDto.setId(diseaseFromDB.getId());
        assertEquals(validDiseaseDto, diseaseMapper.toDto(diseaseFromDB));
    }

    @Test
    @DisplayName("Тест post-эндпоинта с передачей полностью невалидного заболевания")
    public void testPostEndpointWithFailedCreationFullInvalidDisease() throws Exception{
        String result = mockMvc.perform(post("/patient/1/disease")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(fullInvalidDiseaseDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ValidationErrorResponse response = objectMapper.readValue(result, ValidationErrorResponse.class);
        List<Violation> responseViolations = response.getViolations();

        assertTrue(responseViolations.containsAll(violations));
    }

    @Test
    @DisplayName("Тест post-эндпоинта с передачей заболевания с идентификатором")
    public void testPostEndpointWithFailedCreationDisease() throws Exception{
        validDiseaseDto.setId(Long.MAX_VALUE);
        String result = mockMvc.perform(post("/patient/1/disease")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validDiseaseDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ValidationErrorResponse response = objectMapper.readValue(result, ValidationErrorResponse.class);
        List<Violation> responseViolations = response.getViolations();
        Violation violationId = new Violation("createDisease.diseaseDto.id", "The id '" +Long.MAX_VALUE + "' must be null");
        assertTrue(responseViolations.contains(violationId));
    }

    @Test
    @DisplayName("Тест штатной работы get-эндпоинта")
    public void testGetEndpointWithSuccessfulGettingDisease() throws Exception{
        String result = mockMvc.perform(get("/patient/1/disease/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        DiseaseDto diseaseDto = objectMapper.readValue(result, DiseaseDto.class);
        assertEquals(diseaseDto.getId(), 1);
        assertEquals(diseaseDto.getPatientId(), 1);
    }


    @Test
    @DisplayName("Тест get-эндпоинта с передачей id несуществующего пациента")
    public void testGetEndpointWithNotFoundPatient() throws Exception{
        String result = mockMvc.perform(get("/patient/100/disease/1")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse response = objectMapper.readValue(result, ErrorResponse.class);
        assertEquals("Disease with id 1 not found in the patient with id 100", response.getMessage());
    }

    @Test
    @DisplayName("Тест get-эндпоинта с передачей id несуществующего заболевания")
    public void testGetEndpointWithNotFoundDisease() throws Exception{
        String result = mockMvc.perform(get("/patient/1/disease/100")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse response = objectMapper.readValue(result, ErrorResponse.class);
        assertEquals("Disease with id 100 not found in the patient with id 1", response.getMessage());
    }

    @Test
    @DisplayName("Тест штатной работы put-эндпоинта")
    public void testPutEndpointWithSuccessfulUpdatingDisease() throws Exception {
        validDiseaseDto.setId(1L);
        validDiseaseDto.setPatientId(1L);

        String result = mockMvc.perform(put("/patient/1/disease")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validDiseaseDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        DiseaseDto diseaseDto = objectMapper.readValue(result, DiseaseDto.class);
        Optional<Disease> OptionalDiseaseFromDB = diseaseRepository.findById(diseaseDto.getId());

        assertTrue(OptionalDiseaseFromDB.isPresent());
        Disease diseaseFromDB = OptionalDiseaseFromDB.get();
        assertEquals(diseaseMapper.toDto(diseaseFromDB), diseaseDto);
        assertEquals(validDiseaseDto, diseaseMapper.toDto(diseaseFromDB));
    }

    @Test
    @DisplayName("Тест put-эндпоинта с передачей полностью невалидного заболевания")
    public void testPutEndpointWithFailedUpdatingDisease() throws Exception{
        String result = mockMvc.perform(put("/patient/1/disease")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(fullInvalidDiseaseDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ValidationErrorResponse response = objectMapper.readValue(result, ValidationErrorResponse.class);
        List<Violation> responseViolations = response.getViolations();

        assertTrue(responseViolations.containsAll(violations));
    }

    @Test
    @DisplayName("Тест put-эндпоинта с передачей заболевания без идентификатора")
    public void testPutEndpointWithFailedUpdatingDiseaseWithoutId() throws Exception{
        validDiseaseDto.setId(null);
        String result = mockMvc.perform(put("/patient/1/disease")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validDiseaseDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ValidationErrorResponse response = objectMapper.readValue(result, ValidationErrorResponse.class);
        List<Violation> responseViolations = response.getViolations();
        Violation violationId = new Violation("updateDisease.diseaseDto.id", "The id must be not null");
        assertTrue(responseViolations.contains(violationId));
    }

    @Test
    @DisplayName("Тест штатной работы delete-эндпоинта")
    public void testDeleteEndpointWithSuccessfulDeletingDisease() throws Exception{
        String result = mockMvc.perform(delete("/patient/2/disease/2")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Optional<Disease> optionalPatient = diseaseRepository.findById(2L);
        assertTrue(optionalPatient.isEmpty());
    }

    @Test
    @DisplayName("Тест delete-эндпоинта с передачей id несуществующего заболвения")
    public void testDeleteEndpointWithNotFoundDisease() throws Exception{
        String result = mockMvc.perform(delete("/patient/1/disease/1000")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErrorResponse response = objectMapper.readValue(result, ErrorResponse.class);
        assertEquals("Disease with id 1000 not found in the patient with id 1", response.getMessage());
    }
}

