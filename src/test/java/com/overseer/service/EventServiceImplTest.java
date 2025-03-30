package com.overseer.service;

import com.overseer.model.EventLog;
import com.overseer.repository.EventRepository;
import com.overseer.util.Level;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl logServiceImpl;

    public EventServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldReturnSavedLogEntry() {
        // Arrange
        EventLog eventLog = EventLog.builder()
                .application("TestApp")
                .level(Level.INFO)
                .message("This is a test log.")
                .build();

        EventLog savedEventLog = EventLog.builder()
                .id(1L)
                .application("TestApp")
                .level(Level.ERROR)
                .message("This is a test log.")
                .timestamp(LocalDateTime.now())
                .build();

        when(eventRepository.save(any(EventLog.class))).thenReturn(savedEventLog);

        // Act
        EventLog result = logServiceImpl.save(eventLog);

        // Assert
        assertNotNull(result);
        assertEquals(savedEventLog.getId(), result.getId());
        assertEquals(savedEventLog.getApplication(), result.getApplication());
        assertEquals(savedEventLog.getLevel(), result.getLevel());
        assertEquals(savedEventLog.getMessage(), result.getMessage());
        assertNotNull(result.getTimestamp());
        verify(eventRepository, times(1)).save(any(EventLog.class));
    }

    @Test
    void getAllLogs_ShouldReturnListOfLogEntries() {
        // Arrange
        List<EventLog> logEntries = List.of(
                EventLog.builder().id(1L).application("App1").level(Level.INFO).message("Log 1").timestamp(LocalDateTime.now()).build(),
                EventLog.builder().id(2L).application("App2").level(Level.ERROR).message("Log 2").timestamp(LocalDateTime.now()).build()
        );

        when(eventRepository.findAll()).thenReturn(logEntries);

        // Act
        List<EventLog> result = logServiceImpl.getAllLogs();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("App1", result.get(0).getApplication());
        assertEquals("App2", result.get(1).getApplication());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnLogEntry_WhenIdExists() {
        // Arrange
        EventLog eventLog = EventLog.builder()
                .id(1L)
                .application("TestApp")
                .level(Level.INFO)
                .message("This is a test log.")
                .timestamp(LocalDateTime.now())
                .build();

        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(eventLog));

        // Act
        EventLog result = logServiceImpl.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(eventLog.getId(), result.getId());
        assertEquals(eventLog.getApplication(), result.getApplication());
        assertEquals(eventLog.getLevel(), result.getLevel());
        assertEquals(eventLog.getMessage(), result.getMessage());
        assertEquals(eventLog.getTimestamp(), result.getTimestamp());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act
        EventLog result = logServiceImpl.getById(1L);

        // Assert
        assertNull(result);
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void getByApplication_ShouldReturnListOfLogEntries_WhenApplicationExists() {
        // Arrange
        String applicationName = "TestApp";
        List<EventLog> logEntries = List.of(
                EventLog.builder().id(1L).application("OtherApp").level(Level.INFO).message("Log 1").timestamp(LocalDateTime.now()).build(),
                EventLog.builder().id(2L).application(applicationName).level(Level.ERROR).message("Log 2").timestamp(LocalDateTime.now()).build(),
                EventLog.builder().id(3L).application(applicationName).level(Level.ERROR).message("Log 3").timestamp(LocalDateTime.now()).build(),
                EventLog.builder().id(4L).application("AnotherApp").level(Level.ERROR).message("Log 4").timestamp(LocalDateTime.now()).build()
        );

        when(eventRepository.findByApplication(applicationName)).thenReturn(logEntries);

        // Act
        List<EventLog> result = logServiceImpl.getByApplication(applicationName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(applicationName, result.get(0).getApplication());
        assertEquals(applicationName, result.get(1).getApplication());
        verify(eventRepository, times(1)).findByApplication(applicationName);
    }

    @Test
    void getByApplication_ShouldReturnEmptyList_WhenApplicationDoesNotExist() {
        // Arrange
        String applicationName = "NonExistentApp";

        when(eventRepository.findByApplication(applicationName)).thenReturn(Collections.emptyList());

        // Act
        List<EventLog> result = logServiceImpl.getByApplication(applicationName);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(eventRepository, times(1)).findByApplication(applicationName);
    }

    @Test
    void update_ShouldReturnUpdatedLogEntry_WhenIdExists() {
        // Arrange
        Long id = 1L;
        EventLog existingEventLog = EventLog.builder()
                .id(id)
                .application("ExistingApp")
                .level(Level.DEBUG)
                .message("Existing log message")
                .timestamp(LocalDateTime.now().minusDays(1))
                .build();

        EventLog updatedEventLog = EventLog.builder()
                .application("UpdatedApp")
                .level(Level.INFO)
                .message("Updated log message")
                .timestamp(LocalDateTime.now())
                .build();

        EventLog savedEventLog = EventLog.builder()
                .id(id)
                .application("UpdatedApp")
                .level(Level.INFO)
                .message("Updated log message")
                .timestamp(updatedEventLog.getTimestamp())
                .build();

        when(eventRepository.findById(id)).thenReturn(java.util.Optional.of(existingEventLog));
        when(eventRepository.save(any(EventLog.class))).thenReturn(savedEventLog);

        // Act
        EventLog result = logServiceImpl.update(updatedEventLog, id);

        // Assert
        assertNotNull(result);
        assertEquals(savedEventLog.getId(), result.getId());
        assertEquals(savedEventLog.getApplication(), result.getApplication());
        assertEquals(savedEventLog.getLevel(), result.getLevel());
        assertEquals(savedEventLog.getMessage(), result.getMessage());
        assertEquals(savedEventLog.getTimestamp(), result.getTimestamp());
        verify(eventRepository, times(1)).findById(id);
        verify(eventRepository, times(1)).save(any(EventLog.class));
    }

    @Test
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        Long id = 1L;
        EventLog updatedEventLog = EventLog.builder()
                .application("UpdatedApp")
                .level(Level.INFO)
                .message("Updated log message")
                .timestamp(LocalDateTime.now())
                .build();

        when(eventRepository.findById(id)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> logServiceImpl.update(updatedEventLog, id));
        verify(eventRepository, times(1)).findById(id);
        verify(eventRepository, never()).save(any(EventLog.class));
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete_WhenIdExists() {
        // Arrange
        Long id = 1L;

        // Act
        logServiceImpl.deleteById(id);

        // Assert
        verify(eventRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_ShouldThrowException_WhenRepositoryThrowsError() {
        // Arrange
        Long id = 1L;
        doThrow(new RuntimeException("Database error")).when(eventRepository).deleteById(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> logServiceImpl.deleteById(id));
        verify(eventRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAll_ShouldCallRepositoryDeleteAll() {
        // Act
        logServiceImpl.deleteAll();

        // Assert
        verify(eventRepository, times(1)).deleteAll();
    }

    @Test
    void deleteAll_ShouldThrowException_WhenRepositoryThrowsError() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(eventRepository).deleteAll();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> logServiceImpl.deleteAll());
        verify(eventRepository, times(1)).deleteAll();
    }
}