package com.personal.loghub.service;

import com.personal.loghub.model.LogEntry;
import com.personal.loghub.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public LogEntry save(LogEntry logEntry) {
        logRepository.save(logEntry);
        return logEntry;
    }

    public List<LogEntry> getAllLogs() {
        return logRepository.findAll();
    }
}
