package com.overseer.service;

import com.overseer.model.EventLog;
import com.overseer.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public EventLog save(EventLog eventLog) {
        return eventRepository.save(eventLog);
    }

    @Override
    public List<EventLog> getAllLogs() {
        return (List<EventLog>) eventRepository.findAll();
    }

    @Override
    public EventLog getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EventLog not found with id: " + id));
    }

    @Override
    public EventLog update(EventLog eventLogDetails, Long id) {
        EventLog log = getById(id);

        log.setApplication(eventLogDetails.getApplication());
        log.setLevel(eventLogDetails.getLevel());
        log.setMessage(eventLogDetails.getMessage());

        return eventRepository.save(log);
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
