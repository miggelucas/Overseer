package com.personal.loghub.service;

import com.personal.loghub.model.LogEntry;
import com.personal.loghub.repository.LogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

class LogServiceImplTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogServiceImpl logServiceImpl;

    public LogServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldReturnSavedLogEntry() {
        // Arrange
        LogEntry logEntry = LogEntry.builder()
                .application("TestApp")
                .level("INFO")
                .message("This is a test log.")
                .build();

        LogEntry savedLogEntry = LogEntry.builder()
                .id(1L)
                .application("TestApp")
                .level("INFO")
                .message("This is a test log.")
                .timestamp(LocalDateTime.now())
                .build();

        when(logRepository.save(any(LogEntry.class))).thenReturn(savedLogEntry);

        // Act
        LogEntry result = logServiceImpl.save(logEntry);

        // Assert
        assertNotNull(result);
        assertEquals(savedLogEntry.getId(), result.getId());
        assertEquals(savedLogEntry.getApplication(), result.getApplication());
        assertEquals(savedLogEntry.getLevel(), result.getLevel());
        assertEquals(savedLogEntry.getMessage(), result.getMessage());
        assertNotNull(result.getTimestamp());
        verify(logRepository, times(1)).save(any(LogEntry.class));
    }

    @Test
    void getAllLogs_ShouldReturnListOfLogEntries() {
        // Arrange
        List<LogEntry> logEntries = List.of(
                LogEntry.builder().id(1L).application("App1").level("INFO").message("Log 1").timestamp(LocalDateTime.now()).build(),
                LogEntry.builder().id(2L).application("App2").level("ERROR").message("Log 2").timestamp(LocalDateTime.now()).build()
        );

        when(logRepository.findAll()).thenReturn(logEntries);

        // Act
        List<LogEntry> result = logServiceImpl.getAllLogs();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("App1", result.get(0).getApplication());
        assertEquals("App2", result.get(1).getApplication());
        verify(logRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnLogEntry_WhenIdExists() {
        // Arrange
        LogEntry logEntry = LogEntry.builder()
                .id(1L)
                .application("TestApp")
                .level("INFO")
                .message("This is a test log.")
                .timestamp(LocalDateTime.now())
                .build();

        when(logRepository.findById(1L)).thenReturn(java.util.Optional.of(logEntry));

        // Act
        LogEntry result = logServiceImpl.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(logEntry.getId(), result.getId());
        assertEquals(logEntry.getApplication(), result.getApplication());
        assertEquals(logEntry.getLevel(), result.getLevel());
        assertEquals(logEntry.getMessage(), result.getMessage());
        assertEquals(logEntry.getTimestamp(), result.getTimestamp());
        verify(logRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        when(logRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act
        LogEntry result = logServiceImpl.getById(1L);

        // Assert
        assertNull(result);
        verify(logRepository, times(1)).findById(1L);
    }

    @Test
    void update_ShouldReturnUpdatedLogEntry_WhenIdExists() {
        // Arrange
        Long id = 1L;
        LogEntry existingLogEntry = LogEntry.builder()
                .id(id)
                .application("ExistingApp")
                .level("DEBUG")
                .message("Existing log message")
                .timestamp(LocalDateTime.now().minusDays(1))
                .build();

        LogEntry updatedLogEntry = LogEntry.builder()
                .application("UpdatedApp")
                .level("INFO")
                .message("Updated log message")
                .timestamp(LocalDateTime.now())
                .build();

        LogEntry savedLogEntry = LogEntry.builder()
                .id(id)
                .application("UpdatedApp")
                .level("INFO")
                .message("Updated log message")
                .timestamp(updatedLogEntry.getTimestamp())
                .build();

        when(logRepository.findById(id)).thenReturn(java.util.Optional.of(existingLogEntry));
        when(logRepository.save(any(LogEntry.class))).thenReturn(savedLogEntry);

        // Act
        LogEntry result = logServiceImpl.update(updatedLogEntry, id);

        // Assert
        assertNotNull(result);
        assertEquals(savedLogEntry.getId(), result.getId());
        assertEquals(savedLogEntry.getApplication(), result.getApplication());
        assertEquals(savedLogEntry.getLevel(), result.getLevel());
        assertEquals(savedLogEntry.getMessage(), result.getMessage());
        assertEquals(savedLogEntry.getTimestamp(), result.getTimestamp());
        verify(logRepository, times(1)).findById(id);
        verify(logRepository, times(1)).save(any(LogEntry.class));
    }

    @Test
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        Long id = 1L;
        LogEntry updatedLogEntry = LogEntry.builder()
                .application("UpdatedApp")
                .level("INFO")
                .message("Updated log message")
                .timestamp(LocalDateTime.now())
                .build();

        when(logRepository.findById(id)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> logServiceImpl.update(updatedLogEntry, id));
        verify(logRepository, times(1)).findById(id);
        verify(logRepository, never()).save(any(LogEntry.class));
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete_WhenIdExists() {
        // Arrange
        Long id = 1L;

        // Act
        logServiceImpl.deleteById(id);

        // Assert
        verify(logRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_ShouldThrowException_WhenRepositoryThrowsError() {
        // Arrange
        Long id = 1L;
        doThrow(new RuntimeException("Database error")).when(logRepository).deleteById(id);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> logServiceImpl.deleteById(id));
        verify(logRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAll_ShouldCallRepositoryDeleteAll() {
        // Act
        logServiceImpl.deleteAll();

        // Assert
        verify(logRepository, times(1)).deleteAll();
    }

    @Test
    void deleteAll_ShouldThrowException_WhenRepositoryThrowsError() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(logRepository).deleteAll();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> logServiceImpl.deleteAll());
        verify(logRepository, times(1)).deleteAll();
    }
}