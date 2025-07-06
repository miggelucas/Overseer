package com.overseer.service;

import com.overseer.model.EventLog;
import com.overseer.repository.EventRepository;
import com.overseer.util.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private EventLog eventLog;

    @BeforeEach
    void setUp() {
        eventLog = EventLog.builder()
                .application("TestApp")
                .id(1L)
                .level(Level.INFO)
                .message("This is a test log.")
                .build();
    }

    @Test
    void save_shouldCallRepositoryAndReturnSavedLog() {
        // Arrange
        EventLog savedEventLog = EventLog.builder()
                .id(1L)
                .application("TestApp")
                .level(Level.INFO)
                .message("This is a test log.")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(eventRepository.save(any(EventLog.class))).thenReturn(savedEventLog);

        // Act
        EventLog result = eventService.save(eventLog);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TestApp", result.getApplication());
        assertEquals(Level.INFO, result.getLevel());
        assertNotNull(result.getCreatedAt());
        verify(eventRepository, times(1)).save(eventLog);
    }

    @Test
    void getAllLogs_shouldReturnListOfLogs() {
        // Arrange
        List<EventLog> logEntries = List.of(
                EventLog.builder().id(1L).application("App1").level(Level.INFO).message("Log 1").build(),
                EventLog.builder().id(2L).application("App2").level(Level.ERROR).message("Log 2").build()
        );
        when(eventRepository.findAll()).thenReturn(logEntries);

        // Act
        List<EventLog> result = eventService.getAllLogs();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void getById_shouldReturnLog_whenIdExists() {
        // Arrange
        eventLog.setId(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(eventLog));

        // Act
        EventLog result = eventService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenIdDoesNotExist() {
        // Arrange
        long nonExistentId = 99L;
        when(eventRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> eventService.getById(nonExistentId));
        verify(eventRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void update_shouldReturnUpdatedLog_whenIdExists() {
        // Arrange
        Long id = 1L;
        Instant initialCreation = Instant.now().minusSeconds(100);
        EventLog existingLog = EventLog.builder()
                .id(id)
                .application("OldApp")
                .level(Level.DEBUG)
                .message("Old message")
                .createdAt(initialCreation) // Data de criação antiga
                .updatedAt(initialCreation)
                .build();

        EventLog updateDetails = EventLog.builder()
                .application("NewApp")
                .level(Level.WARNING)
                .message("New message")
                .build();

        when(eventRepository.findById(id)).thenReturn(Optional.of(existingLog));
        when(eventRepository.save(any(EventLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EventLog result = eventService.update(updateDetails, id);

        // Assert
        ArgumentCaptor<EventLog> eventLogCaptor = ArgumentCaptor.forClass(EventLog.class);
        verify(eventRepository, times(1)).save(eventLogCaptor.capture());
        EventLog capturedLog = eventLogCaptor.getValue();

        assertEquals(id, capturedLog.getId());
        assertEquals("NewApp", capturedLog.getApplication());
        assertEquals(Level.WARNING, capturedLog.getLevel());
        assertEquals("New message", capturedLog.getMessage());
        assertEquals(initialCreation, capturedLog.getCreatedAt());

        verify(eventRepository, times(1)).findById(id);
    }

    @Test
    void update_shouldThrowException_whenIdDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> eventService.update(eventLog, id));
        verify(eventRepository, times(1)).findById(id);
        verify(eventRepository, never()).save(any(EventLog.class)); // Garante que o save nunca foi chamado
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        Long id = 1L;
        doNothing().when(eventRepository).deleteById(id);

        // Act
        eventService.deleteById(id);

        // Assert
        verify(eventRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAll_shouldCallRepositoryDeleteAll() {
        // Act
        eventService.deleteAll();

        // Assert
        verify(eventRepository, times(1)).deleteAll();
    }
}