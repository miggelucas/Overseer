package com.overseer.controller;

import com.overseer.model.EventLog;
import com.overseer.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

  private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public EventLog createLog(@RequestBody EventLog eventLog) {
        return eventService.save(eventLog);
    }

    @GetMapping
    public List<EventLog> getPins() {
        return eventService.getAllLogs();
    }

    @GetMapping("/application/{applicationName}")
    public List<EventLog> getLogsByApplication(@PathVariable("applicationName") String applicationName) {
        List<EventLog> logs = eventService.getByApplication(applicationName);
        if (logs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No logs found for application: " + applicationName);
        }
        return logs;
    }

    @GetMapping("/id={id}")
    public EventLog getPinById(@PathVariable("id") Long id) {
        return eventService.getById(id);
    }

    @PutMapping("/id={id}")
    public EventLog updatePin(@RequestBody EventLog eventLog, @PathVariable("id") Long id) {
        return eventService.update(eventLog, id);
    }

    @DeleteMapping("/id={id}")
    public void deletePinById(@PathVariable("id") Long id) {
        eventService.deleteById(id);
    }

    @DeleteMapping
    public void deleteAllPins() {
        eventService.deleteAll();
    }
}
