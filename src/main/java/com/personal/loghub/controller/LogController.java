package com.personal.loghub.controller;

import com.personal.loghub.model.LogEntry;
import com.personal.loghub.service.LogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public LogEntry createLog(@RequestBody LogEntry logEntry) {
        return logService.save(logEntry);
    }

    @GetMapping
    public List<LogEntry> getPins() {
        return logService.getAllLogs();
    }

    @GetMapping("/id={id}")
    public LogEntry getPinById(@PathVariable("id") Long id) {
        return logService.getById(id);
    }

    @PutMapping("/id={id}")
    public LogEntry updatePin(@RequestBody LogEntry logEntry, @PathVariable("id") Long id) {
        return logService.update(logEntry, id);
    }

    @DeleteMapping("/id={id}")
    public void deletePinById(@PathVariable("id") Long id) {
        logService.deleteById(id);
    }

    @DeleteMapping
    public void deleteAllPins() {
        logService.deleteAll();
    }
}
