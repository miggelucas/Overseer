package com.overseer.service;

import com.overseer.model.EventLog;

import java.util.List;

public interface EventService {
    EventLog save(EventLog eventLog);
    List<EventLog> getAllLogs();
    EventLog getById(Long id);
    EventLog update(EventLog eventLog, Long id  );
    void deleteById(Long id);
    void deleteAll();
}
