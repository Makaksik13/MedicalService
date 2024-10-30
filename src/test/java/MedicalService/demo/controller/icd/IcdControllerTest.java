package MedicalService.demo.controller.icd;

import MedicalService.demo.SpringContextTest;
import MedicalService.demo.helper.RestResponsePage;
import MedicalService.demo.model.dto.icd.IcdDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@AutoConfigureMockMvc
@DisplayName("Тестирование IcdController")
@Sql(scripts = {"/database/fill_patient.sql", "/database/fill_disease_and_icd.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS,
        config = @SqlConfig(encoding = "utf-8")
)
public class IcdControllerTest extends SpringContextTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${page.icd.size}")
    private int sizeOfPage;

    @Test
    @DisplayName("Тест штатной работы get-эндпоинта")
    public void testGetEndpointWithSuccessfulGettingIcds() throws Exception{
        String result = mockMvc.perform(get("/dictionary/mkb10")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        RestResponsePage<IcdDto> icdDtos = objectMapper.readValue(result, new TypeReference<>() {});

        assertEquals(sizeOfPage, icdDtos.getSize());
    }
}
