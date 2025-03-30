package com.overseer.service;

import com.overseer.model.EventLog;
import com.overseer.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public EventLog save(EventLog eventLog) {
        eventLog.setTimestamp(LocalDateTime.now());
        return eventRepository.save(eventLog);
    }

    @Override
    public List<EventLog> getAllLogs() {
        return (List<EventLog>) eventRepository.findAll();
    }

    @Override
    public EventLog getById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public List<EventLog> getByApplication(String applicationName) {
        return null;
    }

    @Override
    public EventLog update(EventLog eventLog, Long id) {
        EventLog log = getById(id);
        log.setApplication(eventLog.getApplication());
        log.setLevel(eventLog.getLevel());
        log.setMessage(eventLog.getMessage());
        log.setTimestamp(eventLog.getTimestamp());

        return save(log);
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        eventRepository.deleteAll();
    }
}
