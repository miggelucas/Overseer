package com.personal.loghub.service;

import com.personal.loghub.model.LogEntry;

import java.util.List;

public interface LogService {
    LogEntry save(LogEntry logEntry);
    List<LogEntry> getAllLogs();
    LogEntry getById(Long id);
    LogEntry update(LogEntry logEntry, Long id  );
    void deleteById(Long id);
    void deleteAll();
}
