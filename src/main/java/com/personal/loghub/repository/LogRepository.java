package com.personal.loghub.repository;

import com.personal.loghub.model.LogEntry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class LogRepository {

    private final List<LogEntry> logEntries;

    public LogRepository() {
        this.logEntries = new CopyOnWriteArrayList<>();
    }

    public void save(LogEntry logEntry) {
        logEntries.add(logEntry);
        System.out.println("Log salvo: " + logEntry);
    }

    public List<LogEntry> findAll() {
        System.out.println("Log encontrado: " + logEntries);
        return logEntries;
    }

}