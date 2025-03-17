package com.personal.loghub.service;

import com.personal.loghub.model.LogEntry;
import com.personal.loghub.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Override
    public LogEntry save(LogEntry logEntry) {
        logEntry.setTimestamp(LocalDateTime.now());
        return logRepository.save(logEntry);
    }

    @Override
    public List<LogEntry> getAllLogs() {
        return (List<LogEntry>) logRepository.findAll();
    }

    @Override
    public LogEntry getById(Long id) {
        return logRepository.findById(id).orElse(null);
    }

    @Override
    public LogEntry update(LogEntry logEntry, Long id) {
        LogEntry log = getById(id);
        log.setApplication(logEntry.getApplication());
        log.setLevel(logEntry.getLevel());
        log.setMessage(logEntry.getMessage());
        log.setTimestamp(logEntry.getTimestamp());

        return save(log);
    }

    @Override
    public void deleteById(Long id) {
        logRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        logRepository.deleteAll();
    }
}
