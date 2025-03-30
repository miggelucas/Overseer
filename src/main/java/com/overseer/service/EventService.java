package com.overseer.service;

import com.overseer.model.EventLog;

import java.util.List;

public interface EventService {
    EventLog save(EventLog eventLog);
    List<EventLog> getAllLogs();
    List<EventLog> getByApplication(String applicationName);
    EventLog getById(Long id);
    EventLog update(EventLog eventLog, Long id  );
    void deleteById(Long id);
    void deleteAll();
}
