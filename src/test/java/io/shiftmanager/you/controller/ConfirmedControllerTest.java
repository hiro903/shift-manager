package io.shiftmanager.you.controller;

import io.shiftmanager.you.model.Confirmed;
import io.shiftmanager.you.service.ConfirmedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConfirmedController.class)
public class ConfirmedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfirmedService confirmedService;

    @Autowired
    private ObjectMapper objectMapper;

    private Confirmed testConfirmed;
    private List<Confirmed> testConfirmedList;

    @BeforeEach
    void setUp() {
        testConfirmed = new Confirmed();
        testConfirmed.setConfirmedId(1L);
        testConfirmed.setUserId(1L);
        testConfirmed.setShiftId(1L);
        testConfirmed.setConfirmedDate(LocalDate.now());
        testConfirmed.setTimezone(Confirmed.Timezone.morning);
        testConfirmed.setCreatedAt(LocalDateTime.now());
        testConfirmed.setUpdatedAt(LocalDateTime.now());

        Confirmed confirmed2 = new Confirmed();
        confirmed2.setConfirmedId(2L);
        confirmed2.setUserId(1L);
        confirmed2.setShiftId(2L);
        confirmed2.setConfirmedDate(LocalDate.now().plusDays(1));
        confirmed2.setTimezone(Confirmed.Timezone.afternoon);
        confirmed2.setCreatedAt(LocalDateTime.now());
        confirmed2.setUpdatedAt(LocalDateTime.now());

        testConfirmedList = Arrays.asList(testConfirmed, confirmed2);
    }

    @Test
    void getConfirmedById_Success() throws Exception {
        when(confirmedService.getConfirmedById(1L)).thenReturn(testConfirmed);

        mockMvc.perform(get("/api/confirmed/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.confirmed").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.shiftId").value(1))
                .andExpect(jsonPath("$.timezone").value("morning"));
    }

    @Test
    void getConfirmedById_NotFound() throws Exception {
        when(confirmedService.getConfirmedById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/confirmed/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getConfirmedByUserId_Success() throws Exception {
        when(confirmedService.getConfirmedByUserId(1L)).thenReturn(testConfirmedList);

        mockMvc.perform(get("/api/confirmed/user/1")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].confirmedId").value(1))
               .andExpect(jsonPath("$[1].confirmedId").value(2))
               .andExpect(jsonPath("$[0].timezone").value("morning"))
               .andExpect(jsonPath("$[1].timezone").value("afternoon"));
    }

    @Test
    void createConfirmed_Success() throws Exception {
        when(confirmedService.createConfirmed(any(Confirmed.class))).thenReturn(testConfirmed);

        mockMvc.perform(post("/api/confirmed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testConfirmed)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.confirmedId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.timezone").value("morning"));
    }
    @Test
    void  deleteCnfirmed_Success() throws Exception {
        doNothing().when(confirmedService).deleteConfirmed(1L);

        mockMvc.perform(delete("/api/confirmed/1"))
                .andExpect(status().isNoContent());
    }
}

