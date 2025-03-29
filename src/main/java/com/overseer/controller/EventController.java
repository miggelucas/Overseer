package com.overseer.controller;

import com.overseer.model.EventLog;
import com.overseer.service.EventService;
import org.springframework.web.bind.annotation.*;

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
