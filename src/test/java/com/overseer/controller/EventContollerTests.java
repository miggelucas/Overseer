package com.overseer.controller;

import com.overseer.model.EventLog;
import com.overseer.service.EventService;
import com.overseer.util.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize mocks", e);
        }
    }

    @Test
    void testCreateLog() throws Exception {
        EventLog eventLog = new EventLog();
        eventLog.setId(1L);
        eventLog.setApplication("AnyApplication");
        eventLog.setLevel(Level.INFO);
        eventLog.setMessage("Test log");
        when(eventService.save(any(EventLog.class))).thenReturn(eventLog);

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"message\":\"Test log\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.application").value("AnyApplication"))
                .andExpect(jsonPath("$.level").value("INFO"))
                .andExpect(jsonPath("$.message").value("Test log"));

        verify(eventService, times(1)).save(any(EventLog.class));
    }

    @Test
    void testGetAllEvent() throws Exception {
        when(eventService.getAllLogs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/event"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(eventService, times(1)).getAllLogs();
    }

    @Test
    void testGetEventById() throws Exception {
        EventLog eventLog = new EventLog();
        eventLog.setId(1L);
        when(eventService.getById(1L)).thenReturn(eventLog);

        mockMvc.perform(get("/event/id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(eventService, times(1)).getById(1L);
    }

    @Test
    void testGetLogsByApplication() throws Exception {
        String applicationName = "TestApp";
        EventLog eventLog = new EventLog();
        eventLog.setId(1L);
        eventLog.setApplication(applicationName);
        eventLog.setLevel(Level.INFO);
        eventLog.setMessage("Test log");

        when(eventService.getByApplication(applicationName)).thenReturn(Collections.singletonList(eventLog));

        mockMvc.perform(get("/event/application/" + applicationName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].application").value(applicationName))
                .andExpect(jsonPath("$[0].level").value("INFO"))
                .andExpect(jsonPath("$[0].message").value("Test log"));

        verify(eventService, times(1)).getByApplication(applicationName);
    }

    @Test
    void testGetLogsByApplicationNotFound() throws Exception {
        String applicationName = "NonExistentApp";

        when(eventService.getByApplication(applicationName)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/event/application/" + applicationName))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("No logs found for application: " + applicationName));

        verify(eventService, times(1)).getByApplication(applicationName);
    }

    @Test
    void testUpdateEvent() throws Exception {
        EventLog eventLog = new EventLog();
        eventLog.setId(1L);
        eventLog.setApplication("AnyApplication");
        eventLog.setLevel(Level.INFO);
        eventLog.setMessage("Updated log");
        when(eventService.update(any(EventLog.class), eq(1L))).thenReturn(eventLog);

        mockMvc.perform(put("/event/id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"message\":\"Updated log\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.application").value("AnyApplication"))
                .andExpect(jsonPath("$.level").value("INFO"))
                .andExpect(jsonPath("$.message").value("Updated log"));

        verify(eventService, times(1)).update(any(EventLog.class), eq(1L));
    }

    @Test
    void testDeleteEventById() throws Exception {
        doNothing().when(eventService).deleteById(1L);

        mockMvc.perform(delete("/event/id=1"))
                .andExpect(status().isOk());

        verify(eventService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAllEvents() throws Exception {
        doNothing().when(eventService).deleteAll();

        mockMvc.perform(delete("/event"))
                .andExpect(status().isOk());

        verify(eventService, times(1)).deleteAll();
    }
}