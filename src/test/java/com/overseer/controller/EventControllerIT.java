package com.overseer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overseer.model.EventLog;
import com.overseer.repository.EventRepository;
import com.overseer.util.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EventControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private EventLog eventLog;

    @BeforeEach
    void setUp() {
        // Limpa o repositório antes de cada teste para garantir isolamento
        eventRepository.deleteAll();

        // Cria um objeto base para os testes
        eventLog = EventLog.builder()
                .application("test-app")
                .level(Level.INFO)
                .message("This is a test log.")
                .build();
    }

    @Test
    void createLog_shouldCreateAndReturnLogWithTimestamps() throws Exception {
        // Act
        ResultActions resultActions = mockMvc.perform(post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventLog)));

        // Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.application", is(eventLog.getApplication())))
                .andExpect(jsonPath("$.level", is(eventLog.getLevel().toString())))
                .andExpect(jsonPath("$.message", is(eventLog.getMessage())))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", notNullValue()));

        // Verifica
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        EventLog createdLog = objectMapper.readValue(responseBody, EventLog.class);
        assertEquals(createdLog.getCreatedAt(), createdLog.getUpdatedAt());
    }

    @Test
    void updateLog_shouldUpdateAndChangeUpdatedAt() throws Exception {
        EventLog savedLog = eventRepository.save(eventLog);
        Instant initialCreatedAt = savedLog.getCreatedAt();
        Instant initialUpdatedAt = savedLog.getUpdatedAt();

        Thread.sleep(10);

        EventLog updatedDetails = EventLog.builder()
                .application("updated-app")
                .level(Level.ERROR)
                .message("This is an updated message.")
                .build();

        // Act
        ResultActions resultActions = mockMvc.perform(put("/event/id={id}", savedLog.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)));

        // Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedLog.getId().intValue())))
                .andExpect(jsonPath("$.message", is(updatedDetails.getMessage())))
                .andExpect(jsonPath("$.level", is(updatedDetails.getLevel().toString())))
                .andExpect(jsonPath("$.createdAt", is(initialCreatedAt.toString())))
                .andExpect(jsonPath("$.updatedAt", not(is(initialUpdatedAt.toString()))));

        // Assert
        EventLog fromDb = eventRepository.findById(savedLog.getId()).get();
        assertEquals(initialCreatedAt, fromDb.getCreatedAt());
        assertTrue(fromDb.getUpdatedAt().isAfter(initialUpdatedAt));
    }

    @Test
    void getPinById_whenLogExists_shouldReturnLog() throws Exception {
        // Arrange
        EventLog savedLog = eventRepository.save(eventLog);

        // Act & Assert
        mockMvc.perform(get("/event/id={id}", savedLog.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedLog.getId().intValue())));
    }

    @Test
    void getPinById_whenLogDoesNotExist_shouldReturn500() throws Exception {
        // Arrange: Um ID que não existe
        long nonExistentId = 999L;

        // Act & Assert
        mockMvc.perform(get("/event/id={id}", nonExistentId))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deletePinById_shouldRemoveLogFromDatabase() throws Exception {
        // Arrange
        EventLog savedLog = eventRepository.save(eventLog);
        assertTrue(eventRepository.findById(savedLog.getId()).isPresent());

        // Act
        mockMvc.perform(delete("/event/id={id}", savedLog.getId()))
                .andExpect(status().isOk());

        // Assert
        assertFalse(eventRepository.findById(savedLog.getId()).isPresent());
    }
}