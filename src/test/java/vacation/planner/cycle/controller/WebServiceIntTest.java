package vacation.planner.cycle.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebServiceIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCurrentCycle() throws Exception {

        mockMvc.perform(get("/user/me/current-cycle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startDate").value("2025-03-04"));
    }

    @Test
    void testGetUserPeriodForecast() throws Exception {

        mockMvc.perform(get("/user/me/period-forecast")
                        .param("startDate", "2025-03-01")
                        .param("endDate", "2025-03-10"))
                .andExpect(status().isOk());
    }

    @Test
    void testFailOnGetUser() throws Exception {

        mockMvc.perform(get("/user/not_me"))
                .andExpect(status().is(404));
    }

    @Test
    void testGetUser() throws Exception {

        mockMvc.perform(get("/user/me"))
                .andExpect(jsonPath("$.userName").value("me"));
    }
}
